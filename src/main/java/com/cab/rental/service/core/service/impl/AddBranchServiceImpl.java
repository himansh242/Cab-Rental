package com.cab.rental.service.core.service.impl;

import com.cab.rental.service.core.repositories.BranchRepository;
import com.cab.rental.service.core.service.AddBranchService;
import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.models.branch.BranchResponse;

import javax.inject.Inject;

public class AddBranchServiceImpl implements AddBranchService {
    private final BranchRepository branchRepository;
    @Inject
    public AddBranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public BranchResponse addBranch(String branchName) throws Exception {

        BranchTable branchTable = branchRepository.createBranch(branchName);

        return BranchResponse.builder()
                .branchId(branchTable.getBranchId())
                .branchName(branchTable.getBranchName())
                .message("BRANCH SUCCESSFULLY ADDED")
                .build();
    }

    @Override
    public BranchResponse getBranch(String branchName) {

        BranchTable branchTable = branchRepository.getBranch(branchName);

        if(branchTable.getBranchId() == null) {
            return BranchResponse.builder()
                    .message("BRANCH NOT FOUND").build();
        }

        return BranchResponse.builder()
                .branchId(branchTable.getBranchId())
                .branchName(branchTable.getBranchName())
                .message("Branch exist")
                .build();
    }
}
