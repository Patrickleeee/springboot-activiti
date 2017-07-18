package com.patrick.example.dao;

import com.patrick.example.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/3 14:19
 * UpdateTime 2017/7/3 14:19
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
