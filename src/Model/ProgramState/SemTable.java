package Model.ProgramState;

import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SemTable implements ISemTable{
    private Map<Integer, Pair<Integer, List<Integer>>> sem;
    private Integer index;

    public SemTable(){
        sem = new ConcurrentHashMap<>();
        index=0;
    }


    @Override
    public Collection<Integer> keys() {
        return sem.keySet();
    }

    @Override
    public boolean isDefined(Integer k) {
        return sem.containsKey(k);
    }

    @Override
    public void replace(Integer k, Pair<Integer, List<Integer>> pair) {
        sem.replace(k,pair);
    }

    @Override
    public Pair<Integer, List<Integer>> getValue(Integer k) {
        return sem.get(k);
    }

    @Override
    public Collection<Pair<Integer, List<Integer>>> values() {
        return sem.values();
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return sem;
    }

    @Override
    public synchronized void add(Pair<Integer, List<Integer>> pair) {
        index++;
        sem.put(index, pair);
    }

    @Override
    public Integer getAddr() {
        return index;
    }

    @Override
    public String toString() {
        return sem.toString();
    }
}
