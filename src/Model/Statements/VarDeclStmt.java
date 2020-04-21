package Model.Statements;

import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIStack;
import Model.ProgramState.PrgState;
import Model.Type.*;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;

public class VarDeclStmt implements IStmt {
    private String name;
    private Type type;

    public VarDeclStmt(String n, Type t){
        name = n; type =t;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        Value val;
        if(symTbl.isDefined(name) == false) {
            if (type.equals(new IntType())) {
                val = new IntType().defaultValue();
            } else if (type.equals(new BoolType()))
                val = new BoolType().defaultValue();
            else if (type.equals(new StringType()))
                val = new StringType().defaultValue();
            else //if(type instanceof RefType) {
            {
                Type ref = ((RefType)type).getInner();
                val = new RefType(ref).defaultValue();
            }


            symTbl.add(name,val);
            //symTbl.add(name, type.defaultValue());
        }
        else
            throw new MyException("variable is already declared");
        return null;
    }

    @Override
    public IStmt dup() {
        return new VarDeclStmt(name, type);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.add(name,type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return name + " " + type.toString();
    }
}
