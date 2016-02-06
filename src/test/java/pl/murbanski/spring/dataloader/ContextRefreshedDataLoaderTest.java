package pl.murbanski.spring.dataloader;

import com.google.common.collect.ImmutableMap;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import pl.murbanski.spring.dataloader.annotations.LoadDataAfter;
import pl.murbanski.spring.dataloader.scanner.DataLoaderScanner;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ContextRefreshedDataLoaderTest implements WithBDDMockito {

    private DataLoaderScanner dataLoaderScannerMock;
    private ContextRefreshedDataLoader dataLoader;

    @Before
    public void setUp() throws Exception {
        dataLoaderScannerMock = mock(DataLoaderScanner.class);
        dataLoader = new ContextRefreshedDataLoader(mock(AutowireCapableBeanFactory.class), dataLoaderScannerMock);
    }

    @Test
    public void shouldDoNothingWhenPackageIsEmpty() {
        // given
        given(dataLoaderScannerMock.getPackage()).willReturn(null);
        // when
        dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class));
        // then
        verify(dataLoaderScannerMock, never()).getInstanceMap();
    }

    @Test
//    @Ignore("passing when running from IDEA, idk why not working from maven yet")
    public void shouldCallAllLoadersInProperOrder() {
        // given
        final FirstDataLoader firstDataLoaderMock = mock(FirstDataLoader.class);
        final SecondDataLoader secondDataLoaderMock = mock(SecondDataLoader.class);
        final ThirdDataLoader thirdDataLoaderMock = mock(ThirdDataLoader.class);
        final AnotherDataLoader anotherDataLoaderMock = mock(AnotherDataLoader.class);

        given(dataLoaderScannerMock.getPackage()).willReturn(this.getClass().getPackage().getName());
        given(dataLoaderScannerMock.getInstanceMap()).willReturn(new ImmutableMap.Builder<Class<? extends DataLoader>, DataLoader>()
                .put(FirstDataLoader.class, firstDataLoaderMock)
                .put(SecondDataLoader.class, secondDataLoaderMock)
                .put(ThirdDataLoader.class, thirdDataLoaderMock)
                .put(AnotherDataLoader.class, anotherDataLoaderMock)
                .build());

        InOrder inOrder = inOrder(thirdDataLoaderMock, firstDataLoaderMock, anotherDataLoaderMock, secondDataLoaderMock);
        // when
        dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class));
        // then
        inOrder.verify(thirdDataLoaderMock).load();
        inOrder.verify(firstDataLoaderMock).load();
        inOrder.verify(anotherDataLoaderMock).load();
        inOrder.verify(secondDataLoaderMock).load();
    }

    @LoadDataAfter(ThirdDataLoader.class)
    private class FirstDataLoader implements DataLoader {

        public void load() {

        }
    }

    @LoadDataAfter({ThirdDataLoader.class, AnotherDataLoader.class})
    private class SecondDataLoader implements DataLoader {

        public void load() {

        }
    }

    private class ThirdDataLoader implements DataLoader {

        public void load() {

        }
    }

    private class AnotherDataLoader implements DataLoader {

        public void load() {

        }
    }

}