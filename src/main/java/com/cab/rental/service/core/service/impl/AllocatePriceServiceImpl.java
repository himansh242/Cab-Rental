package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.AllocatePriceRepository;
import com.cab.rental.service.core.service.AllocatePriceService;
import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.core.storage.VehicleTable;
import com.cab.rental.service.models.price.AllocatePriceResponse;
import com.cab.rental.service.models.vehicle.VehicleType;
import io.appform.dropwizard.sharding.dao.LookupDao;
import lombok.AllArgsConstructor;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;
@AllArgsConstructor(onConstructor_ = @Inject)
public class AllocatePriceServiceImpl implements AllocatePriceService {


    AllocatePriceRepository allocatePriceRepository;
    LookupDao<BranchTable> branchTableLookupDao;
    LookupDao<VehicleTable> vehicleTableLookupDao;

    private static final String BRANCH_NAME = "branchName";
    private static final String BRANCH_ID = "branchId";
    private static final String VEHICLE_TYPE = "vehicleType";


    @Override
    public AllocatePriceResponse allocatePrice(String branchName, VehicleType vehicleType, Double price) throws Exception {

        DetachedCriteria detachedCriteriaForBranch = DetachedCriteria.forClass(BranchTable.class);
        detachedCriteriaForBranch.add(Restrictions.eq(BRANCH_NAME, branchName));

        List<BranchTable> branchTableList = branchTableLookupDao.scatterGather(detachedCriteriaForBranch);

        if(branchTableList.isEmpty()) {
            return AllocatePriceResponse.builder()
                    .message("No branch present with name " + branchName)
                    .build();
        }

        BranchTable branchTable = branchTableList.get(0);

        DetachedCriteria detachedCriteriaForVehicle = DetachedCriteria.forClass(VehicleTable.class);

        detachedCriteriaForVehicle.add(Restrictions.eq(VEHICLE_TYPE, vehicleType));
        detachedCriteriaForVehicle.add(Restrictions.eq(BRANCH_ID, branchTable.getBranchId()));
        List<VehicleTable> vehicleTableList = vehicleTableLookupDao.scatterGather(detachedCriteriaForVehicle);

        if(vehicleTableList.isEmpty()) {
            return AllocatePriceResponse.builder()
                    .message("No vehicle of type " + vehicleType + " is present in branch " + branchName)
                    .build();
        }

        return allocatePriceRepository.allocatePrice(branchName, vehicleType, price, branchTableList.get(0));
    }
}
