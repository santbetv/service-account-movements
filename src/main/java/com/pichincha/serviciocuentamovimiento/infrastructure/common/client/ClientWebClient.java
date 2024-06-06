/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.infrastructure.common.client;

import com.pichincha.serviciocuentamovimiento.domain.Cliente;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author Santiago Betancur
 */
@Component
public class ClientWebClient {

    private final WebClient.Builder webClientBuilder;

//    @Value("${URL.PRODUCT}")
//    private String urlProduct = "http://localhost:8089/api";
//
//    @Value("${URL.TRANSACTION}")
//    private String urlTransaction;

    @Autowired
    public ClientWebClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    //webClient requires HttpClient library to work propertly       
    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain 
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    public <T> List<T> getList(String URL, Class<T> tipo) throws UnknownHostException {
        List<T> Clientes = new ArrayList<>();
        try {
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl(URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", URL))
                    .build();

            List<T> block = build.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                    .path("/clientes")
                    .build())
                    .retrieve().bodyToFlux(tipo).collectList().block();
            
            if (!block.isEmpty()) {
                Clientes = (List<T>) block;
            }

        } catch (WebClientResponseException e) {

            HttpStatus httpStatus = (HttpStatus) e.getStatusCode();
            if (httpStatus == HttpStatus.NOT_FOUND) {
                return new ArrayList<>();
            } else {
                throw new UnknownHostException(e.getMessage());
            }
        }
        return Clientes;
    }

}
