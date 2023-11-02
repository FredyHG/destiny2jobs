package com.fredyhg.destiny2jobs.repositories;

import com.fredyhg.destiny2jobs.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email);


    Optional<UserModel> findByDiscordName(String discord);
}
