package com.github.matek2305.dataloader;

import com.github.matek2305.dataloader.annotations.LoadDataAfter;
import com.github.matek2305.dataloader.exception.DataDependencyCycleFoundException;
import com.github.matek2305.dataloader.exception.DataLoaderBeanNotFoundException;
import com.github.matek2305.dataloader.exception.UnambiguousDataLoaderBeanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Component executing found data loader beans on {@link ContextRefreshedEvent}.
 * @see ApplicationListener
 * @see ContextRefreshedEvent
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Slf4j
@Component
public class ContextRefreshedDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationContext applicationContext;
    private final Set<String> loadedBeans = new HashSet<>();

    private Map<String, DataLoader> dataLoaderBeanMap;

    @Autowired
    public ContextRefreshedDataLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Scanning context for data loader beans ...");
        dataLoaderBeanMap = applicationContext.getBeansOfType(DataLoader.class);
        if (dataLoaderBeanMap.isEmpty()) {
            log.info("No data loader beans found");
            return;
        }

        log.info("Found {} loaders, starting data load ...", dataLoaderBeanMap.size());
        dataLoaderBeanMap.forEach((name, loaderBean) -> loadData(name));
        log.info("Data loaded successfully");
    }

    private void loadData(String beanName) {
        loadData(beanName, new HashSet<>());
    }

    private void loadData(String beanName, Set<String> dependencyChain) {
        if (loadedBeans.contains(beanName)) {
            return;
        }

        DataLoader dataLoader = checkNotNull(dataLoaderBeanMap.get(beanName), beanName + " bean not found!");
        Class<? extends DataLoader> dataLoaderClass = dataLoader.getClass();
        if (dataLoaderClass.isAnnotationPresent(LoadDataAfter.class)) {
            dependencyChain.add(beanName);

            Class<? extends DataLoader>[] dependencies = dataLoaderClass.getAnnotation(LoadDataAfter.class).value();
            for (Class<? extends DataLoader> clazz : dependencies) {
                final String dependencyBeanName = findBeanNameForClass(clazz);
                if (loadedBeans.contains(dependencyBeanName)) {
                    continue;
                }

                if (dependencyChain.contains(dependencyBeanName)) {
                    throw new DataDependencyCycleFoundException("Data dependency cycle found! Check your data loader classes.");
                }

                dependencyChain.add(dependencyBeanName);
                loadData(dependencyBeanName, dependencyChain);
            }
        }

        dataLoader.load();
        loadedBeans.add(beanName);
    }

    private String findBeanNameForClass(Class<? extends DataLoader> clazz) {
        String[] foundNames = applicationContext.getBeanNamesForType(clazz);
        if (foundNames == null || foundNames.length == 0) {
            throw new DataLoaderBeanNotFoundException("Bean for '" + clazz.getName() + "' class not found");
        } else if (foundNames.length > 1) {
            throw new UnambiguousDataLoaderBeanException("Could not determine bean for '" + clazz.getName() + "' class");
        } else {
            return foundNames[0];
        }
    }
}
