package kr.co.inventory.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inventory.domain.Inventory;
import kr.co.inventory.repositories.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class InventoryControllerTests {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    InventoryRepository inventoryRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    @Test
    void readInventoryList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/inventory/MN3434"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("320i"))
                .andExpect(jsonPath("$.make").value("BMW"))
                .andExpect(jsonPath("$.year").isNumber())
                .andExpect(jsonPath("$.msrp").isNumber())
                .andExpect(jsonPath("$.status").value("Ordered"))
                .andExpect(jsonPath("$.booked").isBoolean())
                .andExpect(jsonPath("$.listed").isBoolean());
    }

    @Test
    void createInventory() throws Exception {
        Inventory dto = Inventory.of("SLF4J88","Unicorn","AUDI",2019,23300L,"Ordered","Y","Y");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/Inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(dto)))
                .andExpect(status().isCreated());

        Optional<Inventory> inventory = inventoryRepository.findById("MN3434");

        if(inventory.isPresent()) {
            Inventory result = inventory.get();

            assertAll(
                    () -> assertThat(result.getModel()).isEqualTo("Unicorn"),
                    () -> assertThat(result.getMake()).isEqualTo("AUDI"),
                    () -> assertThat(result.getYear()).isEqualTo(2019),
                    () -> assertThat(result.getMsrp()).isEqualTo(23300L),
                    () -> assertThat(result.getStatus()).isEqualTo("Ordered"),
                    () -> assertThat(result.getBooked()).isEqualTo(true),
                    () -> assertThat(result.getListed()).isEqualTo(true)
            );
        }
    }

    @Test
    void updateInventory() throws Exception {
        Inventory dto = Inventory.of("MN3434","Unicorn","AUDI",2019,23300L,"Ordered","Y","Y");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/inventory/MN3434")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(dto)))
                .andExpect(status().isOk());

        Optional<Inventory> inventory = inventoryRepository.findById("MN3434");

        if(inventory.isPresent()) {
            Inventory result = inventory.get();

            assertAll(
                    () -> assertThat(result.getModel()).isEqualTo("Unicorn"),
                    () -> assertThat(result.getMake()).isEqualTo("AUDI"),
                    () -> assertThat(result.getYear()).isEqualTo(2019),
                    () -> assertThat(result.getMsrp()).isEqualTo(23300L),
                    () -> assertThat(result.getStatus()).isEqualTo("Ordered"),
                    () -> assertThat(result.getBooked()).isEqualTo(true),
                    () -> assertThat(result.getListed()).isEqualTo(true)
            );
        }
    }

    @Test
    void deleteInventory() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/inventory/MN3434"))
                .andExpect(status().isOk());

        assertTrue(inventoryRepository.findAll().stream().anyMatch(inventory -> inventory.getVin().equals("MN3434")));
    }

    private String toJsonString(Inventory dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}