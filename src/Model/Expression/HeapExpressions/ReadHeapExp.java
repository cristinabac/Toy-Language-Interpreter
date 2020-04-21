package Model.Expression.HeapExpressions;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;

public class ReadHeapExp implements Exp {
    private Exp exp;

    public ReadHeapExp(Exp exp){
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Value> heap) throws MyException {
        Value val = exp.eval(tbl,heap);
        if(!(val instanceof RefValue))
            throw new MyException("Expression " + exp.toString() +" not RefValue!");
        RefValue refVal = (RefValue)val;
        int addr = refVal.getAddress();
        Value valHeap = heap.lookup(addr);

        return valHeap;

    }

    @Override
    public Exp dup() {
        return new ReadHeapExp(exp);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ=exp.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft =(RefType) typ;
            return reft.getInner();
        } else
            throw new MyException("the rH argument is not a Ref Type" + "\n read heap expression: " + this.toString());
    }

    @Override
    public String toString() {
        return "readHeap(" + exp.toString() + ")";
    }
}
