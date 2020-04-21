package Model.ProgramState;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface MyIDictionary<K,V> {
    public boolean isDefined(K id);
    public void update (K id, V val);
    public void add(K id, V val);
    public void remove(K id);
    public V lookup(K id);
    public Collection<V> values();
    public MyIDictionary<K,V> dup();
    public ConcurrentHashMap<K,V> getAll();
    public Collection<K> keys();
}
