package Model.Statements;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.PrgState;
import Model.Type.Type;

public interface IStmt {
    public PrgState execute(PrgState state) throws MyException;
    //which is the execution method for a statement.

    public String toString();

    public IStmt dup();

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;

}
