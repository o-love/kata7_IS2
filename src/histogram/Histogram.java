package histogram;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Histogram<T> {

    private final Map<T, Integer> map = new HashMap<>();

    public Integer get(T key) {
        return map.get(key);
    }

    public Set<T> keySet() {
        return map.keySet();
    }

    public void increment(T key) {
        map.merge(key, 1, Integer::sum);
    }
}
