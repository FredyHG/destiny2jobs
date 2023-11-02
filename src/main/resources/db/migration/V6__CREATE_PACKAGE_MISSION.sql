CREATE TABLE package_mission (
        package_id UUID,
        mission_id UUID,
        PRIMARY KEY (package_id, mission_id),
        FOREIGN KEY (package_id) REFERENCES package_tb(id),
        FOREIGN KEY (mission_id) REFERENCES mission_tb(id)
);