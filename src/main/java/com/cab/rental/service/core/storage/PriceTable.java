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
@Table(name = "cabPrice")
@Builder
public class PriceTable {


    @Id
    @LookupKey
    @Column(name = "priceId", nullable = false, unique = true)
    private String priceId;

    @Column(name = "branchId", nullable = false)
    private String branchId;

    @Column(name = "vehicleType", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "price", nullable = false)
    private Double price;

    public String getPriceId() {
        return Joiner.on(".").join(branchId, vehicleType);
    }
}
