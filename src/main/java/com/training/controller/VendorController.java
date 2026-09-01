package com.training.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.business.bean.VendorBean;
import com.training.service.VendorService;

@RestController
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Vendor Service API!";
    }

    @GetMapping("/vendor/controller/getVendors")
    public ResponseEntity<List<VendorBean>> getVendorDetails() {
        List<VendorBean> list = vendorService.getVendorDetails();
        return ResponseEntity.ok(list);
    }
}
