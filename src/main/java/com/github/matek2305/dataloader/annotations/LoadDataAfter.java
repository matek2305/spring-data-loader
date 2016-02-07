package com.github.matek2305.dataloader.annotations;

import com.github.matek2305.dataloader.DataLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Dependency marker for data loader.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadDataAfter {

    /**
     * Dependent data loaders.
     * @return dependency loaders classes
     */
    Class<? extends DataLoader>[] value();
}
