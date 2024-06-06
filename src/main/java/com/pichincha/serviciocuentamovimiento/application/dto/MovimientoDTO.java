/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.application.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Santiago Betancur
 */
@Getter
@Setter
public class MovimientoDTO {
    
    @Positive(message = "Debe ser numero positivo")
    private Long idCliente;
    @Positive(message = "Debe ser numero positivo")
    private Long idCuenta;
    @NotEmpty(message = "no puede estar vacio")
    private String tipoMovimiento;
    private BigDecimal valor;
}
