package Model.Statements;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIStack;
import Model.ProgramState.MyStack;
import Model.ProgramState.PrgState;
import Model.Type.Type;
import Model.Value.Value;

public class ForkStmt implements IStmt {
    IStmt stmt;

    public ForkStmt(IStmt stmt){
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> newStack = new MyStack<>();
        MyIDictionary<String, Value> symTblDeepcopy =state.getSymTable().dup();
        return new PrgState(newStack,symTblDeepcopy,state.getOutput(),state.getFileTable(),state.getHeap(),state.getSemTable(),stmt);
    }

    @Override
    public IStmt dup() {
        return new ForkStmt(stmt.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        stmt.typecheck(typeEnv.dup());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
