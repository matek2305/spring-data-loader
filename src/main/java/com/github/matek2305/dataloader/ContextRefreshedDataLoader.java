package com.github.matek2305.dataloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.github.matek2305.dataloader.annotations.LoadDataAfter;
import com.github.matek2305.dataloader.exception.DataDependencyCycleFoundException;
import com.github.matek2305.dataloader.scanner.DataLoaderScanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Slf4j
@Component
public class ContextRefreshedDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
    private final DataLoaderScanner dataLoaderScanner;

    private final Set<Class<? extends DataLoader>> loaded = new HashSet<>();

    private Map<Class<? extends DataLoader>, DataLoader> dataLoaderMap = new HashMap<>();

    @Autowired
    public ContextRefreshedDataLoader(
            final AutowireCapableBeanFactory autowireCapableBeanFactory,
            final DataLoaderScanner dataLoaderScanner) {
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
        this.dataLoaderScanner = dataLoaderScanner;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        final String dataLoaderPackage = dataLoaderScanner.getPackage();
        if (StringUtils.isEmpty(dataLoaderPackage)) {
            log.warn("No data loader package specified");
            return;
        }

        log.info("Scanning package '{}' for data loaders ...", dataLoaderPackage);
        dataLoaderMap = dataLoaderScanner.getInstanceMap();
        log.info("Found {} loaders", dataLoaderMap.size());

        log.info("Starting test data load ...");
        dataLoaderMap.forEach((clazz, loader) -> loadData(loader));
        log.info("Test data loaded successfully");
    }

    private void loadData(DataLoader loader) {
        loadData(loader, new HashSet<>());
    }

    private void loadData(DataLoader dataLoader, Set<Class<? extends DataLoader>> dependencyChain) {
        Class<? extends DataLoader> dataLoaderClass = dataLoader.getClass();
        if (loaded.contains(dataLoaderClass)) {
            return;
        }

        if (dataLoaderClass.isAnnotationPresent(LoadDataAfter.class)) {
            dependencyChain.add(dataLoaderClass);

            Class<? extends DataLoader>[] dependencies = dataLoaderClass.getAnnotation(LoadDataAfter.class).value();
            for (Class<? extends DataLoader> clazz : dependencies) {
                if (loaded.contains(clazz)) {
                    continue;
                }

                if (dependencyChain.contains(clazz)) {
                    throw new DataDependencyCycleFoundException("Data dependency cycle found! Check your data loader classes.");
                }

                dependencyChain.add(clazz);
                checkNotNull(dataLoaderMap.get(clazz), "%s instance not found", clazz.getSimpleName());
                loadData(dataLoaderMap.get(clazz), dependencyChain);
            }
        }

        dataLoader.load();
        loaded.add(dataLoaderClass);
    }
}
