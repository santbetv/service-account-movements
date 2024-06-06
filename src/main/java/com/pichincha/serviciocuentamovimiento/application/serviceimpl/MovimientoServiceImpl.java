/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.application.serviceimpl;

import com.pichincha.serviciocuentamovimiento.application.HandleMessage;
import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoResponse;
import com.pichincha.serviciocuentamovimiento.application.dto.MovimientoDTO;
import com.pichincha.serviciocuentamovimiento.application.dto.MovimientoResponse;
import com.pichincha.serviciocuentamovimiento.domain.Cliente;
import com.pichincha.serviciocuentamovimiento.domain.Cuenta;
import com.pichincha.serviciocuentamovimiento.domain.Movimiento;
import com.pichincha.serviciocuentamovimiento.domain.TipoMovimiento;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.MovimientoEntity;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.repository.MovimientoRepository;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleMovimientoValidationException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import com.pichincha.serviciocuentamovimiento.domain.port.out.db.CuentaService;
import com.pichincha.serviciocuentamovimiento.domain.port.out.db.MovimientoService;

/**
 *
 * @author Santiago Betancur
 */
@Service
public class MovimientoServiceImpl implements MovimientoService {

    private static final Logger LOG = LoggerFactory.getLogger(MovimientoServiceImpl.class);

    private final String INFO_URL = "api/movimiento";
    private static final String SALDO_NO_DISPONIBLE = "Saldo no disponible";

    private CuentaService iCuentaService;
    private MovimientoRepository movimientoRepository;
    private HandleMessage handleMessage;

    @Autowired
    public MovimientoServiceImpl(CuentaService iCuentaService, MovimientoRepository movimientoRepository, HandleMessage handleMessage) {
        this.iCuentaService = iCuentaService;
        this.movimientoRepository = movimientoRepository;
        this.handleMessage = handleMessage;
    }

    @Override
    @Transactional(readOnly = true) //
    public MovimientoEntity findById(Long id) throws BussinesRuleException {
        Optional<MovimientoEntity> movimiento = movimientoRepository.findById(id);
        if (!movimiento.isEmpty()) {
            return movimientoRepository.findById(id).get();
        } else {
            BussinesRuleException exception = new BussinesRuleException(INFO_URL);
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true) //
    public List<MovimientoEntity> findAll() {
        List<MovimientoEntity> l = movimientoRepository.findAll();
        return l;
    }

    @Override
    @Transactional(readOnly = true) //
    public List<MovimientoResponse> findByFechaAndByCustomer(String fecha, Long idCliente) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate fechaEntrada = LocalDate.parse(fecha, formatter);

        List<MovimientoResponse> listaMovimientos = new ArrayList<>();
        List<MovimientoEntity> movimientoFecha = movimientoRepository.datosClientePorFecha(fechaEntrada, idCliente);

        movimientoFecha.forEach(info
                -> listaMovimientos.add(new MovimientoResponse(info, handleMessage.obtenerClientePorId(info.getIdCliente())))
        );

        return listaMovimientos;
    }

    @Override
    @Transactional //
    public MovimientoResponse save(MovimientoDTO movimientoDTO, BindingResult result) throws BussinesRuleValidationException, BussinesRuleException, BussinesRuleMovimientoValidationException {

        if (result.hasErrors()) {
            BussinesRuleValidationException exception = new BussinesRuleValidationException(INFO_URL, result);
            throw exception;
        } else {
            CuentaDtoResponse cuenta = iCuentaService.findById(movimientoDTO.getIdCuenta());
            Cliente cliente = handleMessage.obtenerClientePorId(movimientoDTO.getIdCliente());
            BigDecimal infoUltimoSaldo = movimientoRepository.ultimoSaldo(movimientoDTO.getIdCliente(), movimientoDTO.getIdCuenta());

            Movimiento m = new Movimiento();
            if (TipoMovimiento.RETIRO.getValor().equals(movimientoDTO.getTipoMovimiento()) && cuenta.getSaldoInicial().compareTo(BigDecimal.ZERO) == 0) {
                BussinesRuleMovimientoValidationException exception = new BussinesRuleMovimientoValidationException(INFO_URL, SALDO_NO_DISPONIBLE);
                throw exception;
            } else {
                if (TipoMovimiento.RETIRO.getValor().equals(movimientoDTO.getTipoMovimiento()) && infoUltimoSaldo != null) {
                    BussinesRuleMovimientoValidationException exception = new BussinesRuleMovimientoValidationException(INFO_URL, SALDO_NO_DISPONIBLE);
                    throw exception;
                }
                m.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
                m.setValor(movimientoDTO.getValor());
                m.setSaldo(m.agregarSaldoTotal(cuenta.getSaldoInicial(), movimientoDTO.getTipoMovimiento(), infoUltimoSaldo));
                m.setObjCuentaMovimiento(new Cuenta().crearCuenta(cuenta, cliente));
                m.setObjClienteMovimiento(cliente);

                movimientoRepository.save(m.crearMovimiento());
            }
            return new MovimientoResponse().build(m);
        }
    }

    @Override
    @Transactional //
    public void put(MovimientoDTO movimientoDTO, BindingResult result, Long id) throws BussinesRuleException, BussinesRuleValidationException {
        Optional<MovimientoEntity> find = movimientoRepository.findById(id);

        if (!find.isEmpty()) {
            if (result.hasErrors()) {
                BussinesRuleValidationException exception = new BussinesRuleValidationException(INFO_URL, result);
                throw exception;
            } else {
                
                CuentaDtoResponse cuenta = iCuentaService.findById(find.get().getObjCuentaMovimiento().getIdCuenta());
                Cliente cliente = handleMessage.obtenerClientePorId(find.get().getIdCliente());
                
                Movimiento m = new Movimiento();
                m.crearMovimientoActual(find.get(), cliente, new Cuenta().crearCuenta(cuenta, cliente),movimientoDTO.getTipoMovimiento(),movimientoDTO.getValor());

                find.get().setValor(m.getValor());
                find.get().setTipoMovimiento(m.getTipoMovimiento());
                find.get().setSaldo(m.getSaldo());
                
                MovimientoEntity save = movimientoRepository.save(find.get());
            }
        } else {
            BussinesRuleException exception = new BussinesRuleException(INFO_URL);
            throw exception;
        }
    }

    @Override
    public void delete(Long id) throws BussinesRuleException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
