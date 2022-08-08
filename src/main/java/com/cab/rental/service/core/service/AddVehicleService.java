package com.cab.rental.service.core.service;

import com.cab.rental.service.models.vehicle.AddVehicleResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

public interface AddVehicleService {
    AddVehicleResponse addVehicle(String vehicleId, VehicleType vehicleType, String branchName) throws Exception;
}
