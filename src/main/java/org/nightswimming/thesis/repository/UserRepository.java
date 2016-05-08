package org.nightswimming.thesis.repository;

import org.nightswimming.thesis.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}

