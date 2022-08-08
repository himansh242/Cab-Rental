package com.cab.rental.service.models.book;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BookCabResponse {

    private String vehicleId;
    private String branchName;
    private Long startTime;
    private Long endTime;
    private String message;
}
