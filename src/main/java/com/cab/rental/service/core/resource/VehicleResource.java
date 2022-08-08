package com.cab.rental.service.core.resource;


import com.cab.rental.service.core.service.AddVehicleService;
import com.cab.rental.service.models.GenericResponse;
import com.cab.rental.service.models.vehicle.AddVehicleResponse;
import com.cab.rental.service.models.vehicle.VehicleType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1/rentIt/vehicle")
public class VehicleResource {

    AddVehicleService addVehicleService;

    @Inject
    public VehicleResource(AddVehicleService addVehicleService) {
        this.addVehicleService = addVehicleService;
    }


    @POST
    @Path("/addVehicle/{vehicleId}/{vehicleType}/{branchName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GenericResponse<AddVehicleResponse> addVehicle(
            @PathParam("vehicleId") String vehicleId,
            @PathParam("vehicleType")VehicleType vehicleType,
            @PathParam("branchName") String branchName) throws Exception {

        return GenericResponse.ok(addVehicleService.addVehicle(vehicleId, vehicleType, branchName));
    }
}
