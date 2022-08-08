package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.VehicleRepository;
import com.cab.rental.service.core.service.AddBranchService;
import com.cab.rental.service.core.service.AddVehicleService;
import com.cab.rental.service.models.branch.BranchResponse;
import com.cab.rental.service.models.vehicle.AddVehicleResponse;
import com.cab.rental.service.models.vehicle.VehicleType;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class AddVehicleServiceImpl implements AddVehicleService {

    VehicleRepository vehicleRepository;

    AddBranchService branchService;

    @Override
    public AddVehicleResponse addVehicle(String vehicleId, VehicleType vehicleType, String branchName) throws Exception {

        BranchResponse branchResponse = branchService.getBranch(branchName);

        if(branchResponse.getBranchId() == null) {
           return AddVehicleResponse.builder()
                    .message("CORRESPONDING BRANCH NAME IS NOT PRESENT")
                    .build();
        }

        return vehicleRepository.addVehicle(vehicleId, vehicleType, branchResponse);
    }
}
