package com.example.demo.repo;

import com.example.demo.domain.ZetchUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ZetchUser, String> {
}
