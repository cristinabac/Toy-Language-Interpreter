package Model.Expression;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.Type;
import Model.Value.Value;

import java.util.HashMap;

public class ValueExp implements Exp {
    private Value e;

    public ValueExp(Value v){
        e = v;
    }

    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heap) throws MyException {
        return e;
    }

    @Override
    public String toString() {
        return e.toString();
    }

    @Override
    public Exp dup() {
        return new ValueExp(e.dup());
    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException{
        return e.getType();
    }
}
