package pl.murbanski.spring.dataloader.scanner;

import pl.murbanski.spring.dataloader.DataLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public interface DataLoaderScanner {

    String getPackage();

    Map<Class<? extends DataLoader>, DataLoader> getInstanceMap();
}
