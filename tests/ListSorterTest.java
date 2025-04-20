import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListSorterTest {

    private ListSorter listSorter;

    @BeforeEach
    void setUp() {
        listSorter = new ListSorter();
    }

    @Test
    void add_preservesOriginalCasingAndSortsLowercase() {
        listSorter.add("Chase");
        listSorter.add("burrow");
        listSorter.add("Hubbard");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("burrow");
        expected.add("Chase");
        expected.add("Hubbard");

        assertEquals(expected, listSorter.getDisplayList());
    }

    @Test
    void binarySearch_isCaseInsensitive() {
        listSorter.add("Mixon");
        listSorter.add("Taylor");
        listSorter.add("Irwin");

        assertEquals(0, listSorter.binarySearch("irwin"));
        assertEquals(1, listSorter.binarySearch("mixon"));
        assertEquals(2, listSorter.binarySearch("TAYLOR"));
        assertEquals(-1, listSorter.binarySearch("anderson"));
        assertEquals(-4, listSorter.binarySearch("zebra"));
    }

    @Test
    void getDisplayList_returnsOriginalCasing() {
        listSorter.add("Apple");
        listSorter.add("banana");
        listSorter.add("Cat");

        ArrayList<String> result = listSorter.getDisplayList();

        assertEquals("Apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("Cat", result.get(2));
    }

    @Test
    void clear_emptiesTheList() {
        listSorter.add("Sample");
        listSorter.add("Test");
        assertEquals(2, listSorter.size());

        listSorter.clear();
        assertEquals(0, listSorter.size());
        assertTrue(listSorter.getDisplayList().isEmpty());
    }

    @Test
    void size_returnsCorrectCount() {
        assertEquals(0, listSorter.size());
        listSorter.add("One");
        assertEquals(1, listSorter.size());
        listSorter.add("Two");
        assertEquals(2, listSorter.size());
    }
}
