package pl.murbanski.spring.dataloader;

import java.util.Set;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public interface DataLoaderScanner {

    String getPackage();

    Set<Class<? extends DataLoader>> scan();
}
