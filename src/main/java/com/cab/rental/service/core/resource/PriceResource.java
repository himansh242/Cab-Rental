package com.cab.rental.service.core.resource;


import com.cab.rental.service.core.service.AllocatePriceService;
import com.cab.rental.service.models.GenericResponse;
import com.cab.rental.service.models.price.AllocatePriceResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1/rentIt")
public class PriceResource {

    AllocatePriceService allocatePriceService;

    @Inject
    public PriceResource(AllocatePriceService allocatePriceService) {
        this.allocatePriceService = allocatePriceService;
    }

    @POST
    @Path("/allocatePrice/{branchName}/{vehicleType}/{price}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GenericResponse<AllocatePriceResponse> allocatePrice(
            @PathParam("branchName") String branchName,
            @PathParam("vehicleType")VehicleType vehicleType,
            @PathParam("price") Double price
            ) throws Exception {
        return GenericResponse.ok(allocatePriceService.allocatePrice(branchName, vehicleType, price));
    }
}
