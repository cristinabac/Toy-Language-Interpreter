package Model.ProgramState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    //private ArrayList<T> lst;
    private List<T> lst;

    public MyList(){
        //lst = new ArrayList<T>();
        lst = Collections.synchronizedList(new ArrayList<T>() );
    }

    @Override
    public void add(T val) {
        lst.add(val);
    }

    @Override
    public List<T> getAll() {
        return lst;
    }

    @Override
    public String toString() {
        return lst.toString();
    }
}
