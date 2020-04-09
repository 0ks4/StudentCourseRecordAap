/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author Asko
 */
public class MainOR extends Application implements Initializable {

    /**
     * main window navigation buttons
     **/
    private static  MainOR instance;
    @FXML
    private Button btnOpisk;
    @FXML
    private Button btnKurs;
    @FXML
    private Button btnSuor;

    @FXML
    MenuItem miTopStudents;

    @FXML
    MenuItem miTopCourses;

    @FXML
    MenuItem miTopRecords;

    @FXML
    MenuItem miTopInfo;

    /**
     * main window borderpanel
     **/
    @FXML
    BorderPane mainWindow;

    /**
     * main window close "X" button
     **/
    @FXML
    Button btnClose;
    /**
     * main window suppress "_" button
     **/
    @FXML
    Button btnSuppress;

    /**
     * Student window load students button
     **/
    @FXML
    Button btnLoadStudents;
    @FXML
    Button btnLoadCourses;
    @FXML
    Button btnLoadRecords;
    @FXML
    Button btnDeleteMultipleStudents;
    @FXML
    Button btnDeleteMultipleCourses;
    @FXML
    Button btnDeleteMultipleRecords;

    @FXML
    Button btnInfoOk;

    /**instructions for user as labels**/
    @FXML
    Label lblMultiDeleteInstruction;
    @FXML
    Label lblMultiDeleteInstruction1;
    @FXML
    Label lblMultiDeleteInstructionC;
    @FXML
    Label lblMultiDeleteInstructionC1;
    @FXML
    Label lblMultiDeleteInstructionRecord;
    @FXML
    Label lblMultiDeleteInstructionRecord1;

    /** togglebuttons for enabling the option for deleting multiple rows of data from tableviews**/
    @FXML
    ToggleButton tglbtnEnableMultiDeleteStudent;
    @FXML
    ToggleButton tglbtnEnableMultiDeleteCourse;

    @FXML
    ToggleButton tglbtnEnableMultiDeleteRecord;

    @FXML
    private MenuBar mbaMainBar;

    /**Textfield for searchboxes**/
    @FXML
    TextField txfStudentSearch;

    @FXML
    TextField txfRecordSearch;

    /**
     * Student window tableview and columns
     **/
    @FXML
    private TableView<Student> tbvStudentTable;
        @FXML
        private TableColumn<Student, String> tbcStudentID;
        @FXML
        private TableColumn<Student, String> tbcfirstName;
        @FXML
        private TableColumn<Student, String> tbclastName;
        @FXML
        private TableColumn<Student, String> tbcAddress;
        @FXML
        private TableColumn<Student, String> tbcZIP;
        @FXML
        private TableColumn<Student, String> tbcZIPLOC;
        @FXML
        private TableColumn<Student, String> tbcEmail;
        @FXML
        private TableColumn<Student, String> tbcPhone;

    /**
     * Course window tableview and columns for philosophy faculty
     **/
    @FXML
    private TableView<Course> tbvPhilosophyTable;
        @FXML
        private TableColumn<Course, String> tbcCourseIDP;
        @FXML
        private TableColumn<Course, String> tbcNameP;
        @FXML
        private TableColumn<Course, Integer> tbcCreditsP;
        @FXML
        private TableColumn<Course, String> tbcDescriptionP;
        @FXML
        private TableColumn<Course, String> tbcFaculty;

    /**
     * Tableview for all records in the system
     **/
    @FXML
    private TableView<Record> tbvRecordTable;
    @FXML
    private TableColumn<Record, LocalDate> tbcRecordDate;
        @FXML
        private TableColumn<Record, Integer> tbcRecordGrade;
        @FXML
        private TableColumn<Record, String> tbcRecordStudentID;
        @FXML
        private TableColumn<Record, String> tbcRecordCourseID;
        @FXML
        private TableColumn<Record, String> tbcRecordID;
        @FXML
        private TableColumn<Record, String> tbcRecordCourseName;
        @FXML
        private TableColumn<Record, String> tbcRecordStudentName;
        @FXML
        private TableColumn<Record, Integer> tbcRecordCredits;


    @FXML
    private ChoiceBox<String> cbxFaculty;
    
