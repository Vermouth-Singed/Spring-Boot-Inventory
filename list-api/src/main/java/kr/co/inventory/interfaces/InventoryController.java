package kr.co.inventory.interfaces;

import kr.co.inventory.application.InventoryService;
import kr.co.inventory.domain.Inventory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/api/inventory")
@RestController
public class InventoryController {

    @Autowired(required = false)
    private InventoryService inventoryService;

    @GetMapping("/")
    public List<Inventory> readInventoryAllList(){
        return inventoryService.readInventoryAllList();
    }

    @GetMapping("/{vin}")
    public List<Inventory> readInventoryList(@PathVariable String vin){
        return inventoryService.readInventoryList(vin);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInventory(@RequestBody @Valid Inventory inventory){
        inventoryService.createInventory(inventory);
    }

    @PutMapping("/{vin}")
    public void updateInventory(@PathVariable String vin, @RequestBody Inventory inventory){
        inventoryService.updateInventory(vin, inventory);
    }

    @DeleteMapping("/{vin}")
    public void deletePerson(@PathVariable String vin) {
        inventoryService.deleteInventory(vin);
    }
}