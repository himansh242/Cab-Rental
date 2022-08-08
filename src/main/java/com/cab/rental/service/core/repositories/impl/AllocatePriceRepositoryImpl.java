package com.cab.rental.service.core.repositories.impl;

import com.cab.rental.service.core.repositories.AllocatePriceRepository;
import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.core.storage.PriceTable;
import com.cab.rental.service.core.storage.VehicleTable;
import com.cab.rental.service.models.price.AllocatePriceResponse;
import com.cab.rental.service.models.vehicle.VehicleType;
import com.google.common.base.Joiner;
import io.appform.dropwizard.sharding.dao.LookupDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;


@Data
@AllArgsConstructor(onConstructor_ = @Inject)
public class AllocatePriceRepositoryImpl implements AllocatePriceRepository {

    LookupDao<PriceTable> priceTableLookupDao;
    LookupDao<VehicleTable> vehicleTableLookupDao;

    @Override
    public AllocatePriceResponse allocatePrice(String branchName, VehicleType vehicleType, Double price, BranchTable queriedBranch) throws Exception {

        PriceTable priceTable = PriceTable.builder()
                .priceId(Joiner.on(".").join(queriedBranch.getBranchId(),vehicleType))
                .vehicleType(vehicleType)
                .branchId(queriedBranch.getBranchId())
                .price(price)
                .build();

        priceTableLookupDao.save(priceTable);

        return AllocatePriceResponse.builder()
                .priceId(priceTable.getPriceId())
                .branchName(branchName)
                .vehicleType(priceTable.getVehicleType())
                .price(priceTable.getPrice())
                .message("Price for vehicle type " + priceTable.getVehicleType() + " for branch " + branchName + " has been successfully set.")
                .build();
    }
}
