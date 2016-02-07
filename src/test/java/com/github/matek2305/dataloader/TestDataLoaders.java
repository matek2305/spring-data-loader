package com.github.matek2305.dataloader;

import com.github.matek2305.dataloader.annotations.LoadDataAfter;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
final class TestDataLoaders {

    @LoadDataAfter(FourthDataLoader.class)
    static class FirstDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter({ThirdDataLoader.class, AnotherDataLoader.class})
    static class SecondDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    static class ThirdDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(AnotherDataLoader.class)
    static class FourthDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    static class AnotherDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(OtherCycleDataLoader.class)
    static class CycleDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(YetAnotherCycleDataLoader.class)
    static class OtherCycleDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter(CycleDataLoader.class)
    static class YetAnotherCycleDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    @LoadDataAfter({SecondDataLoader.class, YetAnotherCycleDataLoader.class})
    static class TheBestTestDataLoader implements DataLoader {
        @Override
        public void load() {
        }
    }

    private TestDataLoaders() {
    }
}
