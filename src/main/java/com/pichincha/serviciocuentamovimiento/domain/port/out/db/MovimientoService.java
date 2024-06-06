/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.domain.port.out.db;

import com.pichincha.serviciocuentamovimiento.application.dto.MovimientoDTO;
import com.pichincha.serviciocuentamovimiento.application.dto.MovimientoResponse;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.MovimientoEntity;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleMovimientoValidationException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleValidationException;

import java.util.List;
import org.springframework.validation.BindingResult;

/**
 *
 * @author Santiago Betancur
 */
public interface MovimientoService {

    public MovimientoEntity findById(Long id) throws BussinesRuleException;

    public List<MovimientoEntity> findAll();

    public List<MovimientoResponse> findByFechaAndByCustomer(String fecha, Long idCliente);

    public MovimientoResponse save(MovimientoDTO movimientoDTO, BindingResult result) throws BussinesRuleValidationException, BussinesRuleException, BussinesRuleMovimientoValidationException;

    public void put(MovimientoDTO movimientoDTO, BindingResult result, Long id) throws BussinesRuleException, BussinesRuleValidationException;

    public void delete(Long id) throws BussinesRuleException;
}
