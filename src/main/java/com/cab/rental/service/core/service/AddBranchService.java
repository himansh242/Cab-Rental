package com.cab.rental.service.core.service;

import com.cab.rental.service.models.branch.BranchAdditionResponse;
import com.cab.rental.service.models.branch.GetBranchResponse;

public interface AddBranchService {
    BranchAdditionResponse addBranch(String branchName) throws Exception;

    GetBranchResponse getBranch(String branchName);

}
