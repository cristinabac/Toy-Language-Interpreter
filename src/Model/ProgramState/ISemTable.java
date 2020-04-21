package Model.ProgramState;

import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ISemTable {
    public Collection<Integer> keys();
    public boolean isDefined(Integer k);
    public void replace(Integer k, Pair<Integer, List<Integer>> pair);
    public Pair<Integer, List<Integer>> getValue(Integer k);
    public Collection<Pair<Integer, List<Integer>>> values();
    public Map<Integer, Pair<Integer, List<Integer>>> getContent();

    public void add(Pair<Integer, List<Integer>> pair);
    public Integer getAddr();
}
