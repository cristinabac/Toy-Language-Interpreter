package Model.Expression;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;

public class LogicExp implements Exp {
    private Exp e1;
    private Exp e2;
    private String op;

    public LogicExp(Exp e1, Exp e2, String op){
        e1=e1;e2=e2;op=op;
    }

    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heap) throws MyException {
        boolean res;
        Value v1,v2;
        v1=e1.eval(tbl,heap);
        if(v1.getType().equals(new BoolType())) {
            v2 = e2.eval(tbl,heap);
            if (v2.getType().equals(new BoolType())) {
                //both operands are ok
                BoolValue b1 = (BoolValue)v1;
                BoolValue b2 = (BoolValue)v2;
                if(op=="and")
                    res= b1.getVal() && b2.getVal();
                else //op=="or"
                    res= b1.getVal() || b2.getVal();
                Value val_final = new BoolValue(res);
                return val_final;
            } else
                throw new MyException("Operand 2 not boolean");
        }
        else
            throw new MyException("Operand 1 not boolean");
    }

    @Override
    public String toString() {
        return e1.toString() + op + e2.toString();
    }

    @Override
    public Exp dup() {
        return new LogicExp(e1.dup(),e2.dup(),op);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else
                throw new MyException("second operand is not a bool" + " \n logic expression: " + this.toString());
        }else
            throw new MyException("first operand is not a bool"+ " \n logic expression: " + this.toString());
    }
}
