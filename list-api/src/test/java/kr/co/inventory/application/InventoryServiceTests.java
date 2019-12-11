package kr.co.inventory.application;

import kr.co.inventory.domain.Inventory;
import kr.co.inventory.repositories.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTests {
    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Test
    void readInventoryList() {
        when(inventoryRepository.findById("MN3434"))
                .thenReturn(Optional.of(mockInventory()));

        inventoryRepository.findById("MN3434").ifPresent(inventory -> {
            inventory.setModel(inventory.getModel())
                    .setMake(inventory.getMake())
                    .setYear(inventory.getYear())
                    .setMsrp(inventory.getMsrp())
                    .setStatus(inventory.getStatus())
                    .setBooked(inventory.getBooked())
                    .setListed(inventory.getListed());

            assertThat(inventory.getVin()).isEqualTo("MN3434");
        });
    }

    @Test
    void updateInventory() {
        when(inventoryRepository.findById("MN3434"))
                .thenReturn(Optional.of(mockInventory()));

        inventoryService.updateInventory("MN3434", mockInventory());

        verify(inventoryRepository, times(1)).save(argThat(new IsInventoryWillBeChanged()));
    }

    @Test
    void deleteInventory() {
        when(inventoryRepository.findById("MN3434"))
                .thenReturn(Optional.of(mockInventory()));

        inventoryService.deleteInventory("MN3434");

        verify(inventoryRepository, times(1)).save(argThat(new IsInventoryWillBeChanged()));
    }

    private static class IsInventoryWillBeChanged implements ArgumentMatcher<Inventory> {
        @Override
        public boolean matches(Inventory inventory) {
            return inventory.getVin().equals("MN3434");
        }
    }

    private Inventory mockInventory(){
        return Inventory.of("MN3434","320i","BMW",2013,10000L,"Ordered","Y","Y");
    }
}