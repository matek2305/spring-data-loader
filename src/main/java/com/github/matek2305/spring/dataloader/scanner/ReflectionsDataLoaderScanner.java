package com.github.matek2305.spring.dataloader.scanner;

import com.github.matek2305.spring.dataloader.DataLoader;
import com.github.matek2305.spring.dataloader.exception.DefaultConstructorNotFoundException;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Component
class ReflectionsDataLoaderScanner implements DataLoaderScanner {

    private final String dataLoaderPackage;
    private final AutowireCapableBeanFactory autowireCapableBeanFactoryMock;

    @Autowired
    public ReflectionsDataLoaderScanner(
            @Value("${pl.murbanski.spring.dataloader.package:}") final String dataLoaderPackage,
            final AutowireCapableBeanFactory autowireCapableBeanFactoryMock) {
        this.dataLoaderPackage = dataLoaderPackage;
        this.autowireCapableBeanFactoryMock = autowireCapableBeanFactoryMock;
    }

    @Override
    public String getPackage() {
        return dataLoaderPackage;
    }

    @Override
    public Map<Class<? extends DataLoader>, DataLoader> getInstanceMap() {
        Set<Class<? extends DataLoader>> foundLoaderClassSet = new Reflections(dataLoaderPackage).getSubTypesOf(DataLoader.class);
        return foundLoaderClassSet.stream().collect(Collectors.toMap(loader -> loader, this::makeInstance));
    }

    private DataLoader makeInstance(Class<? extends DataLoader> dataLoaderClass) {
        try {
            DataLoader dataLoader = dataLoaderClass.newInstance();
            autowireCapableBeanFactoryMock.autowireBean(dataLoader);
            return dataLoader;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DefaultConstructorNotFoundException("Default constructor for '" + dataLoaderClass.getName() + "' not found", e);
        }
    }
}