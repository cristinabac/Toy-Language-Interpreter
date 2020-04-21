package Model.Statements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.PrgState;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFileStmt implements IStmt {
    private Exp exp;

    public closeRFileStmt(Exp expression){ this.exp = expression;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTable,heap);
        if(val.getType().equals(new StringType()) == false)
            throw new MyException("Expression not String type!");
        String fileName = ((StringValue)val).getVal();
        BufferedReader buff = state.getFileTable().lookup(fileName);
        if(buff == null)
            throw new MyException("File not found in file table! " + fileName);
        try{
            buff.close();
            state.getFileTable().remove(fileName);
        }
        catch(IOException err){
            throw new MyException("Error at closing the file "+fileName);
        }
        return null;
    }

    @Override
    public IStmt dup() {
        return new closeRFileStmt(exp.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (typexp.equals(new StringType())) {
            return typeEnv;
        }
        else
            throw new MyException("The exp of close file has not the type string" + "\n closeRFile statement: " + this.toString());
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }
}
