package com.cab.rental.service.models.branch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBranchResponse {

    String branchId;
    String branchName;
    String message;
}
