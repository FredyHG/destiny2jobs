package com.fredyhg.destiny2jobs.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "mission_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "mission_name")
    private String missionName;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "price")
    private double price;

}
