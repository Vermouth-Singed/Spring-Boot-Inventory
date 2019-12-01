package kr.co.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity
@Builder
@Accessors(chain = true)
public class Inventory {
    @Id
    @GeneratedValue
    private String vin;
    private String model;
    private String make;
    private Integer year;
    private Long msrp;
    private String status;
    private boolean booked;
    private boolean listed;
}
