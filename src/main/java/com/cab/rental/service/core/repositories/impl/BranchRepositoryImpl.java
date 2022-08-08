package com.cab.rental.service.core.repositories.impl;

import com.cab.rental.service.core.repositories.BranchRepository;
import com.cab.rental.service.core.storage.BranchTable;
import com.cab.rental.service.core.storage.DataStore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.appform.dropwizard.sharding.dao.LookupDao;
import lombok.AllArgsConstructor;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;


@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchRepositoryImpl implements BranchRepository {

    private LookupDao<BranchTable> branchTableLookupDao;

    private static final String BRANCH_NAME = "branchName";

    @Override
    public BranchTable createBranch(String branchName) throws Exception {

        BranchTable branchTable = BranchTable.builder()
                .branchId((DataStore.branchId++).toString())
                .branchName(branchName)
                .build();

        branchTableLookupDao.save(branchTable);

        return branchTable;
    }

    @Override
    public BranchTable getBranch(String branchName) {

        DetachedCriteria detachedCriteria =  DetachedCriteria.forClass(BranchTable.class);

        detachedCriteria.add(Restrictions.eq(BRANCH_NAME, branchName));

        List<BranchTable> list = branchTableLookupDao.scatterGather(detachedCriteria);

        BranchTable branchTable = BranchTable.builder().build();
        if(list.isEmpty()) {
            return branchTable;
        }

        BranchTable queriedBranch = list.get(0);
        branchTable.setBranchName(queriedBranch.getBranchName());
        branchTable.setBranchId(queriedBranch.getBranchId());

        return branchTable;
    }
}
