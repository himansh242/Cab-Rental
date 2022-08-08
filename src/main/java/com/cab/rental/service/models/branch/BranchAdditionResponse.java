package com.cab.rental.service.models.branch;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BranchAdditionResponse {

    String id;
    String branchName;

}
