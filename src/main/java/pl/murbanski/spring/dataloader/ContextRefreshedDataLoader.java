package pl.murbanski.spring.dataloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.murbanski.spring.dataloader.annotations.LoadDataAfter;
import pl.murbanski.spring.dataloader.scanner.DataLoaderScanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Slf4j
@Component
public class ContextRefreshedDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
    private final DataLoaderScanner dataLoaderScanner;
    private final DataLoaderFactory dataLoaderFactory;

    private Map<Class<? extends DataLoader>, DataLoader> dataLoaderMap = new HashMap<>();
    private Set<Class<? extends DataLoader>> loaded = new HashSet<>();

    @Autowired
    public ContextRefreshedDataLoader(
            final AutowireCapableBeanFactory autowireCapableBeanFactory,
            final DataLoaderScanner dataLoaderScanner,
            final DataLoaderFactory dataLoaderFactory) {
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
        this.dataLoaderScanner = dataLoaderScanner;
        this.dataLoaderFactory = dataLoaderFactory;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        final String dataLoaderPackage = dataLoaderScanner.getPackage();
        if (StringUtils.isEmpty(dataLoaderPackage)) {
            log.warn("No data loader package specified");
            return;
        }

        log.info("Scanning package '{}' for data loaders ...", dataLoaderPackage);
        final Set<Class<? extends DataLoader>> foundDataLoaders = dataLoaderScanner.scan();
        log.info("Found {} loaders", foundDataLoaders.size());

        log.info("Starting test data load ...");

        dataLoaderMap = foundDataLoaders.stream().collect(Collectors.toMap(loader -> loader, dataLoaderFactory::make));
        dataLoaderMap.values().forEach(this::loadData);

        log.info("Test data loaded successfully");
    }

    private void loadData(DataLoader dataLoader) {
        Class<? extends DataLoader> dataLoaderClass = checkNotNull(dataLoader).getClass();
        if (loaded.contains(dataLoaderClass)) {
            return;
        }

        if (dataLoaderClass.isAnnotationPresent(LoadDataAfter.class)) {
            Class<? extends DataLoader>[] dependencies = dataLoaderClass.getAnnotation(LoadDataAfter.class).value();
            for (Class<? extends DataLoader> clazz : dependencies) {
                if (loaded.contains(clazz)) {
                    continue;
                }

                loadData(dataLoaderMap.get(clazz));
            }
        }

        dataLoader.load();
        loaded.add(dataLoaderClass);
    }
}
