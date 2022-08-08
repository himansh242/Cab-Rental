package com.cab.rental.service.models.price;

import com.cab.rental.service.models.vehicle.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllocatePriceResponse {

    String priceId;

    String branchName;

    VehicleType vehicleType;

    Double price;

    String message;
}
