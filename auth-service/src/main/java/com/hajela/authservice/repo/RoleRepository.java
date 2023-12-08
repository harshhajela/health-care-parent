package com.hajela.authservice.repo;


import com.hajela.authservice.entities.Role;
import com.hajela.authservice.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(Role name);
}
