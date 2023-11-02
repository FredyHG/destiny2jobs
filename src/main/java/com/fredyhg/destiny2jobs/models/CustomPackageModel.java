package com.fredyhg.destiny2jobs.models;

import com.fredyhg.destiny2jobs.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "package_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomPackageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany
    @JoinTable(
            name = "package_mission",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "mission_id")
    )
    private List<MissionModel> missions;

    @Column(name = "estimated_time")
    private int estimatedTime;

    @Column(name = "final_price")
    private double price;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private UserModel worker;

    @Enumerated(EnumType.STRING)
    public ServiceStatus status;

}
