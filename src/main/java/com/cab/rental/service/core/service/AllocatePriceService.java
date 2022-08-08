package com.cab.rental.service.core.service;

import com.cab.rental.service.models.price.AllocatePriceResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

public interface AllocatePriceService {

    AllocatePriceResponse allocatePrice(String branchName, VehicleType vehicleType, Double price) throws Exception;
}
