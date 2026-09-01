package com.training.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.training.dao.VendorDAO;
import com.training.service.VendorService;
import com.training.service.VendorServiceImpl;

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

	@Test
	void emptyVendorListTest() {
		VendorDAO mockDAO = mock(VendorDAO.class);
		when(mockDAO.findAll()).thenReturn(Collections.emptyList());
		VendorServiceImpl service = new VendorServiceImpl(mockDAO);
		assertTrue(service.getVendorDetails().isEmpty());
	}

}
