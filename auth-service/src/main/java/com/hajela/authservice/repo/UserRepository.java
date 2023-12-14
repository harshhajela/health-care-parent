package com.hajela.authservice.repo;


import com.hajela.authservice.entities.RoleEntity;
import com.hajela.authservice.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {"role"})
    Optional<UserEntity> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"role"})
    Page<UserEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"role"})
    Page<UserEntity> findAllByRoleOrderByUserIdDesc(Pageable pageable, RoleEntity role);
}
