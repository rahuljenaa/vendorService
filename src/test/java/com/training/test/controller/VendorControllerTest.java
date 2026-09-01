package com.training.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.training.business.bean.VendorBean;
import com.training.controller.VendorController;
import com.training.service.VendorService;

@WebMvcTest(VendorController.class)
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService vendorServiceMock;

    @Test
    void indexVendorControllerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
               .andExpect(status().isOk())
               .andExpect(content().string("Welcome to Spring Boot Vendor Service API!"));
    }

    @Test
    void getVendorDetailsControllerTest() throws Exception {
        VendorBean bean = new VendorBean("V001", "Only Vimal", "Delhi", "John", "9002348970");
        when(vendorServiceMock.getVendorDetails()).thenReturn(List.of(bean));

        mockMvc.perform(MockMvcRequestBuilders.get("/vendor/controller/getVendors")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].vendorId").value("V001"))
               .andExpect(jsonPath("$[0].vendorName").value("Only Vimal"));
    }
}
