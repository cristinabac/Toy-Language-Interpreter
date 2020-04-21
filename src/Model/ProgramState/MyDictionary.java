package Model.ProgramState;

import Model.MyException;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {
    private ConcurrentHashMap<K,V> dictionary;

    public MyDictionary(){
        dictionary = new ConcurrentHashMap<K, V>();
    }

    @Override
    public boolean isDefined(K id) {
        return dictionary.containsKey(id);
    }

    @Override
    public void update(K id, V val) {
        dictionary.replace(id, val);
    }

    @Override
    public void add(K id, V val) {
        dictionary.put(id, val);
    }

    @Override
    public void remove(K id) {
        dictionary.remove(id);
    }

    @Override
    public V lookup(K id) {
        if(!dictionary.containsKey(id))
            throw new MyException("key - " +  id + " - not in the dictionary! cannot return the value!");
        return dictionary.get(id);
    }

    @Override
    public Collection<V> values() {
        return dictionary.values();
    }

    @Override
    public MyIDictionary<K, V> dup() {
        return new MyDictionary<>(this); //we need the copy constructor...
    }

    @Override
    public ConcurrentHashMap<K, V> getAll() {
        return dictionary;
    }

    @Override
    public Collection<K> keys() {
        return dictionary.keySet();
    }

    //copy constructor
    public MyDictionary(MyDictionary<K,V> myDict){
        this.dictionary = new ConcurrentHashMap<>();
        myDict.dictionary.forEach((k,v)->this.dictionary.put(k,v));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(K key : dictionary.keySet())
            result.append(key.toString()).append("->").append(dictionary.get(key).toString()).append("\n");
        return result.toString();
        //return dictionary.toString();
    }
}
