/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.domain.port.in.web;
import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoRequest;
import com.pichincha.serviciocuentamovimiento.application.dto.CuentaDtoResponse;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.CuentaEntity;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleValidationException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author rizzoli
 */
public interface WebPortCuentas {
    
    @GetMapping("/cuentas/{id}")
    CuentaDtoResponse get(@PathVariable Long id);
    
    @GetMapping("/cuentas")
    List<CuentaEntity> list();
    
    @PostMapping("/cuentas")
    ResponseEntity<?> post(@Valid @RequestBody CuentaDtoRequest input, BindingResult result) throws BussinesRuleValidationException, BussinesRuleException ;
    
    @PutMapping("/cuentas/{id}")
    ResponseEntity<?> put(@Valid @RequestBody CuentaDtoRequest input, BindingResult result, @PathVariable Long id) throws BussinesRuleException, BussinesRuleValidationException;

    @DeleteMapping("/cuentas/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) throws BussinesRuleException;
}
