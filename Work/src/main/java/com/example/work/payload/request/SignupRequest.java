package com.example.work.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private List<String> roles;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  public SignupRequest(String test, String s, String test1234, List<String> role_user) {
  }
}
