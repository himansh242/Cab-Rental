package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.BranchRepository;
import com.cab.rental.service.core.service.AddBranchService;
import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.models.branch.BranchAdditionResponse;
import com.cab.rental.service.models.branch.GetBranchResponse;

import javax.inject.Inject;

public class AddBranchServiceImpl implements AddBranchService {


    private final BranchRepository branchRepository;

    @Inject
    public AddBranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public BranchAdditionResponse addBranch(String branchName) throws Exception {

        BranchTable branchTable = branchRepository.createBranch(branchName);

        return BranchAdditionResponse.builder()
                .id(branchTable.getBranchId())
                .branchName(branchTable.getBranchName())
                .build();
    }

    @Override
    public GetBranchResponse getBranch(String branchName) {

        BranchTable branchTable = branchRepository.getBranch(branchName);

        if(branchTable.getBranchId()== null) {
            return GetBranchResponse.builder()
                    .message("BRANCH NOT FOUND").build();
        }

        return GetBranchResponse.builder()
                .branchId(branchTable.getBranchId())
                .branchName(branchTable.getBranchName())
                .message("Branch exist")
                .build();
    }
}
