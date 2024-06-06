/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
public class CuentaDtoRequest {
    
    @NotEmpty(message = "No puede estar vacio")
    private String numeroNuenta;
    @NotEmpty(message = "No puede estar vacio")
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    @Positive(message = "Debe ser numero positivo")
    private Long idCliente;
    
}
