package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);

}
