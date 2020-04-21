package Model.Expression;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.Type;
import Model.Value.Value;

import java.util.HashMap;

public interface Exp {
    Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heap) throws MyException;
    public String toString();
    public Exp dup();
    Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
}
