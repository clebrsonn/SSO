package com.api.mv.sso.repository;

import com.api.mv.sso.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByRole(String role);

}
