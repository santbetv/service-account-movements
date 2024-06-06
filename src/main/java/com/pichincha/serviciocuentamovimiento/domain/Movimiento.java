package com.pichincha.serviciocuentamovimiento.domain;

import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.MovimientoEntity;
import jakarta.persistence.PrePersist;
import java.math.BigDecimal;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor//con costructor full
@NoArgsConstructor//con constructor default
@Builder
public class Movimiento {

    private Long idMovimiento;
    private LocalDate fecha = LocalDate.now();
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo = BigDecimal.ZERO;
    private Cliente objClienteMovimiento;
    private Cuenta objCuentaMovimiento;

    public void crearMovimientoActual(MovimientoEntity movimientoEntity, Cliente cl, Cuenta c, String tipoMovi, BigDecimal valorN) {
        this.idMovimiento = movimientoEntity.getIdMovimiento();
        this.fecha = movimientoEntity.getFecha();
        this.tipoMovimiento = movimientoEntity.getTipoMovimiento();
        this.valor = movimientoEntity.getValor();
        this.saldo = movimientoEntity.getSaldo();
        this.objClienteMovimiento = cl;
        this.objCuentaMovimiento = c;
        actualizarSaldoTotal(tipoMovi,valorN);
    }

    public MovimientoEntity crearMovimiento() {
        return new MovimientoEntity(idMovimiento, fecha, tipoMovimiento, valor, saldo, objClienteMovimiento.getIdCliente(), objCuentaMovimiento.crearCuenta());
    }

    public BigDecimal agregarSaldoTotal(BigDecimal saldoInicial, String claseMovimiento, BigDecimal saldoaAnterior) {

        if (TipoMovimiento.DEPOSITO.getValor().equals(claseMovimiento.toUpperCase())) {
            if (saldoaAnterior != null) {
                saldoInicial = saldoaAnterior;
            }
            this.saldo = saldoInicial.add(this.valor);
        } else {
            actualizarPostSaldoTotal(saldoaAnterior, saldoInicial);
        }

        return this.saldo;
    }

    private BigDecimal actualizarPostSaldoTotal(BigDecimal saldoaAnterior, BigDecimal saldoInicial) {
        if (saldoaAnterior != null) {
            this.saldo = saldoaAnterior;
            if (TipoMovimiento.RETIRO.getValor().equals(this.tipoMovimiento.toUpperCase())) {
                this.saldo = this.saldo.subtract(this.valor);
            }
        } else {
            this.saldo = saldoInicial.subtract(this.valor);
        }

        return this.saldo;
    }

    public BigDecimal actualizarSaldoTotal(String tipo,BigDecimal valorN) {
        if (TipoMovimiento.DEPOSITO.getValor().equals(tipo.toUpperCase())) {
            this.saldo = this.saldo.add(this.valor);
        } else if (TipoMovimiento.RETIRO.getValor().equals(tipo.toUpperCase())) {
            if (this.saldo.compareTo(BigDecimal.ZERO) > 0) {
                if (this.saldo.compareTo(valorN) > 0) {
                    this.tipoMovimiento=TipoMovimiento.RETIRO.getValor();
                    this.saldo = this.saldo.subtract(valorN);
                } else {
                    this.saldo = this.saldo.add(BigDecimal.ZERO);
                }
            } else {
                this.saldo = this.saldo.add(BigDecimal.ZERO);
            }

        }
        return this.saldo;
    }

    private static final long serialVersionUID = -6146988320602532496L;
}
