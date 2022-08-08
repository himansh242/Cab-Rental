package com.cab.rental.service.core.repositories;

import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.models.price.AllocatePriceResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

public interface AllocatePriceRepository {

    AllocatePriceResponse allocatePrice(String branchName, VehicleType vehicleType, Double price, BranchTable branchTable) throws Exception;
}
