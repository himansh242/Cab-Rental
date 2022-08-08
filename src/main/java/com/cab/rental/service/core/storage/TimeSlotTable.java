package com.cab.rental.service.core.storage;


import io.appform.dropwizard.sharding.sharding.LookupKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "timeTable")
@Builder
public class TimeSlotTable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String Id;

    @LookupKey
    @Column(name = "vehicleId", nullable = false)
    private String vehicleId;

    @Column(name = "startTime", nullable = false)
    private Long startTime;

    @Column(name = "endTime", nullable = false)
    private Long endTime;
}
