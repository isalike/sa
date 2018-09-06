package com.sapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sapi.modal.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByName(String username);
}
