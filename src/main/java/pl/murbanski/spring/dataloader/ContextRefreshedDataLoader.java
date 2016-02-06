package pl.murbanski.spring.dataloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Slf4j
@Component
public class ContextRefreshedDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    public ContextRefreshedDataLoader(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Starting test data load ...");

        // TODO: find loaders and execute in proper order

        log.info("Test data loaded successfully");
    }
}
