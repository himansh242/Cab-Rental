package com.cab.rental.service.core.repositories.impl;

import com.cab.rental.service.core.repositories.VehicleRepository;
import com.cab.rental.service.core.service.AddBranchService;
import com.cab.rental.service.core.storage.VehicleTable;
import com.cab.rental.service.models.branch.GetBranchResponse;
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

    AddBranchService branchService;

    @Override
    public AddVehicleResponse addVehicle(String vehicleId, VehicleType vehicleType, String branchName) throws Exception {
        GetBranchResponse getBranchResponse = branchService.getBranch(branchName);

        AddVehicleResponse addVehicleResponse = AddVehicleResponse.builder().build();
        if(getBranchResponse.getBranchId() == null) {
            addVehicleResponse.setMessage("CORRESPONDING BRANCH NAME IS NOT PRESENT");
            return addVehicleResponse;
        }

        VehicleTable vehicleTable = VehicleTable.builder()
                .vehicleId(vehicleId)
                .vehicleType(vehicleType)
                .branchId(getBranchResponse.getBranchId())
                .build();

        vehicleTableLookupDao.save(vehicleTable);

        addVehicleResponse.setVehicleId(vehicleTable.getVehicleId());
        addVehicleResponse.setMessage("VEHICLE ADDED");
        addVehicleResponse.setVehicleType(vehicleTable.getVehicleType());
        addVehicleResponse.setBranchName(branchName);
        return  addVehicleResponse;
    }
}
