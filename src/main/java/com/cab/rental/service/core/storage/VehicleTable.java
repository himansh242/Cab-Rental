package com.cab.rental.service.core.storage;

import com.cab.rental.service.models.vehicle.VehicleType;
import com.google.common.base.Joiner;
import io.appform.dropwizard.sharding.sharding.LookupKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cabVehicle")
@Builder
public class VehicleTable {

    @Id
    @LookupKey
    @Column(name = "vehicleId", nullable = false, unique = true)
    private String vehicleId;

    @Column(name = "vehicleType", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "branchId", nullable = false)
    private String branchId;

    public String getPriceId() {
        return Joiner.on(".").join(branchId, vehicleType);
    }
}
