package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.AllocatePriceRepository;
import com.cab.rental.service.core.service.AllocatePriceService;
import com.cab.rental.service.models.price.AllocatePriceResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import javax.inject.Inject;

public class AllocatePriceServiceImpl implements AllocatePriceService {


    AllocatePriceRepository allocatePriceRepository;

    @Inject
    public AllocatePriceServiceImpl(AllocatePriceRepository allocatePriceRepository) {
        this.allocatePriceRepository = allocatePriceRepository;
    }
    @Override
    public AllocatePriceResponse allocatePrice(String branchName, VehicleType vehicleType, Double price) throws Exception {
        return allocatePriceRepository.allocatePrice(branchName, vehicleType, price);
    }
}
