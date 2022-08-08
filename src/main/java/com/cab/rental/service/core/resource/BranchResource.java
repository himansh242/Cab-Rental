package com.cab.rental.service.core.resource;

import com.cab.rental.service.models.GenericResponse;
import com.cab.rental.service.models.branch.BranchResponse;
import com.cab.rental.service.core.service.AddBranchService;
import com.cab.rental.service.core.service.impl.AddBranchServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1/rentIt/branch")
public class BranchResource {

    AddBranchService addBranchService;

    @Inject
    public BranchResource(AddBranchServiceImpl addBranchService) {
        this.addBranchService = addBranchService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addBranch/{branchName}")
    public GenericResponse<BranchResponse> addBranch(@PathParam("branchName") String branchName) throws Exception {
        return GenericResponse.ok(addBranchService.addBranch(branchName));
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getBranch/{branchName}")
    public GenericResponse<BranchResponse> getBranch(@PathParam("branchName") String branchName) {
        return GenericResponse.ok(addBranchService.getBranch(branchName));
    }
}
