package Model.ProgramState;

import Model.MyException;
import Model.Statements.IStmt;
import Model.Value.Value;

import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;
    private IStmt originalProgram;

    private MyIDictionary<String, BufferedReader> fileTable;

    private ISemTable semTable;

    private MyIHeap<Value> heap;

    private static int id;

    private int actualId;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl, MyIList<Value> ot, MyIDictionary<String,BufferedReader> filetbl, MyIHeap<Value> hp,ISemTable sem, IStmt prg) {
        exeStack = stk;
        symTable = symtbl;
        out = ot;
        originalProgram = prg.dup(); //duplicate -> deepCopy
        this.fileTable = filetbl;
        this.heap = hp;
        stk.push(prg);

        semTable = sem;

        manageId();
    }

    public Integer getId(){
        return this.actualId;
    }

    private synchronized void manageId(){
        id++;
        actualId = id;
    }


    public Boolean isNotCompleted(){
        if(!this.exeStack.isEmpty())
            return true;
        return false;
    }

    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty())
            throw new MyException("prgstate exe stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    public MyIStack<IStmt> getStk() {
        return this.exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return this.symTable;
    }

    public MyIList<Value> getOutput() {
        return this.out;
    }

    public MyIDictionary<String,BufferedReader> getFileTable(){
        return this.fileTable;
    }

    public MyIHeap<Value> getHeap() {
        return this.heap;
    }

    public ISemTable getSemTable() {return this.semTable;}

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public String toString() {
        return "--Program ID " + Integer.toString(actualId) + "--\n" + "\nExeStack:\n" + exeStack.toString() + "\nSymTable:\n" + symTable.toString()
                + "\nOut:\n" + out.toString() + "\nFileTable:\n" + fileTable.toString() +"\nHeap:\n" + heap.toString() + "\nSemTable:\n"
                + semTable.toString() + "\n";
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(MyIDictionary<String, Value> symTable) {
        this.symTable = symTable;
    }

    public void setOut(MyIList<Value> out) {
        this.out = out;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }


}
