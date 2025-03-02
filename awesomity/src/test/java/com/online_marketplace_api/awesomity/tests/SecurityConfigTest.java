//package com.online_marketplace_api.awesomity.tests;
//
//import com.online_marketplace_api.awesomity.Entity.User;
//import com.online_marketplace_api.awesomity.enums.Role;
//import com.online_marketplace_api.awesomity.Repository.IUserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class SecurityConfigTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private IUserRepository userRepository;
//
//    @Autowired
//    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
//
//    private User adminUser;
//    private User buyerUser;
//    private User sellerUser;
//
//    @BeforeEach
//    public void setup() {
//        userRepository.deleteAll();
//
//        adminUser = createUser("admin", "admin@example.com", Role.ADMIN);
//        buyerUser = createUser("buyer", "buyer@example.com", Role.BUYER);
//        sellerUser = createUser("seller", "seller@example.com", Role.SELLER);
//    }
//
//    private User createUser(String username, String email, Role role) {
//        User user = new User();
//        user.setFirstName(username);
//        user.setLastName(username);
//        user.setEmail(email);
//        user.setEnabled(true);
//        user.setEmailVerified(true);
//        user.setPassword(passwordEncoder.encode("password"));
//        user.setRole(role);
//        return userRepository.save(user);
//    }
//
////    @Test
////    public void testPublicEndpointsAreAccessible() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/login"))
////                .andExpect(MockMvcResultMatchers.status().isOk());
////    }
//
////    @Test
////    public void testLoginSuccess() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content("{\"email\":\"admin@example.com\", \"password\":\"password\"}"))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()); // Check that token exists
////    }
//
//    @Test
//    public void testLoginInvalidCredentials() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"admin@example.com\", \"password\":\"wrongpassword\"}"))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized()); // Or isForbidden depending on your security setup.
//    }
//
//    @Test
//    public void testUnauthenticatedAccessToSecuredEndpoint() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders"))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
//    }
//}