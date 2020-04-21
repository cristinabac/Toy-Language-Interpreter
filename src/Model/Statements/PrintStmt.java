package Model.Statements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.MyIList;
import Model.ProgramState.PrgState;
import Model.Type.Type;
import Model.Value.Value;

public class PrintStmt implements IStmt{
    private Exp exp;

    public PrintStmt(Exp e){
        exp = e;
    }

    public String toString(){
        return "print(" +exp.toString()+")";}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTbl,heap);
        MyIList<Value> out = state.getOutput();
        out.add(val);
        return null;
    }

    @Override
    public IStmt dup() {
        return new PrintStmt(exp.dup());

    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }


}
