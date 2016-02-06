package pl.murbanski.spring.dataloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.murbanski.spring.dataloader.scanner.DataLoaderScanner;

import java.util.Set;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Slf4j
@Component
public class ContextRefreshedDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
    private final DataLoaderScanner dataLoaderScanner;

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
        final Set<Class<? extends DataLoader>> foundDataLoaders = dataLoaderScanner.scan();
        log.info("Found {} loaders", foundDataLoaders.size());

        // TODO: scan package and find loaders

        log.info("Starting test data load ...");

        // TODO: execute in proper order

        log.info("Test data loaded successfully");
    }
}
