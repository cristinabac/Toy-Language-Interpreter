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


public class WriteHeapStmt implements IStmt {
    private String var_name;
    private Exp exp;

    public WriteHeapStmt(String var_name, Exp expression){
        this.var_name = var_name;
        this.exp = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap<Value> heap = state.getHeap();

        if(!symTable.isDefined(var_name))
            throw new MyException("Variable " + var_name + " not defined!");
        Value val = symTable.lookup(var_name);
        if(!(val.getType() instanceof RefType))
            throw new MyException("Type of " + var_name + " not RefType!");

        //check if the address from the RefValue associated in SymTable is a key in Heap
         int addrRefVal = ((RefValue)symTable.lookup(var_name)).getAddress();
         if(!heap.isDefined(addrRefVal))
             throw new MyException("Address " + Integer.toString(addrRefVal) + " does not exist in the heap!");

         //the expression is evaluated and the result must have its type equal to the
        // locationType of the var_name type
        Value expVal = exp.eval(symTable,heap);
        Type locationType = ((RefValue)val).getLocationType();
        if(!expVal.getType().equals(locationType))
            throw new MyException("Evaluated expression type does not match with location type!  (var name: " + var_name + ")");

        heap.update(addrRefVal,expVal);

        return null;
    }

    @Override
    public IStmt dup() {
        return new WriteHeapStmt(var_name, exp.dup());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(var_name);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("Write heap stmt: right hand side and left hand side have different types " + "\n write heap statement: " + this.toString());
    }

    @Override
    public String toString() {
        return "writeHeap(" + var_name + "," + exp.toString() + ")";
    }
}
