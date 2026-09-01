package com.training.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.training.service.VendorService;

@SpringBootTest
class VendorServiceTest {

	@Autowired
	private VendorService vendorService;

	@Test
	void notNullVendorServiceTest() {
		assertNotNull(vendorService);
	}

	@Test
	void notNullGetVendorDetailsTest() {
		assertNotNull(vendorService.getVendorDetails());
	}

	@Test
	void countGetVendorDetailsTest() {
		assertEquals(5, vendorService.getVendorDetails().size());
	}

	@Test
	void recordGetVendorDetailsTest() {
		assertEquals("Only Vimal", vendorService.getVendorDetails().get(0).getVendorName());
	}

}
