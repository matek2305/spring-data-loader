package pl.murbanski.spring.dataloader.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.murbanski.spring.dataloader.ContextRefreshedDataLoader;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Configuration
@ComponentScan(basePackageClasses = ContextRefreshedDataLoader.class)
public class DataLoaderConfiguration {
}
