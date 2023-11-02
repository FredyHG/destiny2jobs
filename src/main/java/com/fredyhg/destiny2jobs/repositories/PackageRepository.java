package com.fredyhg.destiny2jobs.repositories;

import com.fredyhg.destiny2jobs.models.CustomPackageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository  extends JpaRepository<CustomPackageModel, UUID> {
}
