package com.smart.controller.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", "Home - Smart Contact Manager"));
    }
    @Test
    @WithMockUser
    public void testSignUpPage() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", "Sign Up"))
                .andExpect(model().attributeExists("user")); // Ensure the user attribute is checked
    }

    @Test
    @WithMockUser
    public void testAboutPage() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", "About - Smart Contact Manager"));
    }

    @Test
    @WithMockUser
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", "Login Page"));
    }
}
