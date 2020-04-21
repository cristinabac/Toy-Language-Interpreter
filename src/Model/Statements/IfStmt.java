package Model.Statements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.MyIStack;
import Model.ProgramState.PrgState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;

public class IfStmt implements IStmt {
    private Exp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(Exp e, IStmt t, IStmt el) {exp=e; thenS=t;elseS=el;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTlb = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTlb,heap);
        MyIStack<IStmt> stk = state.getStk();
        if(val.getType().equals(new BoolType())){
            BoolValue b = (BoolValue)val; //downcast -> but in this point i am sure that val is a boolean value
            if(b.getVal() == true) //if val is true <=> the cond is true
                stk.push(thenS);
            else
                stk.push(elseS);
        }
        else
            throw new MyException("conditional expr is not a boolean");
        return null;
    }

    @Override
    public IStmt dup() {
        return new IfStmt(exp.dup(),thenS.dup(),elseS.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(typeEnv.dup());
            elseS.typecheck(typeEnv.dup());
            return typeEnv;
        }
        else
            throw new MyException("The condition of IF has not the type bool" + "\n if statement: " + this.toString());
    }

    @Override
    public String toString() {
        return "IF("+ exp.toString()+") THEN(" +thenS.toString() +")ELSE("+elseS.toString()+")";
    }
}
