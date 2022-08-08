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
@Table(name = "cabBranch", indexes = {
        @Index(name = "reference_id_index", columnList = "branchName")
})
@Builder
public class BranchTable {

    @Id
    @LookupKey
    @Column(name = "branchId", nullable = false, unique = true)
    private String branchId;


    @Column(name = "branchName", nullable = false, unique = true)
    private String branchName;

}
