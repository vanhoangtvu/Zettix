package net.zettix.accountcenter.repository;

import net.zettix.accountcenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);
    boolean existsByEmail (String email);
    Optional<User> findByUsername(String username);
}
