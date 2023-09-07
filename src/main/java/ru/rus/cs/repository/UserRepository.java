package ru.rus.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rus.cs.db.model.UserTable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Long> {
    Optional<UserTable> findByUsername(String username);
}
