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

public class WhileStmt implements IStmt {
    //while ( exp ) stmt
    Exp exp;
    IStmt stmt;

    public WhileStmt(Exp expression, IStmt statement){
        this.exp = expression;
        this.stmt = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIStack<IStmt> stack = state.getStk();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTable,heap);
        if(val.getType().equals(new BoolType()))
        {
            if(((BoolValue)val).getVal() == true) //if the cond of while is true, push on the stack the whole whileStmt, and the statement
            {
                stack.push(this.dup());
                stack.push(stmt);
            }
        }
        else
            throw new MyException("Condition expression not boolean!");
        return null;
    }

    @Override
    public IStmt dup() {
        return new WhileStmt(exp.dup(),stmt.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (! typexp.equals(new BoolType()))
            throw new MyException("The exp of while stmt has not the type bool" + "\n while statement: " + this.toString());
        stmt.typecheck(typeEnv.dup());
        return typeEnv;

    }

    @Override
    public String toString() {
        return "while(" + exp.toString() + ") " + stmt.toString();
    }
}
