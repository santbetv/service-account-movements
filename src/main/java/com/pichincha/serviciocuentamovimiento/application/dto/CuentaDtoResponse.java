/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.application.dto;

import com.pichincha.serviciocuentamovimiento.domain.Cuenta;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author rizzoli
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuentaDtoResponse {
    
    private Long idCuenta;
    private String numeroNuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private Long idCliente;
    private String nombreCliente;
    
    public CuentaDtoResponse(Cuenta c) {
        this.idCuenta = c.getIdCuenta();
        this.numeroNuenta = c.getNumeroNuenta();
        this.tipoCuenta = c.getTipoCuenta();
        this.saldoInicial = c.getSaldoInicial();
        this.estado = c.getEstado();
        this.idCliente = c.getCliente().getIdCliente();
        this.nombreCliente = c.getCliente().getNombre();
    }
    
    
    
}
