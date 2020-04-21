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
import java.io.FileReader;
import java.io.IOException;


public class openRFileStmt implements IStmt {
    private Exp exp;

    public openRFileStmt(Exp expression){
        this.exp = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<String,Value> symTable = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();
        Value val = exp.eval(symTable,heap);
        if(!val.getType().equals(new StringType()))
            throw new MyException("Type of evaluated expression not string!");
        //if we got here, val is a StringValue, so we can downcast
        StringValue strVal = (StringValue)val;
        if(fileTable.isDefined(strVal.getVal()))
            throw new MyException("File already exists");
        //try the actual opening of the file
        try{
            BufferedReader buffrd = new BufferedReader(new FileReader(strVal.getVal()));
            //if no exception thrown, add the name of the file and the corresp bufferedreader to the filetable
            fileTable.add(strVal.getVal(),buffrd);
        }
        catch (IOException err){
            throw new MyException("Error at opening the file " +strVal.toString());
        }

        return null;

    }

    @Override
    public IStmt dup() {
        return new openRFileStmt(exp.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (typexp.equals(new StringType())) {
            return typeEnv;
        }
        else
            throw new MyException("The exp of open file has not the type string" + "\n openRFile statement: " + this.toString());
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }
}
