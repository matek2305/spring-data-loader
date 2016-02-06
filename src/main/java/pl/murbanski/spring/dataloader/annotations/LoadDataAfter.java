package pl.murbanski.spring.dataloader.annotations;

import pl.murbanski.spring.dataloader.DataLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadDataAfter {

    Class<? extends DataLoader>[] value();
}