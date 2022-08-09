package com.cab.rental.service.models.inventory;

import com.cab.rental.service.models.vehicle.GetCab;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Inventory {
    String branchName;
    List<GetCab> cabList;
}
