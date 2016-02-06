package pl.murbanski.spring.dataloader.scanner;

import pl.murbanski.spring.dataloader.DataLoader;

import java.util.Set;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public interface DataLoaderScanner {

    String getPackage();

    Set<Class<? extends DataLoader>> scan();
}
