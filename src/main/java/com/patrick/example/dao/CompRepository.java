package com.patrick.example.dao;

import com.patrick.example.model.Comp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompRepository extends JpaRepository<Comp, Long> {
	
}