    /**
     * context menu and items for course tableview
     **/
    @FXML
    ContextMenu cxmCourse;
        @FXML
        MenuItem New_Course;
        @FXML
        MenuItem View_edit_Course;
        @FXML
        MenuItem Add_Record_For_This_Course;
        @FXML
        MenuItem Delete_Course;

    /**
     * context menu and items for student tableview
     **/
    @FXML
    ContextMenu cxmStudent;
    @FXML
    MenuItem New_Student;
    @FXML
    MenuItem View_edit_Student;
    @FXML
    MenuItem Add_Record_For_This_Student;
    @FXML
    MenuItem Delete_Student;
    
    /**
     * context menu and items for record tableview
     **/
    @FXML
    ContextMenu cxmRecord;
    @FXML
    MenuItem miView_Student;
    @FXML
    MenuItem miView_Course;
    @FXML
    MenuItem miEdit_Record;
    @FXML
    MenuItem miDeleteRecord;

    /**these attributes are used in the handling of the
     * search and filtering features of the student-,
     * course- and recordpages
     * **/
    private ChangeListener<String> choiceboxListener;
    private boolean choiceBoxListenerAdded = false;

    FilteredList<Course> courseFilteredList;
    private boolean courseFilteredListUsed=false;
    ObservableList<Course> courseList = FXCollections.observableArrayList();

