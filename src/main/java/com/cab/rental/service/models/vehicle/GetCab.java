package com.cab.rental.service.models.vehicle;

import com.cab.rental.service.models.book.BookingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCab {

    private VehicleType vehicleType;
    private String vehicleId;
    private BookingStatus bookingStatus;
}
