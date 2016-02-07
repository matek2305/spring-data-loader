package com.github.matek2305.dataloader;

import com.github.matek2305.dataloader.exception.DataDependencyCycleFoundException;
import com.github.matek2305.dataloader.exception.DataLoaderBeanNotFoundException;
import com.github.matek2305.dataloader.exception.UnambiguousDataLoaderBeanException;
import com.google.common.collect.ImmutableMap;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Collections;

import static com.github.matek2305.dataloader.TestDataLoaders.*;

/**
 * {@link ContextRefreshedDataLoader} test cases.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ContextRefreshedDataLoaderTest implements WithBDDMockito, WithAssertions {

    private ApplicationContext applicationContextMock;
    private ContextRefreshedDataLoader dataLoader;

    @Before
    public void setUp() throws Exception {
        applicationContextMock = mock(ApplicationContext.class);
        dataLoader = new ContextRefreshedDataLoader(applicationContextMock);
    }

    @Test
    public void shouldDoNothingWhenDataLoaderBeanNotFound() {
        // given
        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(Collections.emptyMap());

        // when
        dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class));

        // then
        verify(applicationContextMock).getBeansOfType(eq(DataLoader.class));
        verifyNoMoreInteractions(applicationContextMock);
    }

    @Test
    public void shouldThrowExceptionWhenDependencyBeanNotFound() {
        // given
        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(new ImmutableMap.Builder<String, DataLoader>()
                .put("firstDataLoader",  mock(FirstDataLoader.class))
                .build());

        // expect
        assertThatThrownBy(() -> dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class)))
                .isExactlyInstanceOf(DataLoaderBeanNotFoundException.class)
                .hasMessageContaining(FourthDataLoader.class.getName());

    }

    @Test
    public void shouldThrowExceptionWhenUnambiguousBeanDefinitionFound() {
        // given
        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(new ImmutableMap.Builder<String, DataLoader>()
                .put("firstDataLoader",  mock(FirstDataLoader.class))
                .build());

        given(applicationContextMock.getBeanNamesForType((Class<?>) any())).willReturn(new String[] { "a", "b" });

        // expect
        assertThatThrownBy(() -> dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class)))
                .isExactlyInstanceOf(UnambiguousDataLoaderBeanException.class)
                .hasMessageContaining(FourthDataLoader.class.getName());
    }

    @Test
    public void shouldCallAllLoadersInProperOrder() {
        // given
        final DataLoader firstDataLoaderMock = mock(FirstDataLoader.class);
        final DataLoader secondDataLoaderMock = mock(SecondDataLoader.class);
        final DataLoader thirdDataLoaderMock = mock(ThirdDataLoader.class);
        final DataLoader fourthDataLoaderMock = mock(FourthDataLoader.class);
        final DataLoader anotherDataLoaderMock = mock(AnotherDataLoader.class);

        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(new ImmutableMap.Builder<String, DataLoader>()
                .put("firstDataLoader", firstDataLoaderMock)
                .put("secondDataLoader", secondDataLoaderMock)
                .put("thirdDataLoader", thirdDataLoaderMock)
                .put("fourthDataLoader", fourthDataLoaderMock)
                .put("anotherDataLoader", anotherDataLoaderMock)
                .build());

        given(applicationContextMock.getBeanNamesForType((Class<?>) any())).will(inv -> {
            String simpleName = ((Class<?>) inv.getArguments()[0]).getSimpleName();
            return new String[] {Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1)};
        });

        InOrder inOrder = inOrder(anotherDataLoaderMock, fourthDataLoaderMock, firstDataLoaderMock, thirdDataLoaderMock, secondDataLoaderMock);

        // when
        dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class));

        // then
        inOrder.verify(anotherDataLoaderMock).load();
        inOrder.verify(fourthDataLoaderMock).load();
        inOrder.verify(firstDataLoaderMock).load();
        inOrder.verify(thirdDataLoaderMock).load();
        inOrder.verify(secondDataLoaderMock).load();
    }

    @Test
    public void shouldThrowExceptionWhenDependencyCycleOccurs() throws Exception {
        // given
        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(new ImmutableMap.Builder<String, DataLoader>()
                .put("theBestTestDataLoader", mock(TheBestTestDataLoader.class))
                .put("secondDataLoader", mock(SecondDataLoader.class))
                .put("thirdDataLoader", mock(ThirdDataLoader.class))
                .put("anotherDataLoader", mock(AnotherDataLoader.class))
                .put("cycleDataLoader", mock(CycleDataLoader.class))
                .put("otherCycleDataLoader", mock(OtherCycleDataLoader.class))
                .put("yetAnotherCycleDataLoader", mock(YetAnotherCycleDataLoader.class))
                .build());

        given(applicationContextMock.getBeanNamesForType((Class<?>) any())).will(inv -> {
            String simpleName = ((Class<?>) inv.getArguments()[0]).getSimpleName();
            return new String[] {Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1)};
        });

        // expect
        assertThatThrownBy(() -> dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class)))
                .isExactlyInstanceOf(DataDependencyCycleFoundException.class);
    }

}