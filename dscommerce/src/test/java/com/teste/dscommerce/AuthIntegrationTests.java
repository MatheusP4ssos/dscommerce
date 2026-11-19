package com.teste.dscommerce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {

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
        .andExpect(jsonPath("$.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.tokenType").value("Bearer"))
        .andReturn()
        .getResponse()
        .getContentAsString();
    return objectMapper.readTree(response).get("accessToken").asText();
  }

  @Test
  void loginReturnsTokenForSeededAdmin() throws Exception {
    login("admin@dscommerce.com", "12345678");
  }

  @Test
  void loginFailsWithWrongPassword() throws Exception {
    String body = objectMapper.writeValueAsString(
        Map.of("email", "admin@dscommerce.com", "password", "senha-errada"));
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void protectedEndpointRequiresAuthentication() throws Exception {
    mockMvc.perform(get("/users"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void publicProductsAreAccessibleWithoutToken() throws Exception {
    mockMvc.perform(get("/products"))
        .andExpect(status().isOk());
  }

  @Test
  void clientCannotListUsers() throws Exception {
    String token = login("maria@email.com", "12345678");
    mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  @Test
  void adminCanListUsers() throws Exception {
    String token = login("admin@dscommerce.com", "12345678");
    mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void authenticatedUserEndpointRequiresAuthentication() throws Exception {
    mockMvc.perform(get("/users/me"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void clientCannotAccessAuthenticatedUserEndpoint() throws Exception {
    String token = login("maria@email.com", "12345678");
    mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  @Test
  void adminCanAccessAuthenticatedUserEndpoint() throws Exception {
    String token = login("admin@dscommerce.com", "12345678");
    mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("admin@dscommerce.com"))
        .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"));
  }
}
