package Model.Statements.HeapStatements;

import Model.Expression.Exp;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.PrgState;
import Model.Statements.IStmt;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;

public class NewStmt implements IStmt {
    private String var_name;
    private Exp exp;

    public NewStmt(String var_name,Exp expression){
        this.var_name = var_name;
        this.exp = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();

        if(!symTable.isDefined(var_name))
            throw new MyException("Variable " + var_name +  " not defined!");
        Value val = symTable.lookup(var_name);
        if(!(val.getType() instanceof RefType))
            throw new MyException("Variable " + var_name + " not RefType");


        //Evaluate the expression to a value and then compare the type of the value to the
        //locationType from the value associated to var_name in SymTable
        Value expValue = exp.eval(symTable, heap); //value of the expression
        RefType varRefType = (RefType)(val.getType());
        Type locationType = varRefType.getInner();
        if(!expValue.getType().equals(locationType))
            throw new MyException("Ref type and type of expression do not match!");

        int addr = heap.add(expValue);
        symTable.update(var_name,new RefValue(addr,varRefType.getInner()));

        return null;
    }

    @Override
    public IStmt dup() {
        return new NewStmt(var_name,exp.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(var_name);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("NEW stmt: right hand side and left hand side have different types " + "\n new statement: " + this.toString());
    }

    @Override
    public String toString() {
        return "new(" + var_name + "," + exp.toString() + ")";
    }
}
