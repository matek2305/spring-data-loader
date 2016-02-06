package com.github.matek2305.spring.dataloader.scanner;

import com.github.matek2305.spring.dataloader.DataLoader;

import java.util.Map;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public interface DataLoaderScanner {

    String getPackage();

    Map<Class<? extends DataLoader>, DataLoader> getInstanceMap();
}
