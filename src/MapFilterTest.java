import java.util.*;
import java.util.stream.Collectors;

public class MapFilterTest {

    private final int DATA_SIZE = 10000;
    private final Map<Integer, String> DATA = new HashMap<>();
    private List<Set<Integer>> TEST_FILTERS = new ArrayList<>();

    public static void main(String[] args) {
        MapFilterTest main = new MapFilterTest();
        long startTime = System.nanoTime();
        main.doSetup();
        long endTime = System.nanoTime();
        System.out.println("----- Data setup: " + (endTime - startTime));

        main.retainAllTest();
        main.streamFilterTest();
        main.parallelStreamFilterTest();
    }

    /**
     * Setup the data.
     * Generate a random map, and a list of random size designed to consist of random keys from the map.
     * Stored globally to ensure a random data set that is shared between each test method.
     */
    private void doSetup() {
        for (int i = 0; i < DATA_SIZE; i++) {
            DATA.put(i, UUID.randomUUID().toString());
        }

        int numberOfTests = 1 + (int)(Math.random() * ((DATA_SIZE - 1) + 1));
        for (int i = 0; i < numberOfTests; i++) {
            Set<Integer> test = new HashSet<>();
            int testFilterSize = 1 + (int)(Math.random() * ((DATA_SIZE - 1) + 1));
            for (int j = 0; j < testFilterSize; j++) {
                test.add(1 + (int)(Math.random() * ((DATA_SIZE - 1) + 1)));
            }
            TEST_FILTERS.add(test);
        }
    }

    /**
     * In this test, make a copy of the map and perform retainAll on the map keyset.
     */
    private void retainAllTest() {
        long startTime = System.nanoTime();

        for (Set<Integer> test : TEST_FILTERS) {
            long innerStart = System.nanoTime();

            Map<Integer, String> copy = new HashMap<>(DATA);
            copy.keySet().retainAll(test);

            long innerEnd = System.nanoTime();
            System.out.println((innerEnd - innerStart));
        }


        long endTime = System.nanoTime();
        System.out.println("----- retainAllTest total time: " + (endTime - startTime));
    }

    /**
     * In this test, stream and filter the map.
     */
    private void streamFilterTest() {
        long startTime = System.nanoTime();

        for (Set<Integer> test : TEST_FILTERS) {
            long innerStart = System.nanoTime();

            DATA.keySet().stream().filter(test::contains).collect(Collectors.toList());

            long innerEnd = System.nanoTime();
            System.out.println((innerEnd - innerStart));
        }


        long endTime = System.nanoTime();
        System.out.println("----- streamFilterTest total time: " + (endTime - startTime));
    }

    /**
     * In this test, stream and filter the map.
     */
    private void parallelStreamFilterTest() {
        long startTime = System.nanoTime();

        for (Set<Integer> test : TEST_FILTERS) {
            long innerStart = System.nanoTime();

            DATA.keySet().parallelStream().filter(test::contains).collect(Collectors.toList());

            long innerEnd = System.nanoTime();
            System.out.println((innerEnd - innerStart));
        }


        long endTime = System.nanoTime();
        System.out.println("----- parallelStreamFilterTest total time: " + (endTime - startTime));
    }
}
