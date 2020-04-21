package Model.ProgramState;

import Model.Value.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface MyIHeap<V>{
    //heap<Integer,V>

    public boolean isDefined(int id);
    public void update (int id, V val);
    public int add(V val);
    public void remove(int id);
    public V lookup(int id);

    public Collection<V> values();

    public void setContent(Map<Integer, V> newHeap);
    public Map<Integer,V> getAll();

}
