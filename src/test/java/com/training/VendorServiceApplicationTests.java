package com.training;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VendorServiceApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> {});
	}

	@Test
	void mainMethodTest() {
		assertDoesNotThrow(() -> VendorServiceApplication.main(new String[] {"--spring.main.banner-mode=off"}));
	}

	@Test
	void configureMethodTest() {
		VendorServiceApplication application = new VendorServiceApplication();
		SpringApplicationBuilder builder = mock(SpringApplicationBuilder.class);
		when(builder.sources(VendorServiceApplication.class)).thenReturn(builder);
		assertNotNull(application.configure(builder));
	}

}
