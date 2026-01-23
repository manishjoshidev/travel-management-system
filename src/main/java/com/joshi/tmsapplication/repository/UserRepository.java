
package com.joshi.tmsapplication.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.joshi.tmsapplication.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
