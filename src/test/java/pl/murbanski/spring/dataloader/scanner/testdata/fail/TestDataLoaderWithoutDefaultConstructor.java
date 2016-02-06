package pl.murbanski.spring.dataloader.scanner.testdata.fail;

import pl.murbanski.spring.dataloader.DataLoader;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class TestDataLoaderWithoutDefaultConstructor implements DataLoader {

    private TestDataLoaderWithoutDefaultConstructor() {
    }

    @Override
    public void load() {
    }
}
