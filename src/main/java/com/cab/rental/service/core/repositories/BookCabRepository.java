package com.cab.rental.service.core.repositories;

import com.cab.rental.service.core.storage.TimeSlotTable;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.book.BookingStatus;
import com.cab.rental.service.models.book.VehiclePriceToBranchId;
import com.cab.rental.service.models.vehicle.GetCab;
import com.cab.rental.service.models.vehicle.VehicleType;

import java.util.Date;
import java.util.List;

public interface BookCabRepository {

    BookCabResponse bookCab(VehicleType vehicleType, Long startTime, Long endTime, List<VehiclePriceToBranchId> vehiclePriceToBranchId) throws Exception;

    BookingStatus getBookingStatus(List<TimeSlotTable> timeSlotTableList, Long startTime, Long endTime) throws Exception;
}
