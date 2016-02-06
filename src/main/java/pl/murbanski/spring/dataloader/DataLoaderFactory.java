package pl.murbanski.spring.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@Component
public class DataLoaderFactory {

    private final AutowireCapableBeanFactory autowireCapableBeanFactoryMock;

    @Autowired
    public DataLoaderFactory(final AutowireCapableBeanFactory autowireCapableBeanFactoryMock) {
        this.autowireCapableBeanFactoryMock = autowireCapableBeanFactoryMock;
    }

    DataLoader make(Class<? extends DataLoader> dataLoaderClass) {
        checkNotNull(dataLoaderClass);

        try {
            DataLoader dataLoader = dataLoaderClass.newInstance();
            autowireCapableBeanFactoryMock.autowireBean(dataLoader);
            return dataLoader;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
