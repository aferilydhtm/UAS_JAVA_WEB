package com.uas.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uas.course.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
