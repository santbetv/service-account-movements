package com.pichincha.serviciocuentamovimiento.application.serviceimpl;

import com.pichincha.serviciocuentamovimiento.domain.Cliente;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.CuentaEntity;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.repository.CuentaRepository;
import com.pichincha.serviciocuentamovimiento.application.HandleMessage;
import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoRequest;
import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoResponse;
import com.pichincha.serviciocuentamovimiento.domain.Cuenta;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.notification.Publisher;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleValidationException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import com.pichincha.serviciocuentamovimiento.domain.port.out.db.CuentaService;

/**
 *
 * @author Santiago Betancur
 */
@Service
public class CuentaServiceImpl implements CuentaService {

    private static final Logger LOG = LoggerFactory.getLogger(CuentaServiceImpl.class);

    private final String INFO_URL = "api/cuenta";

    private CuentaRepository cuentaRepository;
    private HandleMessage handleMessage;
    private Publisher publisher;

    @Autowired
    public CuentaServiceImpl(CuentaRepository cuentaRepository, HandleMessage handleMessage, Publisher publisher) {
        this.cuentaRepository = cuentaRepository;
        this.handleMessage = handleMessage;
        this.publisher = publisher;
    }

    @Override
    @Transactional(readOnly = true) //
    public CuentaDtoResponse findById(Long id) {
        Optional<CuentaEntity> cuenta = cuentaRepository.findById(id);
        Cuenta c = new Cuenta();
        if (!cuenta.isEmpty()) {
            CuentaEntity ce = cuentaRepository.findById(id).get();
            Cliente cliente = handleMessage.obtenerClientePorId(ce.getIdCliente());
            c = Cuenta.builder().numeroNuenta(ce.getNumeroNuenta())
                    .tipoCuenta(ce.getTipoCuenta())
                    .saldoInicial(ce.getSaldoInicial())
                    .estado(ce.getEstado())
                    .cliente(cliente).build();
            c.setIdCuenta(ce.getIdCuenta());
            return new CuentaDtoResponse(c);
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    @Transactional(readOnly = true) //
    public List<CuentaEntity> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional //
    public CuentaDtoResponse save(CuentaDtoRequest cuenta, BindingResult result) throws BussinesRuleValidationException, BussinesRuleException {

        if (result.hasErrors()) {
            BussinesRuleValidationException exception = new BussinesRuleValidationException(INFO_URL, result);
            throw exception;
        } else {
            Cliente cliente = handleMessage.obtenerClientePorId(cuenta.getIdCliente());

            Cuenta c = new Cuenta();
            if (cliente != null) {
                c = Cuenta.builder().numeroNuenta(cuenta.getNumeroNuenta())
                        .tipoCuenta(cuenta.getTipoCuenta())
                        .saldoInicial(cuenta.getSaldoInicial())
                        .estado(cuenta.getEstado())
                        .cliente(cliente).build();
            } else {
                BussinesRuleException exception = new BussinesRuleException("No se pudo asociar un cliente a la cuenta. El cliente con ID " + cuenta.getIdCliente() + " no fue encontrado.");
                throw exception;
            }

            CuentaEntity ce = cuentaRepository.save(c.crearCuenta());

            c.setIdCuenta(ce.getIdCuenta());
            publisher.send(c);
            return new CuentaDtoResponse(c);
        }
    }

    @Override
    @Transactional//
    public void put(CuentaDtoRequest cuentaDTO, BindingResult result, Long id) throws BussinesRuleException, BussinesRuleValidationException {
        Optional<CuentaEntity> find = cuentaRepository.findById(id);

        if (!find.isEmpty()) {
            if (result.hasErrors()) {
                BussinesRuleValidationException exception = new BussinesRuleValidationException(INFO_URL, result);
                throw exception;
            } else {
                find.get().setEstado(cuentaDTO.getEstado());
                find.get().setNumeroNuenta(cuentaDTO.getNumeroNuenta());
                find.get().setSaldoInicial(cuentaDTO.getSaldoInicial());
                find.get().setTipoCuenta(cuentaDTO.getTipoCuenta());
                CuentaEntity ce = cuentaRepository.save(find.get());
                cuentaRepository.save(ce);
            }
        } else {
            BussinesRuleException exception = new BussinesRuleException(INFO_URL);
            throw exception;
        }
    }

    @Override
    @Transactional //
    public void delete(Long id) throws BussinesRuleException {
        Optional<CuentaEntity> cuenta = cuentaRepository.findById(id);
        if (!cuenta.isEmpty()) {
            cuentaRepository.delete(cuenta.get());
        } else {
            BussinesRuleException exception = new BussinesRuleException(INFO_URL);
            throw exception;
        }
    }

}
