package com.example.work.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public class User {
  private Long id;
  private String username;
  private String email;
  private String password;
  private Set<Role> roles;

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Object roles) {
    Gson gson = new Gson();
    String list = gson.toJson(roles);
    List<String> test =  gson.fromJson(list, new TypeToken<List<String>>(){}.getType());

    List<Role> r = new ArrayList<>();

    for(int i=0; i<test.size(); i++){
      Role role = new Role();
      role.setId(i);
      role.setName(ERole.valueOf(test.get(i)));
      r.add(role);
    }
    this.roles = new HashSet<>(r);
  }
  public void setSignRole(Set<Role> roles){
    this.roles = roles;
  }
}
