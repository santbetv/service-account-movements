/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.domain.port.in.web;
import com.pichincha.serviciocuentamovimiento.application.dto.MovimientoDTO;
import com.pichincha.serviciocuentamovimiento.application.dto.MovimientoResponse;
import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.MovimientoEntity;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleException;
import com.pichincha.serviciocuentamovimiento.infrastructure.common.exception.BussinesRuleMovimientoValidationException;
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
public interface WebPortMovimientos {
    
    @GetMapping("/movimientos")
    public List<MovimientoEntity> list();

    @GetMapping("/movimientos/{id}")
    public MovimientoEntity get(@PathVariable Long id) throws BussinesRuleException;

    @GetMapping("/movimientos/fecha/{fecha}/cliente/{id}")
    public List<MovimientoResponse> gets(@PathVariable String fecha,@PathVariable Long id);

    @PostMapping("/movimientos")
    public ResponseEntity<?> post(@Valid @RequestBody MovimientoDTO input, BindingResult result) throws BussinesRuleValidationException, BussinesRuleException, BussinesRuleMovimientoValidationException;

    @PutMapping("/movimientos/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody MovimientoDTO input, BindingResult result, @PathVariable Long id) throws BussinesRuleException, BussinesRuleValidationException;
    
    @DeleteMapping("/movimientos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws BussinesRuleException;

}
