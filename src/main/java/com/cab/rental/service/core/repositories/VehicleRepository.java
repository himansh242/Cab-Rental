package com.cab.rental.service.core.repositories;

import com.cab.rental.service.models.branch.BranchResponse;
import com.cab.rental.service.models.vehicle.AddVehicleResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

public interface VehicleRepository {

    AddVehicleResponse addVehicle(String vehicleId, VehicleType vehicleType, BranchResponse branchResponse) throws Exception;
}
