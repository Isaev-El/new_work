package com.example.work;

import com.example.work.controller.AuthController;
import com.example.work.models.ERole;
import com.example.work.models.Role;
import com.example.work.models.User;
import com.example.work.payload.request.LoginRequest;
import com.example.work.payload.request.SignupRequest;
import com.example.work.payload.response.JwtResponse;
import com.example.work.repository.RoleRepository;
import com.example.work.repository.UserRepository;
import com.example.work.security.jwt.JwtUtils;
import com.example.work.security.services.CRUD.UserService;
import com.example.work.security.services.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;



	@Mock
	private UserService userService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtils jwtUtils;

	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager, userRepository, roleRepository, passwordEncoder, jwtUtils)).build();
	}

	@Test
	public void testSignup() throws Exception {
		String request = "{\"username\":\"testuser\",\"email\":\"testuser@example.com\",\"password\":\"testpass\",\"roles\":[\"ROLE_USER\"]}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"));
	}

	@Test
	public void testAuthenticateUser() throws Exception {
		String json = "{\"username\":\"testuser\", \"password\":\"testpass\"}";

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String responseJson = result.getResponse().getContentAsString();
		JSONObject response = new JSONObject(responseJson);
		String accessToken = response.getString("accessToken");
		assertNotNull(accessToken, "JWT token not found in response body");
	}
}