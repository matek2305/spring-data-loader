package com.github.matek2305.spring.dataloader.scanner;

import com.github.matek2305.spring.dataloader.exception.DefaultConstructorNotFoundException;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.github.matek2305.spring.dataloader.DataLoader;
import com.github.matek2305.spring.dataloader.scanner.testdata.fail.TestDataLoaderWithoutDefaultConstructor;
import com.github.matek2305.spring.dataloader.scanner.testdata.pass.TestDataLoader;

import java.util.Map;

/**
 * {@link ReflectionsDataLoaderScanner} test.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ReflectionsDataLoaderScannerTest implements WithBDDMockito, WithAssertions {

    private AutowireCapableBeanFactory autowireCapableBeanFactoryMock;

    @Before
    public void setUp() throws Exception {
        autowireCapableBeanFactoryMock = mock(AutowireCapableBeanFactory.class);
    }

    @Test
    public void scanShouldReturnTestDataLoader() {
        // given
        final DataLoaderScanner dataLoaderScanner = new ReflectionsDataLoaderScanner(TestDataLoader.class.getPackage().getName(), autowireCapableBeanFactoryMock);
        // when
        final Map<Class<? extends DataLoader>, DataLoader> dataLoaderMap = dataLoaderScanner.getInstanceMap();
        // then
        assertThat(dataLoaderMap).hasSize(1);
        assertThat(dataLoaderMap.get(TestDataLoader.class)).isNotNull().isExactlyInstanceOf(TestDataLoader.class);
    }

    @Test
    public void shouldThrowExceptionWhenDataLoaderHasNoDefConstructor() {
        // given
        final DataLoaderScanner dataLoaderScanner = new ReflectionsDataLoaderScanner(TestDataLoaderWithoutDefaultConstructor.class.getPackage().getName(), autowireCapableBeanFactoryMock);
        // expect
        assertThatThrownBy(dataLoaderScanner::getInstanceMap)
                .isExactlyInstanceOf(DefaultConstructorNotFoundException.class)
                .hasMessageContaining(TestDataLoaderWithoutDefaultConstructor.class.getName());
    }
}