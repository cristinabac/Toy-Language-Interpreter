package Model.Statements.SemaphoreStatements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.ISemTable;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.PrgState;
import Model.Statements.IStmt;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.util.ArrayList;

public class CreateSemaphoreStmt implements IStmt {
    private String var_id;
    private Exp exp;

    public CreateSemaphoreStmt(String id, Exp e){
        var_id=id;
        exp=e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTbl,heap);
        if(!val.getType().equals(new IntType()))
            throw new MyException("create sem, exp no int type|" + this.toString());
        int nr = ((IntValue)val).getVal();

        ISemTable sem = state.getSemTable();

        sem.add(new Pair<>(nr, new ArrayList<>()));


        if (symTbl.isDefined(var_id)) { //var exists
            Type typId = (symTbl.lookup(var_id)).getType(); //getValue / lookup
            if (typId.equals(new IntType())) //var has int type
                symTbl.update(var_id, new IntValue(sem.getAddr()));
            else
                throw new MyException("variable " + var_id + " not int type|" + this.toString());
        } else throw new MyException("variable " + var_id + " does not exist in the sym table|" + this.toString());


        return null;
    }

    @Override
    public IStmt dup() {
        return new CreateSemaphoreStmt(var_id,exp.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "createSemaphore(" + var_id + "," + exp.toString() + ")";
    }
}
