/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.application.dto;


import com.pichincha.serviciocuentamovimiento.domain.Cliente;
import com.pichincha.serviciocuentamovimiento.domain.Movimiento;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.MovimientoEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Santiago Betancur
 */
@Getter
@Setter
public class MovimientoResponse {

    private String fecha;
    private String nombre;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private BigDecimal tipoMovimiento;
    private BigDecimal saldo;

    public MovimientoResponse() {
    }

    public MovimientoResponse(Movimiento movimiento) {
        this.fecha = movimiento.getFecha().toString();
        this.nombre = movimiento.getObjClienteMovimiento().getNombre();
        this.numeroCuenta = movimiento.getObjCuentaMovimiento().getNumeroNuenta();
        this.tipoCuenta = movimiento.getObjCuentaMovimiento().getTipoCuenta();
        this.saldoInicial = movimiento.getObjCuentaMovimiento().getSaldoInicial();
        this.estado = movimiento.getObjCuentaMovimiento().getEstado();
        this.tipoMovimiento = movimiento.getValor();
        this.saldo = movimiento.getSaldo();
    }
    public MovimientoResponse(MovimientoEntity movimiento, Cliente cliente) {
        this.fecha = movimiento.getFecha().toString();
        this.nombre = cliente.getNombre();
        this.numeroCuenta = movimiento.getObjCuentaMovimiento().getNumeroNuenta();
        this.tipoCuenta = movimiento.getObjCuentaMovimiento().getTipoCuenta();
        this.saldoInicial = movimiento.getObjCuentaMovimiento().getSaldoInicial();
        this.estado = movimiento.getObjCuentaMovimiento().getEstado();
        this.tipoMovimiento = movimiento.getValor();
        this.saldo = movimiento.getSaldo();
    }
    
    public MovimientoResponse build(Movimiento movimiento) {
        return new MovimientoResponse(movimiento);
    }

}
