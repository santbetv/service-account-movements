package com.pichincha.serviciocuentamovimiento.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Cliente extends Persona implements Serializable {

    private Long idCliente;
    private String contrasena;
    private Boolean estado;
    private List<Cuenta> cuentas;

    public Cliente() {
        cuentas = new ArrayList<>();
    }

    private static final long serialVersionUID = 4263263470080398951L;

}
