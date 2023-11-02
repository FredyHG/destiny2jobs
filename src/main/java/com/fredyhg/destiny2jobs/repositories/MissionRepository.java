package com.fredyhg.destiny2jobs.repositories;

import com.fredyhg.destiny2jobs.models.MissionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MissionRepository extends JpaRepository<MissionModel, UUID> {

    Page<MissionModel> findAll(Pageable pageable);


    Optional<MissionModel> findByMissionName(String missionName);
}
