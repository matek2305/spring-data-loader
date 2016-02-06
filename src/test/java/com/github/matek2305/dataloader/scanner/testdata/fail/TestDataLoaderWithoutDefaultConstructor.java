package com.github.matek2305.dataloader.scanner.testdata.fail;

import com.github.matek2305.dataloader.DataLoader;

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
