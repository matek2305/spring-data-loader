package pl.murbanski.spring.dataloader.scanner;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.murbanski.spring.dataloader.DataLoader;

import java.util.Set;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Component
class ReflectionsDataLoaderScanner implements DataLoaderScanner {

    private final String dataLoaderPackage;

    @Autowired
    public ReflectionsDataLoaderScanner(
            @Value("${pl.murbanski.spring.dataloader.package:}") final String dataLoaderPackage) {
        this.dataLoaderPackage = dataLoaderPackage;
    }

    public String getPackage() {
        return dataLoaderPackage;
    }

    public Set<Class<? extends DataLoader>> scan() {
        return new Reflections(dataLoaderPackage).getSubTypesOf(DataLoader.class);
    }
}
