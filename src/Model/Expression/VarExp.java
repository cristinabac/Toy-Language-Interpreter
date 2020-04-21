package Model.Expression;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.Type;
import Model.Value.Value;


public class VarExp implements Exp {
    private String id;

    public VarExp(String id){
        this.id = id;
    }

    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heap) throws MyException {
        return tbl.lookup(id);
    }

    @Override
    public java.lang.String toString() {
        return id;
    }

    @Override
    public Exp dup() {
        return new VarExp(id);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}
