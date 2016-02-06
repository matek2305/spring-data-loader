package pl.murbanski.spring.dataloader;

import com.google.common.collect.ImmutableSet;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import pl.murbanski.spring.dataloader.annotations.LoadDataAfter;

import java.util.Set;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ContextRefreshedDataLoaderTest implements WithBDDMockito {

    private final Set<Class<? extends DataLoader>> dataLoaderSet =
            ImmutableSet.of(SecondDataLoader.class, FirstDataLoader.class, AnotherDataLoader.class, ThirdDataLoader.class);


    private AutowireCapableBeanFactory autowireCapableBeanFactoryMock;
    private DataLoaderScanner dataLoaderScannerMock;

    private ContextRefreshedDataLoader dataLoader;

    @Before
    public void setUp() throws Exception {
        autowireCapableBeanFactoryMock = mock(AutowireCapableBeanFactory.class);
        dataLoaderScannerMock = mock(DataLoaderScanner.class);
        dataLoader = new ContextRefreshedDataLoader(autowireCapableBeanFactoryMock, dataLoaderScannerMock);
    }

    @Test
    public void name() throws Exception {
        given(dataLoaderScannerMock.scan()).willReturn(dataLoaderSet);
    }

    private class FirstDataLoader implements DataLoader {

        public void load() {

        }
    }

    @LoadDataAfter(FirstDataLoader.class)
    private class SecondDataLoader implements DataLoader {

        public void load() {

        }
    }

    private class ThirdDataLoader implements DataLoader {

        public void load() {

        }
    }

    @LoadDataAfter({FirstDataLoader.class, ThirdDataLoader.class})
    private class AnotherDataLoader implements DataLoader {

        public void load() {

        }
    }

}