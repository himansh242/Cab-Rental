package com.cab.rental.service.core.repositories;

import com.cab.rental.service.core.storage.BranchTable;

public interface BranchRepository {

    BranchTable createBranch(String branchName) throws Exception;

    BranchTable getBranch(String branchName);
}
