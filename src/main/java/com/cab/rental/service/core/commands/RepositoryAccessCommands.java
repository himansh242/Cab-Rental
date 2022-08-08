package com.cab.rental.service.core.commands;

import io.appform.dropwizard.sharding.dao.LookupDao;
import io.appform.dropwizard.sharding.dao.LookupDao.LockedContext;
import io.appform.dropwizard.sharding.dao.RelationalDao;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RepositoryAccessCommands<T> {

    @Getter
    LookupDao<T> relationalDao;

    public RepositoryAccessCommands(LookupDao<T> relationalDao) {
        this.relationalDao = relationalDao;
    }


//    public List<T> select(
//            final String shardingKey,
//            final DetachedCriteria detachedCriteria,
//            final int start,
//            final int count) {
//        try {
//            return relationalDao.select(shardingKey, detachedCriteria, start, count);
//        } catch (Exception e) {
//            log.error("Error while selecting from db", e);
//        }
//        return null;
//    }
}
