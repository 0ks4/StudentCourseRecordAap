/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import javafx.collections.ObservableList;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 *
 * @author Asko
 */
public class StudentEditPage extends Application implements Initializable {
    /**
     * @param instance = the object instance of this page
     * @param handledStudent = the student that will be handled here in edit mode
     * @param editMode = this boolean is set to true, when editing an existing student, otherwise set to false
     * @param deleteMode = this boolean is set to true when deleting a selected student, otherwise set to false
     * @param nextStudentID = this string is defined when creating a new student by dbhandler new student id -method
     *
     *
     * **/
     private static StudentEditPage instance;
     static Student handledStudent;

     static boolean editMode = false;
     static boolean deleteMode = false;

     static String nextStudentID;

    /**
     * Textfields
     **/
    @FXML
    TextField txfID;
    @FXML
    TextField txfFirstName;
    @FXML
    TextField txfLastName;
    @FXML
    TextField txfAddress;
    @FXML
    TextField txfZIP;
    @FXML
    TextField txfZIPloc;
    @FXML
    TextField txfEmail;
    @FXML
    TextField txfPhone;

    /**
     * Buttons
     **/
    @FXML
    Button btnEditInfo;
    @FXML
    Button btnSave;
    @FXML
    Button btnCancel;
    @FXML
    Button btnGenerate;
    @FXML
    Button btnDelete;
    @FXML
    Button btnAddRecord;
    @FXML
    Button btnRefreshrecord;
    @FXML
    Button btnDeleteRecord;

    /**
     * tableview and columns
     **/
    @FXML
    TableView<Record> tbvRecords;
        @FXML
        TableColumn<Record, LocalDate> tbcRecDate   ;
        @FXML
        TableColumn<Record, String> tbcRecID;
        @FXML
        TableColumn<Record, String> tbcCourseName;
        @FXML
        TableColumn<Record, Integer> tbcGrade;
        @FXML
        TableColumn<Record, Integer> tbcCredits;
        @FXML
        TableColumn<Record, String> tbcCourseID;

        /**context menu**/
        @FXML
        ContextMenu cxmRecordsMenu;
            @FXML
            MenuItem miRecordsViewCourse;
            @FXML
            MenuItem miRecordsDeleteThisRecord;

    /**main method
     * @param args =..
     * **/
    public static void main(String[] args) {
        launch(args);
    }

