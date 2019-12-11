package kr.co.inventory.application;

import kr.co.inventory.domain.Inventory;
import kr.co.inventory.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InventoryService {
    private InventoryRepository inventoryRepository;

    @Autowired
    InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> readInventoryAllList() {
        return inventoryRepository.findAll();
    }

    public List<Inventory> readInventoryList(String vin) {
        return inventoryRepository.findAllById(Collections.singleton(vin));
    }

    public void createInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public void updateInventory(String vin, Inventory item) {
        inventoryRepository.findById(vin).ifPresent(inventory -> {
            inventory.setModel(item.getModel())
                     .setMake(item.getMake())
                     .setYear(item.getYear())
                     .setMsrp(item.getMsrp())
                     .setStatus(item.getStatus())
                     .setBooked(item.getBooked())
                     .setListed(item.getListed());

            inventoryRepository.save(inventory);
        });
    }

    public void deleteInventory(String vin) {
        inventoryRepository.deleteById(vin);
    }
}