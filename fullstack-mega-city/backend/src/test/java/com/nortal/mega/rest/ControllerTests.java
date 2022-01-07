package com.nortal.mega.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nortal.mega.service.BuildingService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTests {


    private final BuildingController buildingController;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BuildingService buildingService;

    @Autowired
    public ControllerTests(BuildingController buildingController,
                           MockMvc mockMvc, ObjectMapper objectMapper, BuildingService buildingService) {
        this.buildingController = buildingController;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.buildingService = buildingService;
    }

    @Test
    @Order(1)
    void testAddingNewBuilding() throws Exception {

        BuildingDto buildingDto = new BuildingDto();
        buildingDto
                .setId(Long.parseLong(buildingService.generateNewId())) // already 2 buildings so new id is 3
                .setAddress("AA")
                .setEnergyUnitMax(10)
                .setSectorCode("1")
                .setEnergyUnits(5)
                .setIndex("NO11")
                .setName("Test");

        String json = objectMapper.writeValueAsString(buildingDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("http://localhost:8080/api/v1/mega/building/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).characterEncoding("UTF-8")).andExpect(result -> ResponseEntity.status(200))
                .andReturn();

        ResponseEntity<BuildingDto> dtoResponseEntity = buildingController.getBuildingById(3L);
        BuildingDto dto = dtoResponseEntity.getBody();
        assert dto != null;
        assertEquals("Test", dto.getName());

    }

    @Test
    @Order(2)
    void testGettingBuilding() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/api/v1/mega/building/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")).andExpect
                        (status().isOk())
                .andReturn();

        ResponseEntity<BuildingDto> dtoResponseEntity = buildingController.getBuildingById(1L);
        BuildingDto dto = dtoResponseEntity.getBody();

        System.out.println(buildingController.getAll());


        assert dto != null;
        assertEquals("Big Building", dto.getName()); //test if names match
    }

    @Test
    @Order(3)
    void testUpdatingBuilding() throws Exception {

        BuildingDto buildingDto = new BuildingDto();
        buildingDto
                .setId(1L)
                .setAddress("AA")
                .setEnergyUnitMax(10)
                .setSectorCode("111")
                .setEnergyUnits(6)
                .setIndex("NO11")
                .setName("Test");

        String json = objectMapper.writeValueAsString(buildingDto);


        mockMvc.perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/api/v1/mega/building")
                        .contentType(MediaType.APPLICATION_JSON).content(json).characterEncoding("UTF-8")).andExpect
                        (status().isOk())
                .andReturn();

        ResponseEntity<BuildingDto> dtoResponseEntity = buildingController.getBuildingById(1L);
        BuildingDto dto = dtoResponseEntity.getBody();
        assert dto != null;
        assertEquals("Test", dto.getName()); //test if names match
    }

    @Test
    @Order(4)
    void testGettingAllBuildings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/api/v1/mega/building")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")).andExpect
                        (status().isOk())
                .andReturn();

        ResponseEntity<List<BuildingDto>> dtoResponseEntity = buildingController.getAll();
        List<BuildingDto> dto = dtoResponseEntity.getBody();
        assert dto != null;
        assertEquals(3, dto.size()); // since a new building was added the size of the list should be 3
    }



}
