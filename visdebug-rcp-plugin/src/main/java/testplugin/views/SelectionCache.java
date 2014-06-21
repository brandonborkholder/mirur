package testplugin.views;

import java.util.Map;
import java.util.TreeMap;

public class SelectionCache {
    private Map<String, PrimitiveArray> cache;
    private String currentSelection;

    public SelectionCache() {
        cache = new TreeMap<>();
        currentSelection = null;
    }

    public void clear() {
        cache.clear();
        currentSelection = null;
    }

    public String getCurrent() {
        return currentSelection;
    }

    public void setCurrent(String name) {
        currentSelection = name;
    }

    public PrimitiveArray getArray(String name) {
        return cache.get(name);
    }

    public void put(String name, PrimitiveArray array) {
        cache.put(name, array);
    }
}
