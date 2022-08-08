package com.cab.rental.service.models.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehiclePriceToBranchId {

    private Double price;
    private String vehicleId;
    private String branchId;
}
