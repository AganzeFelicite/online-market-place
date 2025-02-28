package com.online_marketplace_api.awesomity.Repository;
import com.online_marketplace_api.awesomity.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    boolean existsByEmail(String email);
}