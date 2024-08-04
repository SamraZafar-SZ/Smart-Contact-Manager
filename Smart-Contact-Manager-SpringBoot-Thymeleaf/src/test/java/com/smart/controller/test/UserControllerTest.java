package com.smart.controller.test;

import com.smart.controller.UserController;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.config.CustomUserDetails;
import com.smart.config.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerTest {

	private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ContactRepository contactRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Mock the Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Principal
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(principal);

        // Mock the UserDetailsService
        User user = new User();
        user.setName("testUser");
        UserDetails userDetails = new CustomUserDetails(user);
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(userRepository.getUserByUsername("testUser")).thenReturn(user);
    }

    @Test
    public void contextLoads() {
        assertThat(userController).isNotNull();
    }

    @Test
    public void testUserDashboard() throws Exception {
        mockMvc.perform(get("/user/index").principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/user_dashboard"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", "Dashboard"));
    }

    @Test
    public void testAddContactForm() throws Exception {
        mockMvc.perform(get("/user/add-contact").principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/add_contact_form"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", "Add Contact"));
    }

   
    @Test
    public void testShowContacts() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("testUser");

        Page<Contact> contacts = mock(Page.class);

        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(contactRepository.findContactsByUser(anyInt(), (Pageable) any(PageRequest.class))).thenReturn(contacts);

        mockMvc.perform(get("/user/show-contacts/0").principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/show_contacts"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    public void testContactDetail() throws Exception {
        User user = new User();
        user.setId(1); // Use int type for ID
        user.setName("testUser");

        Contact contact = new Contact();
        contact.setcID(1); // Use int type for ID
        contact.setFirstName("Test Contact");
        contact.setUser(user); // Set the user for the contact

        when(contactRepository.findById(anyInt())).thenReturn(Optional.of(contact));
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/1/contact").principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/contact_detail"))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attribute("contact", contact));
    }


    @Test
    public void testProcessUpdate() throws Exception {
        Contact contact = new Contact();
        contact.setcID(1); // Use int type for ID
        contact.setFirstName("Updated Contact");

        User user = new User();
        user.setId(1);
        user.setName("testUser");

        when(contactRepository.findById(anyInt())).thenReturn(Optional.of(contact));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);

        MockMultipartFile file = new MockMultipartFile("profileImage", "test.txt", "text/plain", "This is a test file.".getBytes());

        mockMvc.perform(multipart("/user/process-update")
                .file(file)
                .principal(() -> "testUser")
                .param("cID", "1")  // Include cID parameter
                .param("firstName", "Updated Contact")
                .param("email", "updated@example.com")
                .param("phone", "0987654321"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/1/contact"));  // Ensure the redirect URL matches
    }

    @Test
    public void testUpdateForm() throws Exception {
        Contact contact = new Contact();
        contact.setcID(1); // Use int type for ID
        contact.setFirstName("Test Contact");

        when(contactRepository.findById(anyInt())).thenReturn(Optional.of(contact));

        mockMvc.perform(get("/user/update-contact/1").principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/update_form"))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attribute("contact", contact));
    }


    @Test
    public void testDeleteContact() throws Exception {
        Contact contact = new Contact();
        contact.setcID(1); // Use int type for ID
        contact.setFirstName("Test Contact");

        User user = new User();
        user.setId(1);
        user.setName("testUser");
        user.getContacts().add(contact);

        when(contactRepository.findById(anyInt())).thenReturn(Optional.of(contact));
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(get("/user/delete/1").principal(() -> "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/show-contacts/0"));

        verify(contactRepository, times(1)).findById(1);  
        verify(userRepository, times(1)).save(user);      
    }

}
