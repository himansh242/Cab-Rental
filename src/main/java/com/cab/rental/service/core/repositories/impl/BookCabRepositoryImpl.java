package com.cab.rental.service.core.repositories.impl;

import com.cab.rental.service.core.commands.RepositoryAccessCommands;
import com.cab.rental.service.core.repositories.BookCabRepository;
import com.cab.rental.service.core.storage.*;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.book.PriceVIdBranch;
import com.cab.rental.service.models.vehicle.VehicleType;
import io.appform.dropwizard.sharding.dao.LookupDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public BookCabResponse bookCab(VehicleType vehicleType, String startTime, String endTime) throws Exception {


        //Vehicle Table
        DetachedCriteria detachedCriteriaForVehicle = DetachedCriteria.forClass(VehicleTable.class);

        detachedCriteriaForVehicle.add(Restrictions.eq(VEHICLE_TYPE, vehicleType));

        List<VehicleTable> vehicleTableList = vehicleTableLookupDao.scatterGather(detachedCriteriaForVehicle);

        List<PriceVIdBranch> vehicleIdAndPrice = new ArrayList<>();

        /*
        for a particular VehicleId, get it's price.
        */
        for(VehicleTable vehicleTable: vehicleTableList) {
            Optional<PriceTable> priceTable = priceTableLookupDao.get(vehicleTable.getPriceId());
            priceTable.ifPresent(table -> vehicleIdAndPrice.add(new PriceVIdBranch(table.getPrice(), vehicleTable.getVehicleId(), table.getBranchId())));
        }

        vehicleIdAndPrice.sort(Comparator.comparing(PriceVIdBranch::getPrice));


        for(PriceVIdBranch IdPriceBranch: vehicleIdAndPrice) {
            var vehicleId = IdPriceBranch.getVehicleId();
            var branchId = IdPriceBranch.getBranchId();

            DetachedCriteria detachedCriteriaForTimeSlot = DetachedCriteria.forClass(TimeSlotTable.class);
            detachedCriteriaForTimeSlot.add(Restrictions.eq(VEHICLE_ID, vehicleId));
            List<TimeSlotTable> timeSlotTableList = timeTableLookupDao.scatterGather(detachedCriteriaForTimeSlot);
            if(!timeSlotTableList.isEmpty()){
                BookCabResponse bookCabResponse = checkIfEntryCanBeMadeAndReturn(timeSlotTableList, vehicleType, branchId, parseDate(startTime), parseDate(endTime));
                if(bookCabResponse.getVehicleId()!= null) return bookCabResponse;
            }
             else return enterFreshEntryAndReturn(vehicleId, branchId, parseDate(startTime), parseDate(endTime));

        }

        //If no cab is available
        return BookCabResponse.builder()
                .message("NO CAB AVAILABLE")
                .build();
    }

    private Long parseDate(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        Date date = simpleDateFormat.parse(time);
        return date.getTime();
    }

    private BookCabResponse enterFreshEntryAndReturn(String vehicleId, String branchId, Long startTime, Long endTime) throws Exception {

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

    private BookCabResponse checkIfEntryCanBeMadeAndReturn(List<TimeSlotTable> timeTableList, VehicleType vehicleType, String branchId, Long startTime, Long endTime) throws Exception {

        var vehicleId = timeTableList.get(0).getVehicleId();
        var branchTable = branchTableLookupDao.get(branchId);
        var branchName = "";
        if(branchTable.isPresent()) branchName = branchTable.get().getBranchName();

//        DetachedCriteria detachedCriteriaForTime = DetachedCriteria.forClass(TimeSlotTable.class);
//        detachedCriteriaForTime.add(Restrictions.eq(VEHICLE_ID, vehicleId));
//
//        List<TimeSlotTable> timeTableList = timeTableLookupDao.scatterGather(detachedCriteriaForTime);

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
