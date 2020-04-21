package Repository;

import Model.MyException;
import Model.ProgramState.PrgState;

import java.util.List;

public interface IRepository {
    public void addProgram(PrgState program);
    public void logPrgStateExec(PrgState prg) throws MyException;
    public List<PrgState> getPrgList();
    public void setPrgList(List<PrgState> prgStateList);
}
