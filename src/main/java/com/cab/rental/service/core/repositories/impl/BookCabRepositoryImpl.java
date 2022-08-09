package com.cab.rental.service.core.repositories.impl;

import com.cab.rental.service.core.commands.RepositoryAccessCommands;
import com.cab.rental.service.core.repositories.BookCabRepository;
import com.cab.rental.service.core.storage.*;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.book.BookingStatus;
import com.cab.rental.service.models.book.VehiclePriceToBranchId;
import com.cab.rental.service.models.vehicle.VehicleType;
import io.appform.dropwizard.sharding.dao.LookupDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;

@Singleton
public class BookCabRepositoryImpl implements BookCabRepository {

    LookupDao<VehicleTable> vehicleTableLookupDao;
    LookupDao<PriceTable> priceTableLookupDao;
    LookupDao<BranchTable> branchTableLookupDao;
    LookupDao<TimeSlotTable> timeTableLookupDao;

    RepositoryAccessCommands<TimeSlotTable> accessors;

    @Inject
    public BookCabRepositoryImpl(
            LookupDao<VehicleTable> vehicleTableLookupDao,
            LookupDao<PriceTable> priceTableLookupDao,
            LookupDao<BranchTable> branchTableLookupDao,
            LookupDao<TimeSlotTable> timeTableLookupDao
    ) {
        this.vehicleTableLookupDao = vehicleTableLookupDao;
        this.priceTableLookupDao = priceTableLookupDao;
        this.branchTableLookupDao = branchTableLookupDao;
        this.timeTableLookupDao = timeTableLookupDao;
        accessors = new RepositoryAccessCommands<TimeSlotTable>(timeTableLookupDao);
    }

    private static final String VEHICLE_TYPE = "vehicleType";

    private static final String PRICE_ID = "priceId";
    private static final String VEHICLE_ID = "vehicleId";
    @Override
    public BookCabResponse bookCab(VehicleType vehicleType, Long startTime, Long endTime, List<VehiclePriceToBranchId> vehicleIdAndPrice) throws Exception {

        for(VehiclePriceToBranchId IdPriceBranch: vehicleIdAndPrice) {
            var vehicleId = IdPriceBranch.getVehicleId();
            var branchId = IdPriceBranch.getBranchId();

            DetachedCriteria detachedCriteriaForTimeSlot = DetachedCriteria.forClass(TimeSlotTable.class);
            detachedCriteriaForTimeSlot.add(Restrictions.eq(VEHICLE_ID, vehicleId));
            List<TimeSlotTable> timeSlotTableList = timeTableLookupDao.scatterGather(detachedCriteriaForTimeSlot);
            if(!timeSlotTableList.isEmpty()){
                BookCabResponse bookCabResponse = checkIfEntryCanBeMadeAndReturn(timeSlotTableList, vehicleType, branchId, startTime, endTime);
                if(bookCabResponse.getVehicleId()!= null) return bookCabResponse;
            }
             else return enterFreshEntryAndReturn(vehicleId, branchId, startTime, endTime);

        }

        //If no cab is available
        return BookCabResponse.builder()
                .message("NO CAB AVAILABLE")
                .build();
    }

    @Override
    public BookingStatus getBookingStatus(List<TimeSlotTable> timeSlotTableList, Long startTime, Long endTime) throws Exception {
        var index = 0;

        if (index == timeSlotTableList.size()) return BookingStatus.AVAILABLE;

        while (index < timeSlotTableList.size() && timeSlotTableList.get(index).getStartTime() <= startTime){
            index++;
        }

        if(index == timeSlotTableList.size()) {
            if(startTime >= timeSlotTableList.get(index-1).getEndTime()) {
                return BookingStatus.AVAILABLE;
            }
            else return BookingStatus.BOOKED;
        }

        if(index == 0) {
            if(endTime <= timeSlotTableList.get(0).getStartTime()) {
                return BookingStatus.AVAILABLE;
            } else return BookingStatus.BOOKED;

        }
        if(startTime >= timeSlotTableList.get(index-1).getEndTime() && endTime <= timeSlotTableList.get(index).getEndTime()) {
            return BookingStatus.AVAILABLE;
        } else return BookingStatus.BOOKED;
    }

    BookCabResponse enterFreshEntryAndReturn(String vehicleId, String branchId, Long startTime, Long endTime) throws Exception {

        TimeSlotTable timeSlotTable = TimeSlotTable.builder()
                .Id((DataStore.slotId++).toString())
                .vehicleId(vehicleId)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        timeTableLookupDao.save(timeSlotTable);

        var branchTable = branchTableLookupDao.get(branchId);
        String branchName = "";
        if(branchTable.isPresent()) {
            branchName = branchTable.get().getBranchName();
        }
        return BookCabResponse.builder()
                .vehicleId(timeSlotTable.getVehicleId())
                .startTime(startTime)
                .endTime(endTime)
                .branchName(branchName)
                .message("Vehicle with Id "
                        + timeSlotTable.getVehicleId()
                        + " has been booked from "
                        + branchName)
                .build();
    }

    BookCabResponse checkIfEntryCanBeMadeAndReturn(List<TimeSlotTable> timeTableList, VehicleType vehicleType, String branchId, Long startTime, Long endTime) throws Exception {

        var vehicleId = timeTableList.get(0).getVehicleId();
        var branchTable = branchTableLookupDao.get(branchId);
        var branchName = "";
        if(branchTable.isPresent()) branchName = branchTable.get().getBranchName();

        timeTableList.sort((Comparator.comparing(TimeSlotTable::getStartTime)));

        var index = 0;
        while(index < timeTableList.size() && timeTableList.get(index).getStartTime() <= startTime) {
            index++;
        }
        if(index == timeTableList.size()) {
            if(timeTableList.get(index-1).getEndTime() <= startTime) {
                return getSuccessResponse(timeTableList, vehicleId, startTime, endTime, branchName);
            }
            return getFailureResponse(vehicleType);
        }

        if(index == 0 ) {
            if(timeTableList.get(index).getStartTime()>= endTime) {
                return getSuccessResponse(timeTableList, vehicleId, startTime, endTime, branchName);
            } else return getFailureResponse(vehicleType);
        }

         if(startTime >= timeTableList.get(index -1).getEndTime() && endTime <= timeTableList.get(index).getStartTime()) {
             return getSuccessResponse(timeTableList, vehicleId, startTime, endTime, branchName);
        }
         else return getFailureResponse(vehicleType);
    }


    private BookCabResponse getSuccessResponse(List<TimeSlotTable> timeTableList, String vehicleId, Long startTime, Long endTime, String branchName) throws Exception {
        timeTableList.add(
                TimeSlotTable.builder()
                        .Id((DataStore.slotId++).toString())
                        .vehicleId(vehicleId)
                        .startTime(startTime)
                        .endTime(endTime)
                        .build());

        for(TimeSlotTable val : timeTableList) {
            timeTableLookupDao.save(val);
        }

        return BookCabResponse.builder()
                .branchName(branchName)
                .vehicleId(vehicleId)
                .startTime(startTime)
                .endTime(endTime)
                .message("Vehicle with Id " + vehicleId + " has been booked from " + branchName + " branch")
                .build();
    }

    private BookCabResponse getFailureResponse(VehicleType vehicleType) {
        return BookCabResponse.builder()
                .message("No cabs of type " + vehicleType + " are available for given time")
                .build();
    }
}
