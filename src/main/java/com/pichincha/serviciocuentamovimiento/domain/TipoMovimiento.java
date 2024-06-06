/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.domain;

import lombok.Getter;

/**
 *
 * @author rizzoli
 */
@Getter
public enum TipoMovimiento {
    RETIRO("RETIRO"),
    DEPOSITO("DEPOSITO");
    
    private final String valor;
    
    TipoMovimiento(String valor){
        this.valor = valor;
    }
}
