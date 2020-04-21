package Model.Expression;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;

import java.util.HashMap;

public class ArithExp implements Exp {
    private Exp e1;
    private Exp e2;
    private char op;

    public ArithExp(char operand, Exp exp1, Exp exp2){
        op=operand; e1 = exp1; e2 = exp2;
    }

    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heap) throws MyException {
        Value v1,v2;
        v1= e1.eval(tbl,heap);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.eval(tbl,heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue)v1;
                IntValue i2 = (IntValue)v2;
                int n1,n2;
                n1= i1.getVal();
                n2 = i2.getVal();
                String str="+-*/";
                if(str.indexOf(op) == -1)
                    throw new MyException("Operand not ok");
                if (op=='+') return new IntValue(n1+n2);
                if (op =='-') return new IntValue(n1-n2);
                if(op=='*') return new IntValue(n1*n2);
                if(op=='/')
                    if(n2==0) throw new MyException("division by zero");
                    else return new IntValue(n1/n2);
            }else
                throw new MyException("second operand is not an integer");
        }else
            throw new MyException("first operand is not an integer");
        return null;
    }

    @Override
    public String toString() {
        return e1.toString() + op + e2.toString();
    }

    @Override
    public Exp dup() {
        return new ArithExp(op,e1.dup(),e2.dup());
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else
            throw new MyException("second operand is not an integer" + " \n arithm expression: " + this.toString());
        }else
        throw new MyException("first operand is not an integer"+ " \n arithm expression: " + this.toString());
    }
}
