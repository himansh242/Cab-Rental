package com.cab.rental.service.core.resource;

import com.cab.rental.service.core.service.BookCabService;
import com.cab.rental.service.models.GenericResponse;
import com.cab.rental.service.models.book.BookCabResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1/rentIt")
public class BookResource {

    BookCabService bookCabService;

    @Inject
    public BookResource(BookCabService bookCabService) {
        this.bookCabService = bookCabService;
    }

    @GET
    @Path("bookCab/{vehicleType}/{startTime}/{endTime}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GenericResponse<BookCabResponse> bookCab(
            @PathParam("vehicleType") VehicleType vehicleType,
            @PathParam("startTime") String startTime,
            @PathParam("endTime") String endTime) throws Exception {

        return GenericResponse.ok(bookCabService.bookCab(vehicleType, startTime, endTime));
    }
}
