package com.github.matek2305.dataloader;

import com.github.matek2305.dataloader.annotations.LoadDataAfter;
import com.github.matek2305.dataloader.exception.DataDependencyCycleFoundException;
import com.google.common.collect.ImmutableMap;
import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ContextRefreshedDataLoaderTest implements WithBDDMockito, WithAssertions {

    private ApplicationContext applicationContextMock;
    private ContextRefreshedDataLoader dataLoader;

    @Before
    public void setUp() throws Exception {
        applicationContextMock = mock(ApplicationContext.class);
        dataLoader = new ContextRefreshedDataLoader(applicationContextMock);

        given(applicationContextMock.getBeanNamesForType((Class<?>) any())).will(inv -> {
            String simpleName = ((Class<?>) inv.getArguments()[0]).getSimpleName();
            return new String[] {Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1)};
        });
    }

    @Test
    public void shouldCallAllLoadersInProperOrder() {
        // given
        final FirstDataLoader firstDataLoaderMock = mock(FirstDataLoader.class);
        final SecondDataLoader secondDataLoaderMock = mock(SecondDataLoader.class);
        final ThirdDataLoader thirdDataLoaderMock = mock(ThirdDataLoader.class);
        final AnotherDataLoader anotherDataLoaderMock = mock(AnotherDataLoader.class);

        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(new ImmutableMap.Builder<String, DataLoader>()
                .put("firstDataLoader", firstDataLoaderMock)
                .put("secondDataLoader", secondDataLoaderMock)
                .put("thirdDataLoader", thirdDataLoaderMock)
                .put("anotherDataLoader", anotherDataLoaderMock)
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

    @Test
    public void shouldThrowExceptionWhenDependencyCycleOccurs() throws Exception {
        // given
        final CycleDataLoader cycleDataLoader = mock(CycleDataLoader.class);
        final OtherCycleDataLoader otherCycleDataLoader = mock(OtherCycleDataLoader.class);
        final YetAnotherCycleDataLoader yetAnotherCycleDataLoader = mock(YetAnotherCycleDataLoader.class);

        given(applicationContextMock.getBeansOfType(eq(DataLoader.class))).willReturn(new ImmutableMap.Builder<String, DataLoader>()
                .put("cycleDataLoader", cycleDataLoader)
                .put("otherCycleDataLoader", otherCycleDataLoader)
                .put("yetAnotherCycleDataLoader", yetAnotherCycleDataLoader)
                .build());

        // expect
        assertThatThrownBy(() -> dataLoader.onApplicationEvent(mock(ContextRefreshedEvent.class)))
                .isExactlyInstanceOf(DataDependencyCycleFoundException.class);
    }

    @LoadDataAfter(ThirdDataLoader.class)
    private class FirstDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter({ThirdDataLoader.class, AnotherDataLoader.class})
    private class SecondDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    private class ThirdDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    private class AnotherDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(OtherCycleDataLoader.class)
    private class CycleDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(YetAnotherCycleDataLoader.class)
    private class OtherCycleDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(CycleDataLoader.class)
    private class YetAnotherCycleDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

}