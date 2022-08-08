package com.cab.rental.service.core.service;

import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import java.util.Date;

public interface BookCabService {

    BookCabResponse bookCab(VehicleType vehicleType, String startTime, String endTime) throws Exception;
}
