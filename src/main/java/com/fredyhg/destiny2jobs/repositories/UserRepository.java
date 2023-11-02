package com.fredyhg.destiny2jobs.repositories;

import com.fredyhg.destiny2jobs.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByDiscordName(String discord);

}
