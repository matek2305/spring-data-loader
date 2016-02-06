package pl.murbanski.spring.dataloader;

import pl.murbanski.spring.dataloader.annotations.LoadDataAfter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ContextRefreshedDataLoaderTest {

    private List<Class<? extends DataLoader>> dataLoaderList =
            Arrays.asList(SecondDataLoader.class, FirstDataLoader.class, AnotherDataLoader.class, ThirdDataLoader.class);

    class FirstDataLoader implements DataLoader {

        public void load() {

        }
    }

    @LoadDataAfter(FirstDataLoader.class)
    class SecondDataLoader implements DataLoader {

        public void load() {

        }
    }

    class ThirdDataLoader implements DataLoader {

        public void load() {

        }
    }

    @LoadDataAfter({FirstDataLoader.class, ThirdDataLoader.class})
    class AnotherDataLoader implements DataLoader {

        public void load() {

        }
    }

}