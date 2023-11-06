package com.fredyhg.destiny2jobs.repositories;

import com.fredyhg.destiny2jobs.models.CustomPackageModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomPackageRepository extends JpaRepository<CustomPackageModel, UUID> {

    @Query(value = "SELECT e FROM CustomPackageModel e WHERE e.status = 'WAIT_FOR_WORKER'")
    List<CustomPackageModel> findAllWhereStatusWaitForWorker();

    @Query(value = "SELECT e FROM CustomPackageModel e WHERE e.status = 'STARTED'")
    List<CustomPackageModel> findAllWhereStatusStarted();

    @Query(value = "SELECT e FROM CustomPackageModel e WHERE e.status = 'FINISH'")
    List<CustomPackageModel> findAllWhereStatusFinish();

    @Query(value = "SELECT e FROM CustomPackageModel e WHERE e.user = :user AND e.status = 'WAIT_FOR_WORKER'")
    Optional<CustomPackageModel> findPendingPackage(UserModel user);

}
