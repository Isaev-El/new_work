package com.example.work.repository;

import com.example.work.models.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Mapper
public interface UserRepository {
  @Insert("INSERT INTO users (username, email, password) VALUES (#{username}, #{email}, #{password})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  void insert(User user);

  @Insert("INSERT INTO user_roles (user_id, role_id) VALUES (#{userId}, #{roleId})")
  void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

  @Select("SELECT u.id, u.username, u.email, u.password, ARRAY_AGG(r.name) as roles " +
          " FROM users u" +
          " JOIN user_roles ur ON u.id = ur.user_id " +
          " JOIN roles r ON ur.role_id = r.id " +
          " where ur.role_id!=2 " +
          " GROUP BY u.id ")
  List<User> getAllUsers();

  @Select("SELECT u.*, ARRAY_AGG(r.name) as roles " +
          "FROM users u " +
          "JOIN user_roles ur ON u.id = ur.user_id " +
          "JOIN roles r ON ur.role_id = r.id " +
          "WHERE u.email = #{email} " +
          "GROUP BY u.id")
  Optional<User> findByEmail(String email);


  @Select("SELECT u.*, ARRAY_AGG(r.name) as roles " +
          "FROM users u " +
          "JOIN user_roles ur ON u.id = ur.user_id " +
          "JOIN roles r ON ur.role_id = r.id " +
          "WHERE u.username = #{username} " +
          "GROUP BY u.id")
  Optional<User> findByUsername(String username);


  @Select("SELECT u.*, ARRAY_AGG(r.name) as roles " +
          "FROM users u " +
          "JOIN user_roles ur ON u.id = ur.user_id " +
          "JOIN roles r ON ur.role_id = r.id " +
          "WHERE u.id = #{id} " +
          "GROUP BY u.id")
  Optional<User> findUserById(Long id);

  @Delete("DELETE FROM users WHERE users.id = #{id}")
  void deleteUser(Long id);

  @Delete("DELETE FROM user_roles WHERE user_id = #{id}")
  void deleteUserRoles(Long id);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE username = #{username})")
  boolean existsByUsername(String username);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean existsByEmail(String email);

  @Update("UPDATE users SET username=CASE WHEN #{username} = '' THEN username ELSE #{username} END, email=CASE WHEN #{email} = '' THEN email ELSE #{email} END WHERE id=#{id}")
  void updateUser(Long id, String username, String email);
}
