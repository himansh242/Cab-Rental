package com.cab.rental.service.core.repositories.impl;

import com.cab.rental.service.core.repositories.VehicleRepository;
import com.cab.rental.service.core.storage.VehicleTable;
import com.cab.rental.service.models.branch.BranchResponse;
import com.cab.rental.service.models.vehicle.AddVehicleResponse;
import com.cab.rental.service.models.vehicle.VehicleType;
import io.appform.dropwizard.sharding.dao.LookupDao;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class VehicleRepositoryImpl implements VehicleRepository {


    LookupDao<VehicleTable> vehicleTableLookupDao;


    @Override
    public AddVehicleResponse addVehicle(String vehicleId, VehicleType vehicleType, BranchResponse branchResponse) throws Exception {

        VehicleTable vehicleTable = VehicleTable.builder()
                .vehicleId(vehicleId)
                .vehicleType(vehicleType)
                .branchId(branchResponse.getBranchId())
                .build();

        vehicleTableLookupDao.save(vehicleTable);

        return AddVehicleResponse.builder()
                .vehicleId(vehicleTable.getVehicleId())
                .vehicleType(vehicleTable.getVehicleType())
                .branchName(branchResponse.getBranchName())
                .message("VEHICLE SUCCESSFULLY ADDED")
                .build();
    }
}
