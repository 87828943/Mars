package com.mars.service;

import com.mars.entity.BillType;
import com.mars.repository.BillTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillTypeService {

    @Autowired
    private BillTypeRepository billTypeRepository;


    public List<BillType> findAll() {
        return billTypeRepository.findAll();
    }
}
