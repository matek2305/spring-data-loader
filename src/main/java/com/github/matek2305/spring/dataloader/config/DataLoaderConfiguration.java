package com.github.matek2305.spring.dataloader.config;

import com.github.matek2305.spring.dataloader.ContextRefreshedDataLoader;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Configuration
@ComponentScan(basePackageClasses = ContextRefreshedDataLoader.class)
public class DataLoaderConfiguration {
}
