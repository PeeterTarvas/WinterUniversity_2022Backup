package com.nortal.mega.service;

import com.nortal.mega.persistence.BuildingDboMapper;
import com.nortal.mega.persistence.BuildingRepository;
import com.nortal.mega.persistence.entity.BuildingDbo;
import com.nortal.mega.rest.BuildingDto;
import com.nortal.mega.rest.BuildingDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingDboMapper buildingDboMapper;
    private final BuildingDtoMapper buildingDtoMapper;

    public List<Building> findAll() {
        return buildingRepository.findAll().stream().map(buildingDboMapper::map).collect(Collectors.toList());
    }
    public String generateNewId() {
        List<BuildingDbo> buildings = buildingRepository.findAll();
        return String.valueOf(buildings.get(buildings.size() - 1).getId() + 1);
    }

    public Building findBuildingById(Long id) {
        return buildingDboMapper.map(buildingRepository.findById(id).orElseThrow());
    }

    public ResponseEntity<BuildingDto> updateBuildingById(BuildingDto buildingDto) {
        Building building = buildingDtoMapper.map(buildingDto);
            Long id = building.getId();
            Optional<BuildingDbo> oldBuildingDbo = buildingRepository.findById(id);
            if (oldBuildingDbo.isPresent()) {
                Building oldBuilding = buildingDboMapper.map(oldBuildingDbo.get());
                building.setEnergyUnitMax(oldBuilding.getEnergyUnitMax());
                building.setSectorCode(oldBuilding.getSectorCode());

                if (building.isValid()) {

                    BuildingDbo newBuilding = buildingDboMapper.map(building);
                    buildingRepository.save(newBuilding);
                    return ResponseEntity.ok().build();
                }

        }

        return ResponseEntity.badRequest().build();
    }


    public ResponseEntity<BuildingDto> save(BuildingDto buildingDto) throws IllegalArgumentException {
        Building building = buildingDtoMapper.map(buildingDto);
        if (building.isValid()) {
            buildingRepository.save(buildingDboMapper.map(building));
            return ResponseEntity.ok().build();
        } return ResponseEntity.badRequest().build();
    }
}
