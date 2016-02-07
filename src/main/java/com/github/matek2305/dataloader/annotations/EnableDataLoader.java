package com.github.matek2305.dataloader.annotations;

import com.github.matek2305.dataloader.config.DataLoaderConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable data loader scanning.
 * @see DataLoaderConfiguration
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(DataLoaderConfiguration.class)
public @interface EnableDataLoader {
}
