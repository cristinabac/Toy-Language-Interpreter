package Model.Statements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.MyIStack;
import Model.ProgramState.PrgState;
import Model.Type.Type;
import Model.Value.Value;

public class AssignStmt implements IStmt {
    private String id;
    private Exp exp;

    public AssignStmt(String i, Exp e){
        id = i; exp = e;
    }

    public String toString() {
        return id + "=" + exp.toString();
    }

    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTbl,heap);
        if (symTbl.isDefined(id)) {
            Type typId = (symTbl.lookup(id)).getType(); //getValue / lookup
            if ((val.getType()).equals(typId))
                symTbl.update(id, val);
            else
                throw new MyException("declared type of variable" + id + " and type of the assigned expression do not match");
        } else throw new MyException("the used variable" + id + " was not declared before");

        return null;

    }

    @Override
    public IStmt dup() {
        return new AssignStmt(id,exp.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types " + "\n assign statement: " + this.toString());
    }
}
