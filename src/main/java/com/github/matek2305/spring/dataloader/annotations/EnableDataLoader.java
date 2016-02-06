package com.github.matek2305.spring.dataloader.annotations;

import com.github.matek2305.spring.dataloader.config.DataLoaderConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(DataLoaderConfiguration.class)
public @interface EnableDataLoader {
}
