package com.teste.dscommerce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductCategoryIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void productsByCategoryArePublicAndPaginated() throws Exception {
    mockMvc.perform(get("/products/category/1")
            .param("page", "0")
            .param("size", "3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(8))
        .andExpect(jsonPath("$.content[0]").exists())
        .andExpect(jsonPath("$.content[2]").exists())
        .andExpect(jsonPath("$.content[3]").doesNotExist());
  }

  @Test
  void productsByCategoryRejectsUnknownCategory() throws Exception {
    mockMvc.perform(get("/products/category/999"))
        .andExpect(status().isNotFound());
  }
}
