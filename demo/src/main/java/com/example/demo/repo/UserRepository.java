package com.example.demo.repo;

import com.example.demo.domain.ZetchUser;
import org.springframework.data.repository.CrudRepository;

/**
 * User Repository Interface
 * Will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
 */
public interface UserRepository extends CrudRepository<ZetchUser, Integer> {
}
