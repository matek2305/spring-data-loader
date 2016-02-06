package pl.murbanski.spring.dataloader.scanner;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import pl.murbanski.spring.dataloader.DataLoader;

import java.util.Set;

/**
 * {@link ReflectionsDataLoaderScanner} test.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ReflectionsDataLoaderScannerTest implements WithAssertions {

    @Test
    @SuppressWarnings("unchecked")
    public void scanShouldReturnTestDataLoader() {
        // given
        final DataLoaderScanner dataLoaderScanner = new ReflectionsDataLoaderScanner(TestDataLoader.class.getPackage().getName());
        // when
        final Set<Class<? extends DataLoader>> foundDataLoaders = dataLoaderScanner.scan();
        // then
        assertThat(foundDataLoaders).hasSize(1).containsOnly(TestDataLoader.class);
    }
}