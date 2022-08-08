package com.cab.rental.service.core.guiceModule;

import com.cab.rental.service.core.repositories.BookCabRepository;
import com.cab.rental.service.core.repositories.BranchRepository;
import com.cab.rental.service.core.repositories.VehicleRepository;
import com.cab.rental.service.core.repositories.AllocatePriceRepository;
import com.cab.rental.service.core.repositories.impl.AllocatePriceRepositoryImpl;
import com.cab.rental.service.core.repositories.impl.BookCabRepositoryImpl;
import com.cab.rental.service.core.repositories.impl.BranchRepositoryImpl;
import com.cab.rental.service.core.repositories.impl.VehicleRepositoryImpl;
import com.cab.rental.service.core.service.AddBranchService;
import com.cab.rental.service.core.service.AddVehicleService;
import com.cab.rental.service.core.service.AllocatePriceService;
import com.cab.rental.service.core.service.BookCabService;
import com.cab.rental.service.core.service.impl.AddBranchServiceImpl;
import com.cab.rental.service.core.service.impl.AddVehicleServiceImpl;
import com.cab.rental.service.core.service.impl.AllocatePriceServiceImpl;
import com.cab.rental.service.core.service.impl.BookCabServiceImpl;
import com.cab.rental.service.core.storage.*;
import com.cab.rental.service.server.CabConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import io.appform.dropwizard.sharding.DBShardingBundle;
import io.appform.dropwizard.sharding.dao.LookupDao;
import io.appform.dropwizard.sharding.dao.RelationalDao;

public class CabModule extends AbstractModule {

    private final DBShardingBundle<CabConfiguration> dbShardingBundle;

    public CabModule(DBShardingBundle<CabConfiguration> dbShardingBundle) {
        this.dbShardingBundle = dbShardingBundle;
    }

    @Override
    public void configure() {
        bind(AddBranchService.class).to(AddBranchServiceImpl.class).in(Scopes.SINGLETON);
        bind(BranchRepository.class).to(BranchRepositoryImpl.class).in(Scopes.SINGLETON);
        bind(AddVehicleService.class).to(AddVehicleServiceImpl.class).in(Scopes.SINGLETON);
        bind(VehicleRepository.class).to(VehicleRepositoryImpl.class).in(Scopes.SINGLETON);
        bind(AllocatePriceRepository.class).to(AllocatePriceRepositoryImpl.class).in(Scopes.SINGLETON);
        bind(AllocatePriceService.class).to(AllocatePriceServiceImpl.class).in(Scopes.SINGLETON);
        bind(BookCabRepository.class).to(BookCabRepositoryImpl.class).in(Scopes.SINGLETON);
        bind(BookCabService.class).to(BookCabServiceImpl.class).in(Scopes.SINGLETON);
    }


    @Provides
    public LookupDao<BranchTable> providesBranchDao() {
        return dbShardingBundle.createParentObjectDao(BranchTable.class);
    }

    @Provides
    public LookupDao<VehicleTable> providesVehicleDao() {
        return dbShardingBundle.createParentObjectDao(VehicleTable.class);
    }

    @Provides
    public LookupDao<PriceTable> providesPriceDao() {
        return dbShardingBundle.createParentObjectDao(PriceTable.class);
    }


//    @Provides
//    public RelationalDao<TimeSlotTable> providesStartTime() {
//        return dbShardingBundle.createRelatedObjectDao(TimeSlotTable.class);
//    }

    @Provides
    public LookupDao<TimeSlotTable> providesStartTime() {
        return dbShardingBundle.createParentObjectDao(TimeSlotTable.class);
    }

}
