/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.domain;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rizzoli
 */
public class MovimientoTest {

    public MovimientoTest() {
    }

    @Test
    public void test_agregar_saldo_totalDeposito() {
        // Crear un objeto Movimiento
        Movimiento movimiento = new Movimiento();

        // Mockear el objeto saldoInicial y saldoAnterior
        BigDecimal saldoInicial = new BigDecimal("1000.0");
        BigDecimal saldoAnterior = new BigDecimal("500.0");

        // Establecer los valores de prueba en el movimiento
        movimiento.setSaldo(BigDecimal.ZERO);
        movimiento.setValor(new BigDecimal("200.0"));

        // Simular el tipo de movimiento
        movimiento.setTipoMovimiento("DEPOSITO");

        // Llamar al método agregarSaldoTotal
        BigDecimal resultado = movimiento.agregarSaldoTotal(saldoInicial, movimiento.getTipoMovimiento(), saldoAnterior);

        // Verificar que se llamó a los métodos de BigDecimal correctamente
        assertEquals(new BigDecimal("700.0"), resultado);
    }

}
