package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.BookCabRepository;
import com.cab.rental.service.core.service.BookCabService;
import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.core.storage.PriceTable;
import com.cab.rental.service.core.storage.TimeSlotTable;
import com.cab.rental.service.core.storage.VehicleTable;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.book.BookingStatus;
import com.cab.rental.service.models.book.VehiclePriceToBranchId;
import com.cab.rental.service.models.inventory.Inventory;
import com.cab.rental.service.models.vehicle.GetAllCabs;
import com.cab.rental.service.models.vehicle.GetCab;
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
    LookupDao<TimeSlotTable> timeSlotTableLookupDao;

    LookupDao<BranchTable> branchTableLookupDao;

    private static final String VEHICLE_ID = "vehicleId";
    private static final String VEHICLE_TYPE = "vehicleType";

    private static final String BRANCH_ID = "branchId";

    @Override
    public BookCabResponse bookCab(VehicleType vehicleType, String startTime, String endTime) throws Exception {
        synchronized (this) {

            List<VehicleTable> vehicleList = getListOfVehicle(vehicleType);

            List<VehiclePriceToBranchId> vehiclePriceToBranchIdList = getVehiclePriceToBranchIdList(vehicleList);


            return bookCabRepository.bookCab(vehicleType, parseDate(startTime), parseDate(endTime), vehiclePriceToBranchIdList);
        }
    }

    @Override
    public GetAllCabs getAllCabs(String startTime, String endTime) {

        List<BranchTable> branchTableList = getAllBranches();

        List<Inventory> inventories = new ArrayList<>();



        branchTableList.forEach(
                branchTable -> {
                    List<VehicleTable> vehicleTableList = getVehicles(branchTable.getBranchId());
                    List<GetCab> getCabs = new ArrayList<>();
                    vehicleTableList.forEach(vehicleTable -> {
                        List<TimeSlotTable> timeSlotTableList = getSlotListForEachVehicle(vehicleTable);
                        try {
                            BookingStatus bookingStatus = bookCabRepository.getBookingStatus(timeSlotTableList, parseDate(startTime), parseDate(endTime));
                            getCabs.add(GetCab.builder()
                                    .vehicleId(vehicleTable.getVehicleId())
                                    .vehicleType(vehicleTable.getVehicleType())
                                    .bookingStatus(bookingStatus)
                                    .build());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    inventories.add(Inventory.builder()
                            .branchName(branchTable.getBranchName())
                            .cabList(getCabs)
                            .build());
                }
        );

        return GetAllCabs.builder()
                .vehicles(inventories)
                .build();
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

    private List<BranchTable> getAllBranches() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BranchTable.class);
        return branchTableLookupDao.scatterGather(detachedCriteria);
    }

    private List<VehicleTable> getVehicles(String branchId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VehicleTable.class);
        detachedCriteria.add(Restrictions.eq(BRANCH_ID, branchId));
        return vehicleTableLookupDao.scatterGather(detachedCriteria);
    }

    private List<TimeSlotTable> getSlotListForEachVehicle(VehicleTable vehicleTable) {
        DetachedCriteria detachedCriteriaForTimeSlotTable = DetachedCriteria.forClass(TimeSlotTable.class);
        detachedCriteriaForTimeSlotTable.add(Restrictions.eq(VEHICLE_ID, vehicleTable.getVehicleId()));
        return timeSlotTableLookupDao.scatterGather(detachedCriteriaForTimeSlotTable);
    }
}
