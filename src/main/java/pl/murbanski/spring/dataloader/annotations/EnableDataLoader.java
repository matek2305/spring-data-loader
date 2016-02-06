package pl.murbanski.spring.dataloader.annotations;

import org.springframework.context.annotation.Import;
import pl.murbanski.spring.dataloader.config.DataLoaderConfiguration;

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
