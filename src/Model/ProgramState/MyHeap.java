package Model.ProgramState;

import Model.MyException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyHeap<Value> implements MyIHeap<Value> {
    private Map<Integer,Value> heap;
    private int id=0;

    public MyHeap(){
        heap = new ConcurrentHashMap<Integer, Value>();
    }

    private int generateAddress(){
        id++;
        return id;
    }

    @Override
    public boolean isDefined(int id) {
        return heap.containsKey(id);
    }

    @Override
    public void update(int id, Value val) {
        heap.replace(id,val);
    }

    @Override
    public int add(Value val) {
        //returns the generated address
        int id=this.generateAddress();
        heap.put(id,val);
        return id;
    }

    @Override
    public void remove(int id) {
        heap.remove(id);
    }

    @Override
    public Value lookup(int id) {
        if(!heap.containsKey(id))
            throw new MyException("Variable not defined in the heap!");
        return heap.get(id);
    }

    @Override
    public Collection<Value> values() {
        return heap.values();
    }

    @Override
    public void setContent(Map<Integer, Value> newHeap) {
        heap = (HashMap<Integer, Value>) newHeap;
    }

    @Override
    public Map<Integer, Value> getAll() {
        return heap;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(Integer key : heap.keySet())
            result.append(key.toString()).append("->").append(heap.get(key).toString()).append("\n");
        return result.toString();
        //return heap.toString();
    }
}
