package com.example.work.controller;

import com.example.work.models.ERole;
import com.example.work.models.Role;
import com.example.work.models.User;
import com.example.work.payload.request.LoginRequest;
import com.example.work.payload.request.SignupRequest;
import com.example.work.payload.response.JwtResponse;
import com.example.work.payload.response.MessageResponse;
import com.example.work.repository.RoleRepository;
import com.example.work.repository.UserRepository;
import com.example.work.security.jwt.JwtUtils;
import com.example.work.security.services.CRUD.UserService;
import com.example.work.security.services.UserDetailsImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private UserService userService;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  public AuthController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    Cookie cookie = new Cookie("access_token", jwt);
    response.addCookie(cookie);
    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }


  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest user) {
    Gson gson = new Gson();

    System.out.println(gson.toJson(user));
    if (userRepository.existsByUsername(user.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(user.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }
    User user1 = new User(user.getUsername(),user.getEmail(),encoder.encode(user.getPassword()));

    userRepository.insert(user1);
    List<Role> roles = new ArrayList<>();
    for(String role: user.getRoles()){
      Optional<Role> r = roleRepository.findByName(ERole.valueOf(role));
      r.ifPresent(roles::add);
    }
    user1.setSignRole(new HashSet<>(roles));

    user1.getRoles().forEach(role -> {
      userRepository.insertUserRole(user1.getId(), role.getId().longValue());
    });
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
