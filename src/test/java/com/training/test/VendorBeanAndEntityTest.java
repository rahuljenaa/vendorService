package com.training.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.training.business.bean.VendorBean;
import com.training.entity.VendorEntity;

class VendorBeanAndEntityTest {

    @Test
    void testVendorBean() {
        VendorBean bean = new VendorBean();
        bean.setVendorId("V001");
        bean.setVendorName("Only Vimal");
        bean.setVendorAddress("Delhi");
        bean.setContactPerson("John");
        bean.setContactNumber("9002348970");

        assertEquals("V001", bean.getVendorId());
        assertEquals("Only Vimal", bean.getVendorName());
        assertEquals("Delhi", bean.getVendorAddress());
        assertEquals("John", bean.getContactPerson());
        assertEquals("9002348970", bean.getContactNumber());
        assertNotNull(bean.toString());
        assertTrue(bean.toString().contains("V001"));

        VendorBean paramBean = new VendorBean("V002", "PRR", "Mumbai", "Sam", "8700112345");
        assertEquals("V002", paramBean.getVendorId());
    }

    @Test
    void testVendorEntity() {
        VendorEntity entity = new VendorEntity();
        entity.setVendorId("V001");
        entity.setVendorName("Only Vimal");
        entity.setVendorAddress("Delhi");
        entity.setContactPerson("John");
        entity.setContactNumber("9002348970");

        assertEquals("V001", entity.getVendorId());
        assertEquals("Only Vimal", entity.getVendorName());
        assertEquals("Delhi", entity.getVendorAddress());
        assertEquals("John", entity.getContactPerson());
        assertEquals("9002348970", entity.getContactNumber());
        assertNotNull(entity.toString());
        assertTrue(entity.toString().contains("V001"));

        VendorEntity paramEntity = new VendorEntity("V002", "PRR", "Mumbai", "Sam", "8700112345");
        assertEquals("V002", paramEntity.getVendorId());
    }
}
