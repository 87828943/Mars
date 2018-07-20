package com.mars.repository;

import com.mars.entity.BillType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillTypeRepository extends JpaRepository<BillType, Long> {
}
