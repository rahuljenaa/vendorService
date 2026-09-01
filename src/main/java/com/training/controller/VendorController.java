package com.training.controller;

import com.training.business.bean.VendorBean;
import com.training.service.VendorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VendorController {
    @Autowired
    private VendorServiceImpl vendorService;

    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Vendor Service API!";
    }

    @GetMapping("/vendor/controller/getVendors")
    public ResponseEntity<List<VendorBean>> getVendorDetails(){
        List<VendorBean> list = vendorService.getVendorDetails();

        return ResponseEntity.ok(list);
    }
}