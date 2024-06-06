/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.application;

import com.pichincha.serviciocuentamovimiento.domain.Cliente;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.client.ClientWebClient;
import jakarta.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author rizzoli
 */
@Component
public class HandleMessage {

    private ClientWebClient client;
    private RabbitTemplate rabbitTemplate;
    private List<Cliente> clientes;
    
    @Value("${URL.CLIENTE}")
    private String urlCliente;

    public HandleMessage(RabbitTemplate rabbitTemplate, ClientWebClient client) {
        this.rabbitTemplate = rabbitTemplate;
        this.client = client;
    }

    @RabbitListener(queues = "my_queue_cliente")
    public void handleClienteMessage(Cliente cliente) {
        actualizarCliente(cliente);
    }

    public Cliente obtenerClientePorId(Long idCliente) {
        // Buscar el cliente por su id en la lista
        return clientes.stream()
                .filter(cliente -> cliente.getIdCliente().equals(idCliente))
                .findFirst()
                .orElse(null);
    }

    public void actualizarCliente(Cliente cliente) {
        // Buscar y agregar el cliente en la lista
        boolean clienteExistente = clientes.stream().anyMatch(data -> data.getIdentificacion().equals(cliente.getIdentificacion()));

        if (!clienteExistente) {
            clientes.add(cliente);
        }

    }

    private void actualizarClienteInicio(List<Cliente> cliente) {
        // Buscar y reemplazar el cliente en la lista
        clientes = new ArrayList<>();
        clientes.addAll(cliente);
    }

    @PostConstruct
    public void init() throws UnknownHostException {
        String urlProduct = urlCliente;
        actualizarClienteInicio(client.getList(urlProduct, Cliente.class));
    }

    public Cliente obtenerClienteDeCola(Long idCliente) {
        // Consume el mensaje de la cola "my_queue_cuenta" y obtiene el cliente
        Object mensaje = rabbitTemplate.receiveAndConvert("my_queue_cuenta");

        if (mensaje instanceof Cliente) {
            Cliente cliente = (Cliente) mensaje;

            // Comprueba si el idCliente coincide
            if (cliente.getIdCliente().equals(idCliente)) {
                return cliente;
            } else {
                System.out.println("El cliente obtenido de la cola no coincide con el idCliente proporcionado.");
                return null;
            }
        } else {
            System.out.println("El mensaje recibido de la cola no es de tipo Cliente.");
            return null;
        }
    }

}
