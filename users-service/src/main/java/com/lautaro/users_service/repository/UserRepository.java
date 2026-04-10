package com.lautaro.users_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lautaro.users_service.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

}
