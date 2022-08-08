package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.BookCabRepository;
import com.cab.rental.service.core.service.BookCabService;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import javax.inject.Inject;
import java.util.Date;

public class BookCabServiceImpl implements BookCabService {

    BookCabRepository bookCabRepository;

    @Inject
    public BookCabServiceImpl(BookCabRepository bookCabRepository) {
        this.bookCabRepository = bookCabRepository;
    }

    @Override
    public BookCabResponse bookCab(VehicleType vehicleType, String startTime, String endTime) throws Exception {
        synchronized (this) {
            return bookCabRepository.bookCab(vehicleType, startTime, endTime);
        }
    }
}
