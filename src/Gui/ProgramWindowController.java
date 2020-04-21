package Gui;

import Controller.Controller;
import Model.MyException;
import Model.ProgramState.MyIDictionary;
import Model.ProgramState.MyIHeap;
import Model.ProgramState.MyIStack;
import Model.ProgramState.PrgState;
import Model.Statements.IStmt;
import Model.Value.Value;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class ProgramWindowController {
    private Controller controller;
    private Integer currentPrgId=-1;
    private Stage selfStage = null;

    @FXML
    private Label nrPrgStatesLabel;

    @FXML
    private TextField nrOfPrgStatesTextField;

    @FXML
    private Label prgStateIdLabel;

    @FXML
    private ListView<Integer> prgStateIdListView;

    @FXML
    private Label exeStackLabel;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private Label symTblLabel;

    @FXML
    private TableView<Map.Entry<String,String>> symTableTableView;

    @FXML
    private TableColumn<Map.Entry<String,String>,String> varNameColumn;

    @FXML
    private TableColumn<Map.Entry<String,String>,String> valueSymTableColumn;

    @FXML
    private Label heapLabel;

    @FXML
    private TableView<Map.Entry<Integer,String>> heapTableView;

    @FXML
    private TableColumn<Map.Entry<Integer,String>,Integer> addressColumn;

    @FXML
    private TableColumn<Map.Entry<Integer,String>,String> valueHeapColumn;

    @FXML
    private Label fileTableLabel;

    @FXML
    private ListView<String> fileTblListView;

    @FXML
    private Label outLabel;

    @FXML
    private ListView<String> outListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    public void initialize(){
        this.nrOfPrgStatesTextField.setEditable(false);

        addressColumn.setCellValueFactory(p->new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        valueHeapColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue() + ""));

        varNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey() + ""));
        valueSymTableColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue() + ""));
    }

    private void populateAll(){
        populateExeStack(controller.getProgramById(currentPrgId));
        populateFileTable();
        populateHeap();
        populateIdList();
        populateOut();
        populateSymTable(controller.getProgramById(currentPrgId));
        populateNrIds();
    }

    private void populateNrIds(){
        //int nr = controller.getListOfPrgIds().size();
        int nr = controller.getNumberOfProgramStates();
        nrOfPrgStatesTextField.setText(Integer.toString(nr));
    }

    private void populateIdList(){
        ObservableList<Integer> lst = FXCollections.observableArrayList();
        //lst.addAll(controller.getListOfPrgIds());
        lst.addAll(controller.notCompletedProgramsIds());
        prgStateIdListView.setItems(lst);
    }

    private void populateHeap(){
        MyIHeap<Value> prgHeap = controller.getHeap();
        Map<Integer,String> newHeap = new HashMap<>();

        //make a new hash map, but <Integer,String>
        for(Integer key : prgHeap.getAll().keySet())
            newHeap.put(key,prgHeap.lookup(key).toString());

        //make that <Integer,string> map to match the format for map.entry...
        Map<Integer,String> heapMap = new HashMap<>();
        for(Map.Entry<Integer,String> entry: newHeap.entrySet())
            heapMap.put(entry.getKey(),entry.getValue());

        List<Map.Entry<Integer,String>> heapList = new ArrayList<>(heapMap.entrySet());
        this.heapTableView.setItems(FXCollections.observableList(heapList));
        this.heapTableView.refresh();

    }

    private void populateOut(){
        ObservableList<String> lst = FXCollections.observableArrayList();
        lst.addAll(controller.getOut().getAll().stream().map(Object::toString).collect(Collectors.toList()));
        outListView.setItems(lst);
    }

    private void populateExeStack(PrgState prg){
        MyIStack<IStmt> currentStack = prg.getStk();
        List<String> statements = new ArrayList<>();
        for(IStmt stmt : currentStack.getAll())
            statements.add(stmt.toString());
        this.exeStackListView.setItems(FXCollections.observableList(statements));
    }

    private void populateFileTable(){
        MyIDictionary<String, BufferedReader> fileTbl = controller.getFileTable();
        List<String> files = new ArrayList<>();
        for(String file : fileTbl.keys())
            files.add(file);
        this.fileTblListView.setItems(FXCollections.observableList(files));
    }

    private void populateSymTable(PrgState prg){
        MyIDictionary<String, Value> prgSymTable = prg.getSymTable();
        Map<String,String> newSymTbl = new HashMap<>();

        //make a new hash map, but <string,string>
        for(String key : prgSymTable.keys())
            newSymTbl.put(key,prgSymTable.lookup(key).toString());

        //make that <string,string> map to match the format for map.entry...
        Map<String,String> symbolMap = new HashMap<>();
        for(Map.Entry<String,String> entry: newSymTbl.entrySet())
            symbolMap.put(entry.getKey(),entry.getValue());

        List<Map.Entry<String,String>> symbolList = new ArrayList<>(symbolMap.entrySet());
        this.symTableTableView.setItems(FXCollections.observableList(symbolList));
        this.symTableTableView.refresh();

        /*
        Map<String, Pair<List<String>, IStmt>> sym = controller.getSymTable().getContent();
        List<Map.Entry<String, Pair<List<String>, IStmt>>> symLst = new ArrayList<>();

        for(Map.Entry<String, Pair<List<String>, IStmt>> elem : sym.entrySet())
            symLst.add(elem);

        symTbl.setItems(FXCollections.observableList(symLst));
        symTbl.refresh();
         */
    }


    public void setProgramController(Controller ctr){
        this.controller=ctr;
    }

    public void setPage(Stage stage){
        this.selfStage = stage;
    }

    @FXML
    void prgStateIdClicked(MouseEvent event) {
        this.currentPrgId = this.prgStateIdListView.getSelectionModel().getSelectedItem();
        if(controller.getListOfPrgIds().contains(currentPrgId)) {
            //populateExecutionStack(controller.getProgramById(currentPrgId));
            populateAll();
        }
        else
            currentPrgId=controller.getListOfPrgIds().get(0);
        System.out.println(currentPrgId);
    }

    @FXML
    void runOneStepButtonClicked(MouseEvent event) {
        try {
            if(currentPrgId==-1)
                currentPrgId=controller.getListOfPrgIds().get(0);
            if(!controller.getListOfPrgIds().contains(currentPrgId))
                currentPrgId=controller.getListOfPrgIds().get(0);

            controller.oneStepGui();
            populateAll();
            //if(controller.getListOfPrgIds().size()==0){
            if(controller.getNumberOfProgramStates()==0){
                Alert endOfExecution = new Alert(Alert.AlertType.INFORMATION);
                endOfExecution.setResizable(true);
                endOfExecution.setHeaderText("End of Program Execution! \n");
                endOfExecution.setContentText("Window Will Close Now!");
                endOfExecution.showAndWait();
                selfStage.close();
            }
            if(!controller.getListOfPrgIds().contains(currentPrgId))
                currentPrgId=controller.getListOfPrgIds().get(0);
        }
        catch (MyException | InterruptedException e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setResizable(true);
            errorAlert.setHeaderText("Execution Error\n");
            this.currentPrgId=-1;
            String err = "Type:\n";
            err = err.concat(e.getMessage().replace(":","\nMessage:\n"));
            errorAlert.setContentText(err+"\nWindow will close!");
            errorAlert.showAndWait();
            selfStage.close();
        }

    }

}