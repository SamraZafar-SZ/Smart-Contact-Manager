package com.smart.controller.test;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        User user = new User();
        user.setId(1);
        user.setName("testUser");

        Contact contact1 = new Contact();
        contact1.setcID(1);
        contact1.setEmail("test1@example.com");
        contact1.setFirstName("First1");
        contact1.setLastName("Last1");

        Contact contact2 = new Contact();
        contact2.setcID(2);
        contact2.setEmail("test2@example.com");
        contact2.setFirstName("First2");
        contact2.setLastName("Last2");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(contactRepository.findByEmailContainingAndUser(anyString(), eq(user))).thenReturn(contacts);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testSearch() throws Exception {
        String query = "test";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");

        mockMvc.perform(get("/search/{query}", query).principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test1@example.com"))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("First1"))
                .andExpect(jsonPath("$[1].firstName").value("First2"));
    }
}
