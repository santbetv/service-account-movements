/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.repository;


import com.pichincha.serviciocuentamovimiento.infrastructure.adapter.out.db.model.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Santiago Betancur
 */
@Repository
public interface CuentaRepository extends JpaRepository<CuentaEntity, Long> {
    
}
