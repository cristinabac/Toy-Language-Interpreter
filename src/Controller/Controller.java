package Controller;

import Model.MyException;
import Model.ProgramState.*;
import Model.Statements.IStmt;
import Model.Value.RefValue;
import Model.Value.Value;
import Repository.IRepository;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repo;
    private boolean displayFlag;

    private ExecutorService executor;

    public Controller(IRepository r, boolean df){
        repo = r; displayFlag=df;
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList){

        return inPrgList.stream()
                .filter(p->p.isNotCompleted())
                .collect(Collectors.toList());

    }

    public void displayCrtState(PrgState state){
        if(displayFlag)
            System.out.println(state.toString());
    }

    public void oneStepForAllPrg(List<PrgState> prgList) {
        //before the execution, print the PrgState List into the log file
        prgList.forEach(prg ->repo.logPrgStateExec(prg));

        //RUN concurrently one step for each of the existing PrgStates
        //-----------------------------------------------------------------------
        //prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());

        //start the execution of the callables
        //it returns the list of new created PrgStates (namely threads)
        List<PrgState> newPrgList = null;
        try {
            newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (MyException | InterruptedException | ExecutionException e) {
                            //e.printStackTrace();
                            throw new MyException(e.getMessage());
                        }
                        //return null;  //pentru ca aveam mai jos - missing return statement...
                       })
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        //add the new created threads to the list of existing threads
        if(newPrgList != null)
            prgList.addAll(newPrgList);
        //-----------------------------------------------------------------------------

        //after the execution, print the PrgState List into the log file
        prgList.forEach(prg ->repo.logPrgStateExec(prg));
        //Save the current programs in the repository
        repo.setPrgList(prgList);

    }


    public void allStep(){
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList=removeCompletedPrg(repo.getPrgList());
        while (prgList.size() > 0) {
            executeGarbageCollector();
            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdownNow();
        //HERE the repository still contains at least one Completed Prg
        // and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
        //setPrgList of repository in order to change the repository

        // update the repository state
        repo.setPrgList(prgList);
    }


    private void executeGarbageCollector(){
        List<PrgState> programs = repo.getPrgList();
        List<List<Integer>> addrSymTbl = programs.stream().map(PrgState::getSymTable).map(p->getAddrFromSymTable(p.values())).collect(Collectors.toList());
        List<Integer> addresses = new ArrayList<Integer>();
        addrSymTbl.forEach(addresses::addAll);
        List<Integer> add2 = getAddrFromHeap(programs.get(0).getHeap().values());
        addresses.addAll(add2);
        Map<Integer,Value> garbCol = unsafeGarbageCollector(addresses, programs.get(0).getHeap().getAll());
        programs.forEach(p->p.getHeap().setContent(garbCol));
    }


    private Map<Integer, Value> unsafeGarbageCollector(List<Integer> addresses, Map<Integer,Value> heap){
        return heap.entrySet().stream().filter(e->addresses.contains(e.getKey())).
                collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues){
        return symTableValues.stream().
                filter(v->v instanceof RefValue).
                map(v->((RefValue)v).getAddress()).
                collect(Collectors.toList());
    }

    private List<Integer> getAddrFromHeap(Collection<Value> heapValues){
        /* For the case when an address from the heap will reference to another reference

        Initially:
        new(v,20)
        new(a,v)
        new(v,30)

        heap: 1->20, 2->1, 3->30


         */
        return heapValues.stream().
                filter(v->v instanceof RefValue)
                .map(v->((RefValue)v).getAddress())
                .collect(Collectors.toList());
    }


    //-----------------------------------------------------------------------------------------------
    //NEW FUNCTIONS FOR ASSIGN 6
    //-----------------------------------------------------------------------------------------------

    public List<Integer> getListOfPrgIds(){
        return repo.getPrgList().stream().map(PrgState::getId).collect(Collectors.toList());
    }

    public MyIHeap<Value> getHeap(){
        return repo.getPrgList().get(0).getHeap(); //al prgs have the same heap, so return the one of the first program
    }

    public MyIList<Value> getOut(){
        return repo.getPrgList().get(0).getOutput();
    }

    public PrgState getProgramById(Integer id){
        if(id == null)
            return null;
        for(PrgState prg: repo.getPrgList()) {
            if (prg.getId() == id.intValue())
                return prg;
        }
        return null;
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {return repo.getPrgList().get(0).getFileTable();}

    public void oneStepGui() throws InterruptedException {
        executor = Executors.newFixedThreadPool(5);
        removeCompletedPrg(repo.getPrgList());
        List<PrgState> programs = repo.getPrgList();

        if (programs.size() > 0) {
            executeGarbageCollector();
            //oneStepForAllPrg(programs); //????
            oneStepForAllPrgGui(programs);
            removeCompletedPrg(repo.getPrgList());
            executor.shutdownNow();
        }
    }


    public Integer getNumberOfProgramStates(){
        Integer nr = 0;
        for(PrgState p: repo.getPrgList())
            if(!p.getStk().isEmpty()){
                nr++;
            }
        return nr;
    }


    public List<Integer> notCompletedProgramsIds(){

        return repo.getPrgList().stream()
                .filter(p->p.isNotCompleted())
                .map(p->p.getId())
                .collect(Collectors.toList());
    }



    public void oneStepForAllPrgGui(List<PrgState> programs) throws InterruptedException {
        /*
        prgList.forEach(prg ->repo.logPrgStateExec(prg));

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());

        List<PrgState> newPrgList = null;
        try {
            newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (MyException | InterruptedException | ExecutionException e) {
                            throw new MyException(e.getMessage());
                        }
                    })
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        if(newPrgList != null)
            prgList.addAll(newPrgList);

        prgList.forEach(prg ->repo.logPrgStateExec(prg));

        repo.setPrgList(prgList);

         */
        programs.forEach(prg->{repo.logPrgStateExec(prg);
            System.out.println(prg);});

        List<Callable<PrgState>> callList = programs.stream().filter(p->!p.getStk().isEmpty()).
                map((PrgState p)->(Callable<PrgState>)(p::oneStep)).collect(Collectors.toList());

        List<PrgState> newProgramList = executor.invokeAll(callList).stream().map(future->{
            try{
                return future.get();
            }
            catch (MyException | InterruptedException | ExecutionException exc){
                throw new MyException(exc.getMessage());
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        programs.addAll(newProgramList);
        programs.forEach(prg->{
            System.out.println(prg);repo.logPrgStateExec(prg);});
        repo.setPrgList(programs);
    }

}
