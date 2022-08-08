package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.BookCabRepository;
import com.cab.rental.service.core.service.BookCabService;
import com.cab.rental.service.core.storage.PriceTable;
import com.cab.rental.service.core.storage.VehicleTable;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.book.VehiclePriceToBranchId;
import com.cab.rental.service.models.vehicle.VehicleType;
import io.appform.dropwizard.sharding.dao.LookupDao;
import lombok.AllArgsConstructor;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class BookCabServiceImpl implements BookCabService {

    BookCabRepository bookCabRepository;
    LookupDao<VehicleTable> vehicleTableLookupDao;

    LookupDao<PriceTable> priceTableLookupDao;

    private static final String VEHICLE_ID = "vehicleId";
    private static final String VEHICLE_TYPE = "vehicleType";


    @Override
    public BookCabResponse bookCab(VehicleType vehicleType, String startTime, String endTime) throws Exception {
        synchronized (this) {

            List<VehicleTable> vehicleList = getListOfVehicle(vehicleType);

            List<VehiclePriceToBranchId> vehiclePriceToBranchIdList = getVehiclePriceToBranchIdList(vehicleList);


            return bookCabRepository.bookCab(vehicleType, parseDate(startTime), parseDate(endTime), vehiclePriceToBranchIdList);
        }
    }

    private Long parseDate(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        Date date = simpleDateFormat.parse(time);
        return date.getTime();
    }


    private List<VehicleTable> getListOfVehicle(VehicleType vehicleType) throws Exception {
        DetachedCriteria detachedCriteriaForVehicle = DetachedCriteria.forClass(VehicleTable.class);

        detachedCriteriaForVehicle.add(Restrictions.eq(VEHICLE_TYPE, vehicleType));

        return vehicleTableLookupDao.scatterGather(detachedCriteriaForVehicle);

    }

    private List<VehiclePriceToBranchId> getVehiclePriceToBranchIdList(List<VehicleTable> vehicleList) throws Exception {
        List<VehiclePriceToBranchId> vehiclePriceToBranchIdList = new ArrayList<>();

        for(VehicleTable vehicleTable: vehicleList) {
            Optional<PriceTable> priceTable = priceTableLookupDao.get(vehicleTable.getPriceId());
            priceTable.ifPresent(table -> vehiclePriceToBranchIdList.add(new VehiclePriceToBranchId(table.getPrice(), vehicleTable.getVehicleId(), table.getBranchId())));
        }
        vehiclePriceToBranchIdList.sort(Comparator.comparing(VehiclePriceToBranchId::getPrice));

        return vehiclePriceToBranchIdList;
    }
}