    /**main method**/
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("GUIstudentEditView"));
        primaryStage.setTitle("Edit/View Student");
        primaryStage.setScene(new Scene(root,1000, 1000));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
    
    /**Initialise method handles procedures
     * according to mode set with mode booleans
     * depending on mode, certain elements are hidden
     * and certain set visible
     * **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        if (editMode) {
            getStudent();
            populateRecords();
            btnGenerate.setVisible(false);
            btnAddRecord.setVisible(true);
        } else {
            btnEditInfo.setVisible(false);
            enableFields(new ActionEvent());
            btnAddRecord.setVisible(false);
            btnRefreshrecord.setVisible(false);
            btnDeleteRecord.setVisible(false);
            txfID.setText(nextStudentID);
        }

        btnDelete.setVisible(false);

        if(deleteMode){
            getStudent();
            populateRecords();
            btnDelete.setVisible(true);
            enableFields(new ActionEvent());
            btnGenerate.setVisible(false);
            btnEditInfo.setVisible(false);
            btnSave.setVisible(false);
            btnAddRecord.setVisible(false);
            btnRefreshrecord.setVisible(false);
        }
    }

    /**
     * method to populate text fields on load
     **/
    @FXML
    private void getStudent() {
        System.out.println("Handling student: "+handledStudent.toString());
        Student toBeEdited = handledStudent;
        System.out.println("Student to be edited:"+toBeEdited.toString());
        txfID.setText(toBeEdited.getStudentID());
        txfFirstName.setText(toBeEdited.getFirstName());
        txfLastName.setText(toBeEdited.getLastName());
        txfAddress.setText(toBeEdited.getAddress());
        txfZIP.setText(toBeEdited.getZIP());
        txfZIPloc.setText(toBeEdited.getZIPloc());
        txfEmail.setText(toBeEdited.getEmail());
        txfPhone.setText(toBeEdited.getPhone());
    }

    /**
     *  method to get student record data from database
     *  and set a listener for doubleclicks on mouse
     **/
    @FXML
    public void populateRecords() {
        DBhandler db = new DBhandler();

        tbvRecords.getItems().clear();
        tbvRecords.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        DBhandler dbh = new DBhandler();
        System.out.println("\t >> trying to populate course table");

        tbcRecDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
        tbcRecID.setCellValueFactory(new PropertyValueFactory<>("RecordID"));
        tbcCourseName.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
        tbcCredits.setCellValueFactory(new PropertyValueFactory<>("Credits"));
        tbcGrade.setCellValueFactory(new PropertyValueFactory<>("Grade"));
        tbcCourseID.setCellValueFactory(new PropertyValueFactory<>("CourseID"));

        System.out.println("\t >> table prepped. Getting courses...");

       ObservableList<Record> a = dbh.loadStudentsRecordData(handledStudent.getStudentID());
        System.out.println("\t >> Course retrieval done. Populating course table...");

        tbvRecords.getItems().addAll(a);

        System.out.println("\t >> Course table populated.");

        tbvRecords.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ) {
                    callCourseEditPageFromStudentEditPage();
                }
            }
        });
    }

    /**this method enables and disables
     * textfields for editing purposes
     * if fields are disabled it is not possible to save data
     *
     * **/
    @FXML
    private void enableFields(ActionEvent e) {
        if (txfFirstName.isDisabled()) {
            txfFirstName.setDisable(false);
            txfLastName.setDisable(false);
            txfAddress.setDisable(false);
            txfZIP.setDisable(false);
            txfZIPloc.setDisable(false);
            txfEmail.setDisable(false);
            txfPhone.setDisable(false);
        } else{
            txfFirstName.setDisable(true);
            txfLastName.setDisable(true);
            txfAddress.setDisable(true);
            txfZIP.setDisable(true);
            txfZIPloc.setDisable(true);
            txfEmail.setDisable(true);
            txfPhone.setDisable(true);
        }
    }

    /**
     * method to cancel editing and close stage
     **/
    @FXML
    private void Cancel(ActionEvent e) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.hide();
        stage.close();
    }

    /**
     *  method to update edited information to database
     **/


    /**
     * method to insert new information to database
     * this method is invoked after user
     * has confirmed the insert by pressing ok
     * button
     * after insert stage is closed
     **/
    @FXML
    private void newStudent() {
        System.out.println("trying to insert new student to database");
        DBhandler db = new DBhandler();
        Student s = new Student(
                txfID.getText(),
                txfFirstName.getText(),
                txfLastName.getText(),
                txfAddress.getText(),
                txfZIP.getText(),
                txfZIPloc.getText(),
                txfEmail.getText(),
                txfPhone.getText()
                );
        System.out.println(s.toString());
        db.insertStudent(s);
        Cancel(new ActionEvent());
    }
    
    /**
     *  method to update edited information to database
     *  after update the stage is closed
     **/
    @FXML
    private void updateStudent() {
        System.out.println("trying to insert new student to database");
        DBhandler db = new DBhandler();
        Student s = new Student(
                txfID.getText(),
                txfFirstName.getText(),
                txfLastName.getText(),
                txfAddress.getText(),
                txfZIP.getText(),
                txfZIPloc.getText(),
                txfEmail.getText(),
                txfPhone.getText()
        );
        System.out.println(s.toString());
        db.updateStudentToDatabase(s);
        RecordEditPage.handledStudent=s;
        Cancel(new ActionEvent());
    }

    /**this method automatically generates data to textfields
     * enabling faster testing of the app
     * **/
    @FXML
    private void generateData(ActionEvent e) {
        Generator generate = new Generator();
        txfFirstName.setText(generate.firstName());
        txfLastName.setText(generate.lastName());
        txfAddress.setText(generate.address());
        txfZIP.setText(generate.ZIP());
        txfZIPloc.setText(generate.ZIPloc());
        txfEmail.setText(generate.email());
        txfPhone.setText(generate.phone());
    }

    /**this method requires user to confirm the
     * inserting of new student data
     * after pressing ok -button the insert clause is handled
     * by invoking the newStudent -method
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    public void confirmStudentInsert() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String title ="Confirm inserted data";
        String HeaderText = "Confirm inserted data";
        String ContentText = "Are you sure you want to insert a new student?";

        AlertHandler ah = new AlertHandler();

        if (ah.getConfirmation(title, HeaderText, ContentText) == ButtonType.OK) {
            newStudent();
            Method m = MainOR.getInstance().getClass().getDeclaredMethod("callStudentData", ActionEvent.class);
            m.invoke(MainOR.getInstance(),new ActionEvent());
            System.out.println("Ok pressed");
        } else {
            System.out.println("Cancel pressed");
        }
    }
    
    /**this method requires user to confirm the
     * updating of  student data
     * after pressing ok -button the update clause is handled
     * by invoking the newStudent -method
     * **/
    public void confirmStudentUpdate(){

        String title ="Confirm student information update";
        String HeaderText = "Confirm inserted data";
        String ContentText = "Are you sure you want to update this student's information?";

        AlertHandler ah = new AlertHandler();

        if (ah.getConfirmation(title, HeaderText, ContentText) == ButtonType.OK) {
            System.out.println("Ok pressed");
            updateStudent();
        } else {
            System.out.println("Cancel pressed");
        }
    }

    /**this method handles the action of pressing the save button
     * according to the current mode setting of the page
     * it also alerts user if fields have not been enabled for
     * editing
     * if editmode = true, handled as update
     * if editmode = false, handled as insert
     * @param e = actionevent from button
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    public void StudentSaveButtonPressed(ActionEvent e) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AlertHandler ah = new AlertHandler();
        if (editMode){
            System.out.println("edit mode on: choosing student update");
            if(txfFirstName.isDisabled()){
                ah.getError("Please edit student information", "Enable Student editing fields for saving", "Please edit before saving");
                System.out.println("please edit data before saving to database");
            } else {
                System.out.println("requesting confirmation for editing data to database");
                confirmStudentUpdate();
            }
        }
        else {
            System.out.println("Edit mode off: choosing student insert");
            confirmStudentInsert();
        }
    }

    /**this method handles the action of pressing the delete button
     *requires confirmation of user to save data to database
     * @param e = actionevent from button
     * **/
    @FXML
    public void confirmStudentDelete(ActionEvent e) {
        Student s = handledStudent;
        DBhandler db = new DBhandler();
        String title ="Confirm delete";
        String HeaderText = "Student will be permanently deleted";
        String ContentText = "Are you sure you want to delete this student?";

        AlertHandler ah = new AlertHandler();

        if (ah.getConfirmation(title, HeaderText, ContentText) == ButtonType.OK) {
            System.out.println("Ok pressed");
            db.deleteStudentFromDatabase(s);
            Cancel(new ActionEvent());
        } else {
            System.out.println("Cancel pressed");
        }
    }

    /**
     * method calls course select page
     * in order to add a record for the student
     *
     * **/
    @FXML
    private void callCourseSelectPage(ActionEvent e)  {

        CourseSelectPage.selectForRecordMode= false;

        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIcourseSelectPage");
        Stage stage = new Stage();
        stage.setTitle("Add record for student");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**this method handles the deletion of
     * multiple courses from student
     * **/
    @FXML
    private void confirmDeletionOfRecords(){
        ObservableList<Record> recordsToBeDeleted = tbvRecords.getSelectionModel().getSelectedItems();

        DBhandler db = new DBhandler();
        String title ="Confirm delete";
        String HeaderText = "Record(s) will be permanently deleted";
        String ContentText = "Are you sure you want to delete this data?";
        AlertHandler ah = new AlertHandler();

        if(recordsToBeDeleted.isEmpty()){
            ah.getTableError();
        }
        else{
        if (ah.getConfirmation(title, HeaderText, ContentText) == ButtonType.OK) {
            System.out.println("Ok pressed");
            db.deleteRecordsFromDatabase(recordsToBeDeleted);
            populateRecords();
        } else {
            System.out.println("Cancel pressed");
        }
    }}
    
    /**method to navigate to
     * course edit page from student edit page**/
    @FXML
    private void callCourseEditPageFromStudentEditPage()  {

        DBhandler db = new DBhandler();

        CourseEditPage.editMode = true;
        CourseEditPage.deleteMode = false;
        Record r = tbvRecords.getSelectionModel().getSelectedItem();
       try {
           CourseEditPage.handledCourse = db.getCourseByID(r.getCourseID());
           Pageloader loader = new Pageloader();
           Parent root = loader.getPage("GUIcourseEditView");
           Stage stage = new Stage();
           stage.setTitle("View / Edit Course");
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.setScene(new Scene(root));
           stage.showAndWait();

       } catch (NullPointerException ex) {
           new AlertHandler().getTableError();
        }
    }

    /**method to get instance of this class
     * for invoking its methods outside the student edit page
     * @return = instance of this object
     **/
    public static StudentEditPage getInstance(){
        return instance;
    }
}
