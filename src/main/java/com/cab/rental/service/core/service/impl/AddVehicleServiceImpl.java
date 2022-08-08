package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.VehicleRepository;
import com.cab.rental.service.core.service.AddVehicleService;
import com.cab.rental.service.models.vehicle.AddVehicleResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import javax.inject.Inject;

public class AddVehicleServiceImpl implements AddVehicleService {

    VehicleRepository vehicleRepository;

    @Inject
    public AddVehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public AddVehicleResponse addVehicle(String vehicleId, VehicleType vehicleType, String branchName) throws Exception {

        return vehicleRepository.addVehicle(vehicleId, vehicleType, branchName);
    }
}
