package com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "cuentas")
public class CuentaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long idCuenta;

    @Column(name = "numero_nuenta")
    private String numeroNuenta;

    @Column(name = "tipo_cuenta")
    private String tipoCuenta;

    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;

    private Boolean estado;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    //@JsonIgnore // Evita la serialización finita no mostrar lista de cuentaMovimientos
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objCuentaMovimiento", cascade = CascadeType.ALL)
    private List<MovimientoEntity> cuentaMovimientos;

    public CuentaEntity() {
        this.cuentaMovimientos = new ArrayList<>();
    }

    public CuentaEntity(Long idCuenta, String numeroNuenta, String tipoCuenta, BigDecimal saldoInicial, Boolean estado, Long idCliente) {
        this.idCuenta = idCuenta;
        this.numeroNuenta = numeroNuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.idCliente = idCliente;
    }
    
    

}
