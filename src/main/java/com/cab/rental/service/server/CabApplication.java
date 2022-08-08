package com.cab.rental.service.server;

import com.cab.rental.service.core.guiceModule.CabModule;
import com.cab.rental.service.core.storage.*;
import com.google.inject.Stage;
import io.appform.dropwizard.sharding.config.ShardedHibernateFactory;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.appform.dropwizard.sharding.DBShardingBundle;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class CabApplication extends Application<CabConfiguration> {

    private final static String APP_NAME = "RENT_IT";

    public static void main(String[] args) throws Exception {
        new CabApplication().run(args);
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public void initialize(Bootstrap<CabConfiguration> bootstrap) {
        var dbShardingBundle = new  DBShardingBundle<CabConfiguration>(BranchTable.class,
                VehicleTable.class,
                PriceTable.class,
                TimeSlotTable.class) {
            @Override
            protected ShardedHibernateFactory getConfig(CabConfiguration configuration) {
                return configuration.getSharded();
            }
        };

        bootstrap.addBundle(dbShardingBundle);

        var guiceBundle = GuiceBundle.builder()
                .enableAutoConfig("com.cab.rental.service")
                .modules(new CabModule(dbShardingBundle))
                .printDiagnosticInfo()
                .build(Stage.PRODUCTION);

        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(CabConfiguration cabConfiguration, Environment environment) {
    }
}
