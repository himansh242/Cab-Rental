package com.cab.rental.service.models.vehicle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddVehicleResponse {

    String vehicleId;
    VehicleType vehicleType;
    String BranchName;
    String Message;
}
