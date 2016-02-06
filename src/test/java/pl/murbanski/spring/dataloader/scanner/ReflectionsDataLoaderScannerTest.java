package pl.murbanski.spring.dataloader.scanner;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import pl.murbanski.spring.dataloader.DataLoader;

import java.util.List;
import java.util.Map;

/**
 * {@link ReflectionsDataLoaderScanner} test.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ReflectionsDataLoaderScannerTest implements WithBDDMockito, WithAssertions {

    @Test
    public void scanShouldReturnTestDataLoader() {
        // given
        final AutowireCapableBeanFactory autowireCapableBeanFactoryMock = mock(AutowireCapableBeanFactory.class);
        final DataLoaderScanner dataLoaderScanner = new ReflectionsDataLoaderScanner(TestDataLoader.class.getPackage().getName(), autowireCapableBeanFactoryMock);
        // when
        final Map<Class<? extends DataLoader>, DataLoader> dataLoaderMap = dataLoaderScanner.getInstanceMap();
        // then
        assertThat(dataLoaderMap).hasSize(1);
        assertThat(dataLoaderMap.get(TestDataLoader.class)).isNotNull().isExactlyInstanceOf(TestDataLoader.class);
    }
}