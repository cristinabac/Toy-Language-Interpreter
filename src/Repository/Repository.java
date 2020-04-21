package Repository;

import Model.MyException;
import Model.ProgramState.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

public class Repository implements IRepository{
    private List<PrgState> programs;

    private String logFilePath;

    public Repository(String filePath){
        this.programs = new Vector<PrgState>();
        this.logFilePath = filePath;
    }

    @Override
    public void addProgram(PrgState program) {
        programs.add(program);
    }

    @Override
    public void logPrgStateExec(PrgState prg) throws MyException {
        try{
            PrintWriter logFile= new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            logFile.write(prg.toString());
            logFile.close();
        }
        catch(IOException err){
            throw new MyException("Error writing in the log file");
        }
    }

    @Override
    public List<PrgState> getPrgList() {
        return programs;
    }

    @Override
    public void setPrgList(List<PrgState> prgStateList) {
        this.programs = prgStateList;
    }

}
