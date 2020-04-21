package Model.Statements.SemaphoreStatements;

import Model.MyException;
import Model.ProgramState.ISemTable;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIStack;
import Model.ProgramState.PrgState;
import Model.Statements.IStmt;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.util.List;

public class AcquireStmt implements IStmt {
    private String var_id;

    public AcquireStmt(String var){
        var_id = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIStack<IStmt> stk= state.getStk();

        if (symTbl.isDefined(var_id)) {
            Type typId = (symTbl.lookup(var_id)).getType(); //getValue / lookup
            if (!typId.equals(new IntType()))
                throw new MyException("type of variable" + var_id + " is not int|" + this.toString());
        } else throw new MyException("the used variable" + var_id + " is not in the sym table|" + this.toString());

        int foundIndex = ((IntValue)symTbl.lookup(var_id)).getVal(); //we can downcast to IntValue because we know the type is int

        ISemTable sem = state.getSemTable();

        if(!sem.isDefined(foundIndex))
            throw new MyException("index "+ foundIndex + " not in the semaphoreTable|" + this.toString());
        else
        {
            Pair<Integer, List<Integer>> pair = sem.getValue(foundIndex);
            int NL = pair.getValue().size();
            int n1 = pair.getKey();
            if ( n1  > NL)
            {
                if(! pair.getValue().contains(state.getId()))
                    pair.getValue().add(state.getId());
            }
            else
                stk.push(this.dup());
        }



        return null;
    }

    @Override
    public IStmt dup() {
        return new AcquireStmt(var_id);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "acquire(" + var_id + ")";
    }
}
