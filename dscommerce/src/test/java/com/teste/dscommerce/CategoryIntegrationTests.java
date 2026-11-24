package com.teste.dscommerce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private String login(String email, String password) throws Exception {
    String body = objectMapper.writeValueAsString(Map.of("email", email, "password", password));
    String response = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    return objectMapper.readTree(response).get("accessToken").asText();
  }

  private String categoryBody(String name) throws Exception {
    return objectMapper.writeValueAsString(Map.of("name", name));
  }

  @Test
  void getCategoriesIsPublic() throws Exception {
    mockMvc.perform(get("/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").isNotEmpty());
  }

  @Test
  void postCategoryRules() throws Exception {
    String name = "Categoria " + UUID.randomUUID();
    String body = categoryBody(name);
    String clientToken = login("maria@email.com", "12345678");
    String adminToken = login("admin@dscommerce.com", "12345678");

    mockMvc.perform(post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(post("/categories")
            .header("Authorization", "Bearer " + clientToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isForbidden());

    mockMvc.perform(post("/categories")
            .header("Authorization", "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value(name));
  }

  @Test
  void deleteCategoryRules() throws Exception {
    String body = categoryBody("Categoria " + UUID.randomUUID());
    String adminToken = login("admin@dscommerce.com", "12345678");
    String clientToken = login("maria@email.com", "12345678");
    String response = mockMvc.perform(post("/categories")
            .header("Authorization", "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
    Long id = objectMapper.readTree(response).get("id").asLong();

    mockMvc.perform(delete("/categories/{id}", id))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(delete("/categories/{id}", id)
            .header("Authorization", "Bearer " + clientToken))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/categories/{id}", id)
            .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isNoContent());

    mockMvc.perform(delete("/categories/{id}", id)
            .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void postCategoryRejectsBlankName() throws Exception {
    String adminToken = login("admin@dscommerce.com", "12345678");

    mockMvc.perform(post("/categories")
            .header("Authorization", "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(categoryBody("")))
        .andExpect(status().isUnprocessableEntity());
  }
}
