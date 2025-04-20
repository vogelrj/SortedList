import java.util.ArrayList;

public class ListSorter {
    private static class Entry {
        String original;
        String lower;

        Entry(String original) {
            this.original = original;
            this.lower = original.toLowerCase();
        }
    }

    private final ArrayList<Entry> list = new ArrayList<>();

    public void add(String value) {
        String lower = value.toLowerCase();
        int index = findInsertIndex(lower);
        list.add(index, new Entry(value));
    }

    public int binarySearch(String value) {
        String lower = value.toLowerCase();
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = list.get(mid).lower.compareTo(lower);

            if (cmp == 0) return mid;
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return -low - 1;
    }

    private int findInsertIndex(String lower) {
        int low = 0;
        int high = list.size();

        while (low < high) {
            int mid = (low + high) / 2;
            if (list.get(mid).lower.compareTo(lower) < 0)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    public ArrayList<String> getDisplayList() {
        ArrayList<String> displayList = new ArrayList<>();
        for (Entry entry : list) {
            displayList.add(entry.original);
        }
        return displayList;
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }
}
