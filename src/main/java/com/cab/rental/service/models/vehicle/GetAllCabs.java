package com.cab.rental.service.models.vehicle;

import com.cab.rental.service.models.inventory.Inventory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllCabs {
    List<Inventory> vehicles;
}