    /**these variables are used as coordinates
     * in moving around the undecorated stage
     * with your mouse
     * **/
    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * main method
     **/
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("GUImainOR2.fxml"));
        primaryStage.setTitle("Tervetuloa Opiskelijarekisteriin");
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        final boolean resizable = primaryStage.isResizable();
        primaryStage.setResizable(!resizable);
        primaryStage.setResizable(resizable);
    }

    /**
     * this method is used for moving the undecorated stage
     * of the main window
     * this method handles the event of pressing down
     * the mouse button on menubar
     * gets the current screen coordinates of the stage
     * @param event = mousebutton press
     * **/
    @FXML
    protected void onMenuBarPressed(MouseEvent event) {
        Stage primaryStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        xOffset = primaryStage.getX() - event.getScreenX();
        yOffset = primaryStage.getY() - event.getScreenY();
    }

    /**
     * this method is used for moving the undecorated stage
     * of the main window
     * this method handles the event of releasing
     * the mouse button on menubar
     * gets the current screen coordinates of the event and sets the coordinates
     * for the stage
     * @param event = mousebutton press
     * **/
    @FXML
    protected void onMenuBarReleased(MouseEvent event) {
        Stage primaryStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        primaryStage.setX(event.getScreenX()+ xOffset);
        primaryStage.setY(event.getScreenY()+ yOffset);

        //final boolean resizable = primaryStage.isResizable();
        /*primaryStage.setResizable(!resizable);
        primaryStage.setResizable(resizable);
        primaryStage.setMaxWidth(mainWindow.getPrefWidth());
        System.out.println("stage width: "+primaryStage.getMaxWidth());
        System.out.println("borderpane width: "+mainWindow.getPrefWidth());
        System.out.println("xbutton location: "+btnClose.getLayoutX());*/
       // primaryStage.setWidth(mainWindow.getPrefWidth());
       // primaryStage.setHeight(mainWindow.getPrefHeight());
        System.out.println("stage height: "+mainWindow.getHeight());
    }
    
    /**
     * this method is used for moving the undecorated stage
     * of the main window
     * this method handles the event of dragging
     * the pressed mouse button on menubar
     * @param event = mouse drag
     *
     * **/
    @FXML
    protected void onMenuBarDragged(MouseEvent event) {
        Stage primaryStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        primaryStage.setX(event.getScreenX()+ xOffset);//+
        primaryStage.setY(event.getScreenY()+ yOffset );//
        primaryStage.setWidth(mainWindow.getPrefWidth());
        primaryStage.setHeight(mainWindow.getPrefHeight());
    }

    /**
     * this method is used to call the student page to the mainGUI
     */
    @FXML
    private void callStudentPage(ActionEvent e) {
        System.out.println("called studentpage");
        Pageloader loader = new Pageloader();
        Pane view = loader.getPage("GUIstudentpage");
        this.mainWindow.setCenter(view);
    }

    /**
     * this method is used to call the course page to the mainGUI
     */
    @FXML
    private void callCoursePage() {
        System.out.println("called coursepage");
        Pageloader loader = new Pageloader();
        Pane view = loader.getPage("GUIcoursePage");
        this.mainWindow.setCenter(view);
    }

    @FXML
    private void callAppInfo() {
        System.out.println("called application info page");
        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIinfoPage");
        Stage stage = new Stage();
        stage.setTitle("Application Info");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**
     * this method is used to call the record page to the mainGUI
     */
    @FXML
    private void callRecordPage(ActionEvent e) {
        System.out.println("called record page");
        Pageloader loader = new Pageloader();
        Pane view = loader.getPage("GUIrecordpage");
        this.mainWindow.setCenter(view);
    }

    /**
     * this method is used to shutdown the application
     **/
    @FXML
    private void systemExit(ActionEvent e) throws SQLException {
        System.exit(0);
    }

    /**
     * overridden initialize method
     * establishes the instance of this page
     * on to the static variable instance
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance=this;
    }

    /**
     * This method is used to populate student page's tableview
     * populates student table
     * adds listener for search textfield
     * adds listener for mouse doubleclicks on tableview
     *
     * @param e = actionevent from button
     **/
    @FXML
    public void callStudentData(ActionEvent e) {
    try {
    DBhandler dbh = new DBhandler();
    txfStudentSearch.clear();

    System.out.println("\t >> trying to populate student table");

    tbvStudentTable.getItems().removeAll();
    tbvStudentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    tbcStudentID.setCellValueFactory(new PropertyValueFactory<Student, String>("studentID"));
    tbcfirstName.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
    tbclastName.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
    tbcAddress.setCellValueFactory(new PropertyValueFactory<Student, String>("address"));
    tbcZIP.setCellValueFactory(new PropertyValueFactory<Student, String>("ZIP"));
    tbcZIPLOC.setCellValueFactory(new PropertyValueFactory<Student, String>("ZIPloc"));
    tbcEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
    tbcPhone.setCellValueFactory(new PropertyValueFactory<Student, String>("phone"));

    System.out.println("\t >> tableview prepped. Getting students...");

    ObservableList<Student> studentList = dbh.loadStudentData();
    System.out.println("\t >> Student retrieval done. Populating student table...");

    tbvStudentTable.setItems(studentList);

    System.out.println("\t >> student table populated.");

    tbvStudentTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                try {
                    callStudentEdit(new ActionEvent());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    });
    setStudentSearchListener();
    //btnLoadStudents.setDisable(true);
    } catch(UnsupportedOperationException exc){

    }

    }

    /**
     * method to call course data page and populate tableview
     * populates course table
     * adds listener for filtering choicebox
     * adds listener for mouse doubleclicks on tableview
     **/
    @FXML
    public void callCourseData() {

    if (choiceBoxListenerAdded) {
        cbxFaculty.valueProperty().removeListener(choiceboxListener);
    }
    if (courseFilteredListUsed) {
        courseFilteredList.getSource().removeAll();
    }

    ObservableList<String> cbxlist = FXCollections.observableArrayList(null, "Economics", "Humanities", "Philosophy", "Science", "Social Science");
    cbxFaculty.setItems(cbxlist);
    cbxFaculty.setValue(null);


    tbvPhilosophyTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    DBhandler dbh = new DBhandler();
    System.out.println("\t >> trying to populate course table");

    tbcCourseIDP.setCellValueFactory(new PropertyValueFactory<Course, String>("courseID"));
    tbcNameP.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
    tbcCreditsP.setCellValueFactory(new PropertyValueFactory<Course, Integer>("credits"));
    tbcDescriptionP.setCellValueFactory(new PropertyValueFactory<Course, String>("description"));
    tbcFaculty.setCellValueFactory(new PropertyValueFactory<Course, String>("faculty"));

    System.out.println("\t >> table prepped. Getting courses...");

    courseList.removeAll();
    courseList = dbh.loadCourseData();
    System.out.println("\t >> Course retrieval done. Populating course table...");
    tbvPhilosophyTable.getItems().removeAll();
    tbvPhilosophyTable.refresh();
    tbvPhilosophyTable.setItems(courseList);

    System.out.println("\t >> Course table populated.");

    tbvPhilosophyTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                callCourseEdit(new ActionEvent());
            }
        }
    });
    setCourseChoiceBoxSortListener();
    //btnLoadCourses.setDisable(true);
}

    /**
     * method to call record data page and populate tableview
     * populates record table
     * adds listener for search textfield
     * adds listener for mouse doubleclicks on tableview
     * @param d = button event
     **/
    @FXML
    public void callRecordData(ActionEvent d) {
        try {
            txfRecordSearch.clear();
            DBhandler db = new DBhandler();

            tbvRecordTable.getItems().removeAll();
            tbvRecordTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            DBhandler dbh = new DBhandler();
            System.out.println("\t >> trying to populate course table");

            tbcRecordDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
            tbcRecordGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
            tbcRecordStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
            tbcRecordCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
            tbcRecordID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
            tbcRecordCourseName.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
            tbcRecordStudentName.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
            tbcRecordCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

            System.out.println("\t >> table prepped. Getting courses...");

            ObservableList<Record> a = dbh.loadRecordData();
            System.out.println("\t >> Course retrieval done. Populating course table...");

            tbvRecordTable.setItems(a);

            System.out.println("\t >> Course table populated.");

            tbvRecordTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        callRecordEdit(new ActionEvent());
                    }
                }
            });
            setRecordSearchListener();
            //btnLoadRecords.setDisable(true);
        } catch (UnsupportedOperationException e){
        }
    }

    /**
     * method to call student data editing page
     * utilizes pageloader class
     * @param e = action event from button
     **/
    @FXML
    private void callStudentEdit(ActionEvent e) throws IOException {

        StudentEditPage.editMode = true;
        StudentEditPage.deleteMode = false;
        try {
            Student toBeEdited = tbvStudentTable.getSelectionModel().getSelectedItem();
            StudentEditPage.handledStudent = toBeEdited;
            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIstudentEditView");
            Stage stage = new Stage();
            stage.setTitle("View / Edit Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (NullPointerException ex){
            new AlertHandler().getTableError();
        }
    }

    /**
     * method to call student data editing page from recordpage
     * utilizes pageloader class
     * handles the mode booleans for the student edit page
     * @param e = action event from button
     **/
    @FXML
    private void callStudentEditFromRecords(ActionEvent e) throws IOException {
        DBhandler db = new DBhandler();
        StudentEditPage.editMode = true;
        StudentEditPage.deleteMode = false;
        try {
            Record r = tbvRecordTable.getSelectionModel().getSelectedItem();
            Student toBeEdited = db.getStudentByID(r.getStudentID());
            StudentEditPage.handledStudent = toBeEdited;
            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIstudentEditView");
            Stage stage = new Stage();
            stage.setTitle("View / Edit Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(NullPointerException ex){
            new AlertHandler().getTableError();
        }
    }

    /**
     * Opening the student editing page with empty fields and a new studentnumber
     * @param e = action event from button
     **/
    @FXML
    private void callStudentNew(ActionEvent e) throws IOException {

        StudentEditPage.editMode = false;
        StudentEditPage.deleteMode = false;

        StudentEditPage.nextStudentID = (new DBhandler().getNewStudentID());
        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIstudentEditView");
        Stage stage = new Stage();
        stage.setTitle("New Student");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /** method to call student edit page in delete -mode
     * @param e = action event from button
     *
     * **/
    @FXML
    private void callStudentDelete(ActionEvent e) throws IOException {

        StudentEditPage.editMode = false;
        StudentEditPage.deleteMode = true;
        try {
            Student toBeDeleted = tbvStudentTable.getSelectionModel().getSelectedItem();
            StudentEditPage.handledStudent = toBeDeleted;
            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIstudentEditView");
            Stage stage = new Stage();
            stage.setTitle("Delete Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch(NullPointerException ex){
            new AlertHandler().getTableError();
        }
    }

    /**
     * method to call course data editing page with selected course data in textfields
     *
     * @param e = action event from button or contextmenu
     **/
    @FXML
    private void callCourseEdit(ActionEvent e) {
        CourseEditPage.editMode = true;
        CourseEditPage.deleteMode = false;
        try {
            Course toBeEdited = tbvPhilosophyTable.getSelectionModel().getSelectedItem();
            CourseEditPage.handledCourse = toBeEdited;

            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIcourseEditView");
            Stage stage = new Stage();
            stage.setTitle("View / Edit Course");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch(NullPointerException ex) {
        new AlertHandler().getTableError();
        }
    }

    /** method to call course edit page from record list main page
     *
     * @param e = action event from button or contextmenu
     * **/
    @FXML
    private void callCourseEditFromRecords(ActionEvent e) {
        DBhandler db = new DBhandler();
        CourseEditPage.editMode = true;
        CourseEditPage.deleteMode = false;

        try {
            Record r = tbvRecordTable.getSelectionModel().getSelectedItem();
            Course toBeEdited = db.getCourseByID(r.getCourseID());
            CourseEditPage.handledCourse = toBeEdited;

            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIcourseEditView");
            Stage stage = new Stage();
            stage.setTitle("View / Edit Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch(NullPointerException ex) {
        new AlertHandler().getTableError();
        }
    }

    /**
     * Opening the course editing page with empty fields and a new coursenumber
     * @param e = action event from button or contextmenu
     **/
    @FXML
    private void callCourseNew(ActionEvent e) {

        CourseEditPage.editMode = false;
        CourseEditPage.deleteMode = false;
        CourseEditPage.nextCourseID = (new DBhandler().getNewCourseID());

        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIcourseEditView");
        Stage stage = new Stage();
        stage.setTitle("New Course");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**
     * method to call course page with delete mode options
     * @param e = action event from button
     **/
    @FXML
    private void callCourseDelete(ActionEvent e) throws IOException {

        CourseEditPage.editMode = false;
        CourseEditPage.deleteMode = true;
        try {
            Course toBeDeleted = tbvPhilosophyTable.getSelectionModel().getSelectedItem();
            CourseEditPage.handledCourse = toBeDeleted;

            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIcourseEditView");
            Stage stage = new Stage();
            stage.setTitle("Delete Course");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(NullPointerException ex) {
            new AlertHandler().getTableError();
        }
    }

    /**
     *  method to call record data editing page
     *  @param e = action event from button or menubar item
     **/
    @FXML
    private void callRecordEdit(ActionEvent e) {
        DBhandler db = new DBhandler();
        try{
        RecordEditPage.handledRecord = tbvRecordTable.getSelectionModel().getSelectedItem();
        RecordEditPage.handledStudent = db.getStudentByID(RecordEditPage.handledRecord.getStudentID());
        RecordEditPage.handledCourse = db.getCourseByID(RecordEditPage.handledRecord.getCourseID());

            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIrecordEditView");
            Stage stage = new Stage();
            stage.setTitle("View / Edit Record");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(NullPointerException ex){
            new AlertHandler().getTableError();
        }
    }

    /**
     * method to call a student's record page
     *
     **/
    @FXML
    private void addRecordForSelectedStudent() {

        try {
            Student s = tbvStudentTable.getSelectionModel().getSelectedItem();
            StudentEditPage.handledStudent = s;
            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIcourseSelectPage");
            Stage stage = new Stage();
            stage.setTitle("Add record for student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(NullPointerException ex){
            new AlertHandler().getTableError();
        }
    }

    /**
     *  method to add a record for selected course
     *  navigates to student select page
     **/
    @FXML
    private void addRecordForSelectedCourse() {

        try {
            Course c = tbvPhilosophyTable.getSelectionModel().getSelectedItem();
            CourseEditPage.handledCourse = c;
            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIstudentSelectPage");
            Stage stage = new Stage();
            stage.setTitle("Add record for student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(NullPointerException ex){
            new AlertHandler().getTableError();
        }
    }

    /**
     *  method to enable multideletemode for studentpage
     *  sets a delete button visible with
     *  instruction labels
     **/
    @FXML
    private void EnableMultiDeleteStudents(ActionEvent e) {
        if (btnDeleteMultipleStudents.isVisible()) {
            btnDeleteMultipleStudents.setVisible(false);
            lblMultiDeleteInstruction.setVisible(false);
            lblMultiDeleteInstruction1.setVisible(false);
            tglbtnEnableMultiDeleteStudent.setText("Enable multi-delete");
        } else {
            btnDeleteMultipleStudents.setVisible(true);
            lblMultiDeleteInstruction.setVisible(true);
            lblMultiDeleteInstruction1.setVisible(true);
            tglbtnEnableMultiDeleteStudent.setText("Disable multi-delete");
        }
    }

    /**
     *  method to select and delete multiple students
     *  requires a confirmation from user
     *  utilises dbhandlers method to delete
     *  from database
     * @param e = event from button
     **/
    @FXML
    public void deleteMultipleStudents(ActionEvent e) {
        String title = "Multi-delete in progress";
        String header = "This operation will delete all selected students and their records";
        String confirm = "Are you sure you wish to proceed? ";
        AlertHandler ah = new AlertHandler();
        DBhandler db = new DBhandler();
        ObservableList<Student> deleteList = FXCollections.observableArrayList();

        deleteList = tbvStudentTable.getSelectionModel().getSelectedItems();
        if (ah.getConfirmation(title, header, confirm) == ButtonType.OK) {
                db.massDeleteStudentFromDatabase(deleteList);
        } else {
            ah.getInformation("multi-delete cancelled", "Student information was not deleted", "");
        }
        callStudentData(new ActionEvent());
    }

    /**
     *  method to enable multideletemode for coursepage
     *  sets a delete button visible with
     *  instruction labels
     **/
    @FXML
    private void EnableMultiDeleteCourses(ActionEvent e) {
        if (btnDeleteMultipleCourses.isVisible()) {
            btnDeleteMultipleCourses.setVisible(false);
            lblMultiDeleteInstructionC.setVisible(false);
            lblMultiDeleteInstructionC1.setVisible(false);
            tglbtnEnableMultiDeleteCourse.setText("Enable multi-delete");
        } else {
            btnDeleteMultipleCourses.setVisible(true);
            lblMultiDeleteInstructionC.setVisible(true);
            lblMultiDeleteInstructionC1.setVisible(true);
            tglbtnEnableMultiDeleteCourse.setText("Disable multi-delete");
        }
    }

    /**
     *  method to select and delete multiple courses
     *  requires a confirmation from user
     *  utilises dbhandlers method to delete
     *  from database
     * @param e = actionevent from button
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     **/
    @FXML
    public void deleteMultipleCourses(ActionEvent e) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String title = "Multi-delete in progress";
        String header = "This operation will delete all selected courses and all records associated with them";
        String confirm = "Are you sure you wish to proceed? ";
        AlertHandler ah = new AlertHandler();
        DBhandler db = new DBhandler();
        ObservableList<Course> deleteList = FXCollections.observableArrayList();

        deleteList = tbvPhilosophyTable.getSelectionModel().getSelectedItems();
        if (ah.getConfirmation(title, header, confirm) == ButtonType.OK) {
            db.massDeleteCourseFromDatabase(deleteList);
        } else {
            ah.getInformation("multi-delete cancelled", "Course information was not deleted", "");
        }
       callCourseData();
    }
    
    /**
     *  method to select and delete multiple courses
     *  requires a confirmation from user
     *  utilises dbhandlers method to delete
     *  from database
     * @param e = event from button
     **/
    @FXML
    public void deleteMultipleRecords(ActionEvent e) {
        try {

            String title = "Multi-delete in progress";
            String header = "This operation will delete all selected records";
            String confirm = "Are you sure you wish to proceed? ";
            AlertHandler ah = new AlertHandler();
            DBhandler db = new DBhandler();
            ObservableList<Record> deleteList = FXCollections.observableArrayList();

            deleteList = tbvRecordTable.getSelectionModel().getSelectedItems();
            if (ah.getConfirmation(title, header, confirm) == ButtonType.OK) {
                db.deleteRecordsFromDatabase(deleteList);
            } else {
                ah.getInformation("multi-delete cancelled", "Record information was not deleted", "");
            }
            callRecordData(new ActionEvent());
        }catch(NullPointerException xce){
            new AlertHandler().getTableError();
        }
    }

    /**
     *  method to enable multideletemode for coursepage
     *  sets a delete button visible with
     *  instruction labels
     **/
    @FXML
    private void EnableMultiDeleteRecords(ActionEvent e) {
        if (btnDeleteMultipleRecords.isVisible()) {
            btnDeleteMultipleRecords.setVisible(false);
            lblMultiDeleteInstructionRecord.setVisible(false);
            lblMultiDeleteInstructionRecord1.setVisible(false);
            tglbtnEnableMultiDeleteRecord.setText("Enable multi-delete");
        } else {
            btnDeleteMultipleRecords.setVisible(true);
            lblMultiDeleteInstructionRecord.setVisible(true);
            lblMultiDeleteInstructionRecord1.setVisible(true);
            tglbtnEnableMultiDeleteRecord.setText("Disable multi-delete");
        }
    }

    /**
     *  method to set a textfield search listener
     *  for student page tableview element
     * it uses a filteredlist object
     * that is comprised of user input
     *  and compares it to tableview column contents
     **/
    public void setStudentSearchListener(){
        FilteredList<Student> studentFilteredList= new FilteredList<>(tbvStudentTable.getItems(), b -> true);

        txfStudentSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            studentFilteredList.setPredicate(Student ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Student.getFirstName().toLowerCase().indexOf(lowerCaseFilter)!=-1){
                    return true;
                } else if(Student.getLastName().toLowerCase().indexOf(lowerCaseFilter) !=-1){
                    return true;
                } else if(String.valueOf(Student.getStudentID()).indexOf(lowerCaseFilter)!=-1){
                    return true;
                } else return false;
            });
        });
        SortedList<Student>studentSortedList = new SortedList<>(studentFilteredList);
        studentSortedList.comparatorProperty().bind(tbvStudentTable.comparatorProperty());
        tbvStudentTable.setItems(studentSortedList);
    }

    /**
     *  method to set a textfield search listener
     *  for record page tableview element
     * it uses a filteredlist object
     * that is comprised of user input
     *  and compares it to tableview column contents
     **/
    public void setRecordSearchListener(){
        FilteredList<Record> recordFilteredList= new FilteredList<>(tbvRecordTable.getItems(), b -> true);

        txfRecordSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            recordFilteredList.setPredicate(Record ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                newValue.toString();
                String lowerCaseFilter = newValue.toLowerCase();
                if(Record.getStudentName().toLowerCase().indexOf(lowerCaseFilter)!=-1){
                    return true;
                } else if(Record.getCourseName().toLowerCase().indexOf(lowerCaseFilter) !=-1){
                    return true;
                } else if(String.valueOf(Record.getRecordID()).indexOf(lowerCaseFilter)!=-1){
                    return true;
                }
                else if(String.valueOf(Record.getCredits()).contains(lowerCaseFilter)){
                    return true;
                }
                else if(String.valueOf(Record.getStudentID()).indexOf(lowerCaseFilter)!=-1){
                    return true;
                }
                else if(String.valueOf(Record.getCourseID()).indexOf(lowerCaseFilter)!=-1){
                    return true;
                }
                else if(String.valueOf(Record.getRecordDate()).indexOf(lowerCaseFilter)!=-1){
                    return true;
                }
                else return false;
            });
        });
        SortedList<Record>recordSortedList = new SortedList<>(recordFilteredList);
        recordSortedList.comparatorProperty().bind(tbvRecordTable.comparatorProperty());
        tbvRecordTable.setItems(recordSortedList);
    }

    /**
     *  method to set a choicebox filter listener
     *  for record page tableview element
     * similarly to textfield search, it uses a filteredlist object
     * that is comprised of readymade strings corresponding to faculties
     *  it then filters the tableview contents according to chosen faculty
     **/
    public void setCourseChoiceBoxSortListener(){
        courseFilteredList= new FilteredList<>(tbvPhilosophyTable.getItems(), b -> true);

        cbxFaculty.valueProperty().addListener(choiceboxListener = new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    courseFilteredList.setPredicate(newValue== null ? null : (Course) -> newValue.equals(Course.getFaculty()));
            }
        });
        courseFilteredListUsed = true;
        choiceBoxListenerAdded = true;
        tbvPhilosophyTable.setItems(courseFilteredList);
    }

    /**this method returns the instance of the mainpage
     * the instance is used, when invoking methods
     * outside the mainpage, mainly when returning back
     * from edipages to tableview, and a tableview refresh
     * is needed.
     * @return = instance of this object
     * **/
    public static MainOR getInstance(){
            return instance;
    }

    /**
     * this method is used to minimize the mainstage
     * @param e = button event
     * **/
    @FXML
    private void minimizeWindow(ActionEvent e){
        Stage stage = (Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /** this method is used to close the information panel
     * @param e = button event
     *
     * **/
    @FXML
    private void closeInfo(ActionEvent e){
        Stage stage = (Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow();
        stage.hide();
        stage.close();
    }
}