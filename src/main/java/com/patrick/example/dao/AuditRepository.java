package com.patrick.example.dao;

import com.patrick.example.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/6 14:38
 * UpdateTime 2017/7/6 14:38
 */
public interface AuditRepository extends JpaRepository<Audit, Long> {
}
