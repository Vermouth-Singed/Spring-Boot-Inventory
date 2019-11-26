package kr.co.inventory.interfaces;

import kr.co.inventory.application.InventoryService;
import kr.co.inventory.domain.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/inventory")
@RestController
public class InventoryController {
//    @Autowired
//    private InventoryService inventoryService;

    @GetMapping("/{vin}")
    public List<Inventory> list(@PathVariable String vin){
        return null;
//        return inventoryService.getInventoryList(vin);
    }
}
