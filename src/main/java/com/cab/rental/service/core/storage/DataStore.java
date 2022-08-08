package com.cab.rental.service.core.storage;

import lombok.Data;

import javax.inject.Singleton;
import java.util.Map;

@Data
@Singleton
public class DataStore {

    public static Integer branchId = 1;

    public static Map<String, Integer> startTimeId;

    public static Map<String, Integer> entTimeId;

    public static Long slotId = 1L;
}
