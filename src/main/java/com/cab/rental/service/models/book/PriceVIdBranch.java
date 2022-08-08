package com.cab.rental.service.models.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceVIdBranch {

    private Double price;
    private String vehicleId;
    private String branchId;
}
