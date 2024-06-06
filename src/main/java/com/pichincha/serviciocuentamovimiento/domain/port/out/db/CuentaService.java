package com.pichincha.serviciocuentamovimiento.domain.port.out.db;

import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoRequest;
import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoResponse;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.CuentaEntity;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleValidationException;
import java.util.List;
import org.springframework.validation.BindingResult;

/**
 *
 * @author Santiago Betancur
 */
public interface CuentaService {

    public CuentaDtoResponse findById(Long id);

    public List<CuentaEntity> findAll();

    public CuentaDtoResponse save(CuentaDtoRequest cuenta, BindingResult result) throws BussinesRuleValidationException, BussinesRuleException;

    public void put(CuentaDtoRequest cuentaDTO, BindingResult result, Long id) throws BussinesRuleException, BussinesRuleValidationException;
    
    public void delete(Long id) throws BussinesRuleException;
}
