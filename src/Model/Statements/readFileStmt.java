package Model.Statements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.PrgState;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class readFileStmt implements IStmt {
    private Exp exp;
    private String var_name;

    public readFileStmt(Exp expression, String varName){
        this.exp = expression;
        this.var_name = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        MyIHeap<Value> heap = state.getHeap();
        //check if var_name is defined
        if(symTable.isDefined(var_name) == false)
            throw new MyException("Variable not defined!");
        //check if var_name is a variable of IntType
        Value val = symTable.lookup(var_name);
        if(val.getType().equals(new IntType()) == false)
            throw new MyException("Variable not int type!");
        //check if exp is a stringValue
        Value expVal = exp.eval(symTable,heap);
        if(expVal.getType().equals(new StringType()) == false)
            throw new MyException("Expression not string value!");
        String str = ((StringValue)expVal).getVal();
        BufferedReader buff = fileTable.lookup(str);
        if(buff == null)
            throw new MyException("File" + str +  " not found!");
        try{
            String line = buff.readLine();
            if(line != null){
                symTable.update(var_name,new IntValue(Integer.parseInt(line)));
            }
            else
                symTable.update(var_name, new IntValue(0));
        }
        catch(IOException err){
            throw new MyException("Error at reading from file " + str);
        }

        return null;
    }

    @Override
    public IStmt dup() {
        return new readFileStmt(exp.dup(),var_name);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (! typexp.equals(new StringType()))
            throw new MyException("The exp of read file has not the type string" + "\n readFile statement: " + this.toString());

        //var_name -> int
        Type typevar = typeEnv.lookup(var_name);
        if (! typevar.equals(new IntType()))
            throw new MyException("The variable of read file has not the type int" + "\n readFile statement: " + this.toString());

        return typeEnv;

    }

    @Override
    public String toString() {
        return "readFromFile(" +  exp.toString() + "," + var_name + ")";
    }
}
