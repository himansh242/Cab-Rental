package com.cab.rental.service.core.service;

import com.cab.rental.service.models.branch.BranchResponse;

public interface AddBranchService {
    BranchResponse addBranch(String branchName) throws Exception;

    BranchResponse getBranch(String branchName);

}
