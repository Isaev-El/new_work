package com.example.work.repository;

import com.example.work.models.ERole;
import com.example.work.models.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
public interface RoleRepository {
  @Select("SELECT * FROM roles WHERE name = #{name}")
  Optional<Role> findByName(@Param("name") ERole name);
}
