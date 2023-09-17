package com.skypro.springintegrationtesting.repository;

import com.skypro.springintegrationtesting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
