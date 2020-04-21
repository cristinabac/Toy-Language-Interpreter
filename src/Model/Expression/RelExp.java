package Model.Expression;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;

public class RelExp implements Exp {
    private Exp e1;
    private Exp e2;
    private String op;

    public RelExp(Exp e1, Exp e2, String op){
        this.e1=e1; this.e2=e2; this.op=op;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Value> heap) throws MyException {
        boolean res=true;
        Value v1,v2;
        v1=e1.eval(tbl,heap);
        if(v1.getType().equals(new IntType())) {
            v2 = e2.eval(tbl,heap);
            if (v2.getType().equals(new IntType())) {
                //both operands are int, now evaluate the exp
                IntValue b1 = (IntValue) v1;
                IntValue b2 = (IntValue) v2;
                if(op=="<")
                    res= b1.getVal() < b2.getVal();
                if(op=="<=")
                    res= b1.getVal() <= b2.getVal();
                if(op=="==")
                    res= b1.getVal() == b2.getVal();
                if(op=="!=")
                    res= b1.getVal() != b2.getVal();
                if(op==">")
                    res= b1.getVal() > b2.getVal();
                if(op==">=")
                    res= b1.getVal() >= b2.getVal();
                Value val_final = new BoolValue(res);
                return val_final;
            } else
                throw new MyException("Operand 2 not int");
        }
        else
            throw new MyException("Operand 1 not int");
    }

    @Override
    public Exp dup() {
        return new RelExp(e1.dup(),e2.dup(),op);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType();
            } else
                throw new MyException("second operand is not an integer" + " \n rel expression: " + this.toString());
        }else
            throw new MyException("first operand is not an integer"+ " \n rel expression: " + this.toString());
    }

    @Override
    public String toString() {
        return e1.toString()+op+e2.toString();
    }
}
