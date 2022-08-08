package com.cab.rental.service.models.branch;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BranchResponse {

    String branchId;
    String branchName;
    String message;
}
