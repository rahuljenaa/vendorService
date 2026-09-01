package com.training.test.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.training.dao.VendorDAO;
import com.training.entity.VendorEntity;

@SpringBootTest
class VendorDAOTest {

	@Autowired
	private VendorDAO vendorDAO;

	@Test
	void notNullVendorDAOTest() {
		assertNotNull(vendorDAO);
	}

	@Test
	void findByIdVendorDAOTest() {
		VendorEntity entity = vendorDAO.findById("V001").orElse(null);
		assertNotNull(entity);
		assertEquals("Only Vimal", entity.getVendorName());
	}
}
