package Gui;

import Model.MyException;
import Model.Type.Type;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Controller.Controller;
import Model.ProgramState.*;
import Model.Statements.IStmt;
import Repository.IRepository;
import Repository.Repository;
import View.Interpreter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainWindowController {
    private ArrayList<IStmt> programs;
    private Integer index = null;
    private Stage startWindow = null;
    private ArrayList<Integer> executedPrograms = new ArrayList<>();


    @FXML
    private ListView<String> programsList;

    @FXML
    private Label listLabel;

    @FXML
    private Label chooseProgramLabel;

    @FXML
    private Button executeButton;

    @FXML
    private TextArea selectedProgramTextArea;

    @FXML
    public void initialize(){
        this.selectedProgramTextArea.setEditable(false);
        this.programs = Interpreter.getAllExamples();
        this.populateList();
    }

    private void populateList() {
        ObservableList<String> lst = FXCollections.observableArrayList();
        lst.addAll(programs.stream().map(Object::toString).collect(Collectors.toList()));
        programsList.setItems(lst);
    }

    @FXML
    void executeButtonClicked(MouseEvent event) throws IOException {
        System.out.println(index);
        if(index==null){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("No selected item");
            errorAlert.setContentText("Please select a program from the list of programs!");
            errorAlert.showAndWait();
            return;
        }


        //TYPECHECKING
        try {
            programs.get(index).typecheck(new MyDictionary<>());
            System.out.println("typechecker passed :)");

            //starting the program, after we know that the tre program passed the typechecker
            startWindowForSelectedProgram(programs.get(index),index); //de aici => throws IOException
            executedPrograms.add(index);
            selectedProgramTextArea.setText("");
            index=null;
        }catch (MyException e){
            Alert endOfExecution = new Alert(Alert.AlertType.INFORMATION);
            endOfExecution.setResizable(true);
            endOfExecution.setHeaderText("Typecheck error! \n");
            endOfExecution.setContentText(e.getMessage());
            endOfExecution.showAndWait();
        }


    }

    private void startWindowForSelectedProgram(IStmt selectedProgram, Integer idx) throws IOException {
        PrgState prg = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),new MyDictionary<>(), new MyHeap<>(),new SemTable(), selectedProgram);
        IRepository repo = new Repository("log" + Integer.toString(index) +"txt");
        repo.addProgram(prg);
        Controller ctr = new Controller(repo, true);

        //start the new window for the selected program
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ProgramWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);

        ProgramWindowController programWindowController = fxmlLoader.getController();
        programWindowController.setProgramController(ctr);

        Stage stage = new Stage();
        stage.setTitle("Execution of the program");
        stage.setScene(scene);

        stage.initOwner(startWindow);
        stage.initModality(Modality.APPLICATION_MODAL);
        programWindowController.setPage(stage);
        stage.showAndWait();

    }

    @FXML
    void programClicked(MouseEvent event) {
        this.index = programsList.getSelectionModel().getSelectedIndex();
        selectedProgramTextArea.setText("Selected Program:\n\n\n");
        selectedProgramTextArea.appendText(programs.get(index).toString().replace(';','\n'));
    }

}
