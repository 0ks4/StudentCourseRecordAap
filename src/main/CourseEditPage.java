/**
 * this class is used when users navigate
 * to edit course information in the program
 * the page has two modes:
 * edit mode - where all the necessary elements
 * to make changes to courses are shown to the user
 * this includes buttons and labels
 * Delete mode - where certain elements of the
 * edit mode are hidden and necessary elements
 * to delete mode are shown.
 * the modes are controlled via static boolean
 * which is set in the calling method of this page
 * when navigating to this page
 * **/

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
import java.util.*;

/**
 *
 * @author Asko
 */
public class CourseEditPage extends Application implements Initializable {
    /**course instance that will be viewed or edited
     * this is needed when calling this page's
     * methods from outside of this page
     * **/
    private static CourseEditPage instance;

    /**
     * when navigating to this page this attribute
     * is defined as the course, that is to be edited
     * this definition is handled in the methods where
     * you navigate to this page
     * **/
    static Course handledCourse;

    /**
     * mode booleans are used
     * when user navigates to edit course information
     * editmode is set to true and delete mode to false
     * and vice versa when deleting information
     * when deleting course information, all record information
     * will be deleted as well
     * **/
    static boolean editMode = false;
    static boolean deleteMode = false;

    static String nextCourseID;

    /**Textfields**/
    @FXML
    private TextField txfCourseID;
    @FXML
    private TextField txfName;
    @FXML
    private TextField txfCredits;
    @FXML
    private TextField txfFaculty;
    @FXML
    private TextArea txaDescription;

    /**Buttons**/
    @FXML
    private Button btnEditInfo;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnGenerate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAddRecordC;
    @FXML
    private Button btnRefreshC;
    @FXML
    private Button btnDeleteRecordsC;

    /**
     * context menu for the tableview element
     * **/
    @FXML
    ContextMenu ctxMenuCourseEdit;
    @FXML
    MenuItem mniViewStudent;
    @FXML
    MenuItem mniDeleteRecord;

    /**tableview and columns**/
    @FXML
    private TableView <Record> tbvCourseRecords;
    @FXML
    private TableColumn <Record, Date>tbcRecDate;
        @FXML
        private TableColumn <Record, String>tbcRecID;
        @FXML
        private TableColumn <Record, String> tbcStudentName;
        @FXML
        private TableColumn <Record, Integer> tbcCredits;
        @FXML
        private TableColumn <Record, Integer> tbcGrade;
        @FXML
        private TableColumn <Record, String> tbcCourseID;

    /**main method
     * @param args = ...**/
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("GUIcourseEditView.fxml"));
        primaryStage.setTitle("Edit/View Course");
        primaryStage.setScene(new Scene(root,600,600));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }
    /**initialize method checks the mode booleans and
     * sets page elements visible or invisible
     * depending on from where the user is navigating to this page
     * **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        if(editMode){
            getCourse();
        populateRecords();
        btnGenerate.setVisible(false);
        } else {
            btnEditInfo.setVisible(false);
            enableFields(new ActionEvent());
            txfCourseID.setText(nextCourseID);
            btnAddRecordC.setVisible(false);
            btnRefreshC.setVisible(false);
            btnDeleteRecordsC.setVisible(false);
        }

        btnDelete.setVisible(false);

        if(deleteMode){
            getCourse();
            populateRecords();
            btnDelete.setVisible(true);
            enableFields(new ActionEvent());
            btnGenerate.setVisible(false);
            btnEditInfo.setVisible(false);
            btnSave.setVisible(false);
        }
    }

    /** method to populate textfields with course information
     * doesnt use any parameters
     * and doesnt have a return value
     * **/
    @FXML
    public void getCourse(){
        Course toBeEdited = handledCourse;
        txfCourseID.setText(toBeEdited.getCourseID());
        txfName.setText(toBeEdited.getName());
        txfCredits.setText(""+toBeEdited.getCredits());
        txfFaculty.setText(toBeEdited.getFaculty());
        txaDescription.setText(toBeEdited.getDescription());
    }

    /** method to populate tableview with course records
     * prepares the tableview by clearing it
     * and setting up the columns and setting selection mode
     * to multiple row -selection mode. When this is done
     * uses an instance of DBhandler class to get data
     * from database using DBhandlers query methods
     * finally sets an action listener for mouse double clicks
     * doesnt use any parameters
     * and doesnt have a return value
     * **/
    @FXML
    public void populateRecords(){

        tbvCourseRecords.getItems().clear();
        tbvCourseRecords.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        DBhandler dbh = new DBhandler();
        System.out.println("\t >> trying to populate course table");

        tbcRecDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
        tbcRecID.setCellValueFactory(new PropertyValueFactory<>("RecordID"));
        tbcStudentName.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        tbcCredits.setCellValueFactory(new PropertyValueFactory<>("Credits"));
        tbcGrade.setCellValueFactory(new PropertyValueFactory<>("Grade"));
        //tbcCourseID.setCellValueFactory(new PropertyValueFactory<>("CourseID"));

        System.out.println("\t >> table prepped. Getting courses...");

        ObservableList<Record> a = dbh.loadCoursesRecordData(handledCourse.getCourseID());
        System.out.println("\t >> Course retrieval done. Populating course table...");

        tbvCourseRecords.getItems().addAll(a);

        System.out.println("\t >> Course table populated.");

        tbvCourseRecords.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ) {
                    callStudentEditPageFromCourseEditPage();
                }
            }
        });
    }

    /**method to enable textfields for editing
     * this method is used by btnEditInfo -element
     * and it switches the textfields between enabled and disabled
     * the page wont save data when these fields are disabled
     * user is informed when this is attempted with an alert
     * this is handled in the CourseSaveButtonPressed -method
     * **/
    @FXML
    private void enableFields(ActionEvent e){
       if(txfName.isDisabled()) {
            txfName.setDisable(false);
            txfCredits.setDisable(false);
            txfFaculty.setDisable(false);
            txaDescription.setDisable(false);
       } else {
           txfName.setDisable(true);
           txfCredits.setDisable(true);
           txfFaculty.setDisable(true);
           txaDescription.setDisable(true);
       }
    }

    /** method to cancel editing and close stage **/
    @FXML
    private void Cancel(ActionEvent e){
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.hide();
        stage.close();
    }
    
    /**
     * this method is used to generate random data
     * to textfields using an instance of the
     * generator -class and its methods
     * **/
    @FXML
    private void generateData(ActionEvent e){

        Random r = new Random();
        Generator generate = new Generator();
        txfName.setText(generate.courseName());
        txfCredits.setText(""+(r.nextInt(5)+1));
        txaDescription.setText(generate.description());
        txfFaculty.setText(generate.faculty());
    }

    /** method to insert new information to database
     * constructs a course from textfield
     * information and uses dbhandler to
     * insert the data in to the database
     * closes the course editing page and calls
     * main program pages coursepage tableview element
     * to refresh information
     * no parameters, no return value
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     *
     * **/
    public void newCourse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Integer credits = 0;
        DBhandler db = new DBhandler();
        Course c = new Course(
                txfCourseID.getText(),
                txfName.getText(),
                credits.parseInt(txfCredits.getText()),
                txaDescription.getText(),
                txfFaculty.getText()
        );
        db.insertCourse(c);
        Cancel(new ActionEvent());
        Method m = MainOR.getInstance().getClass().getDeclaredMethod("callCourseData");
        m.invoke(MainOR.getInstance());
    }

    /** method to update edited information to database
     * works similarly to insert method
     * but uses dbhandlers update clause -method instead
     * and doesnt close the course editing page
     * no parameters, no return value
     * **/
    @FXML
    public void updateCourse() {
        Integer credits = 0;
        DBhandler db = new DBhandler();
        System.out.println("trying to update student to database");

            Course c = new Course(
                    txfCourseID.getText(),
                    txfName.getText(),
                    credits.parseInt(txfCredits.getText()),
                    txaDescription.getText(),
                    txfFaculty.getText()
            );
            System.out.println(c.toString());
            db.updateCourseToDatabase(c);
            RecordEditPage.handledCourse = c;
            Cancel(new ActionEvent());
    }

    /**
     * method to handle the save button event
     * this method checks the pages mode booleans
     * and runs necessary procedures regarding the mode
     * also if trying to save information while the textfields are disabled
     * gives error to user when this is attempted
     * after mode checking and error handling is done the method uses the
     * right dbhandler method according to page mode
     * @param e = actionevent from button
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    public void CourseSaveButtonPressed(ActionEvent e) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AlertHandler ah = new AlertHandler();

            if (editMode) {
                if (txfName.isDisabled()) {
                    ah.getError("Course information was not edited", "Please enable course information fields for editing before saving", "Please edit before saving");
                    System.out.println("please edit data before saving to database");
                } else {
                    System.out.println("requesting confirmation for editing data to database");
                    try {
                    confirmCourseUpdate();
                    }catch(NumberFormatException exce){

                        new AlertHandler().getDataTypeError();
                    }
                }
            } else {
                System.out.println("Edit mode off: choosing course insert");
                try {
                confirmCourseInsert();
                }catch(NumberFormatException exce){

                    new AlertHandler().getDataTypeError();
            }
        }
    }

    /** method to confirm course inserting to database
     * after save button is pressed a confirmation is required from user
     * AlertHandler classes method is used to get confirmation from user
     * if confirmed by pressing ok-button newCourse -method is called
     * else operation is cancelled
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    public void confirmCourseInsert() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("waiting for confirmation");
        String title ="Confirm inserted data";
        String HeaderText = "Save course information to database";
        String ContentText = "Are you sure you want to insert a new course?";

        AlertHandler ah = new AlertHandler();

        if (ah.getConfirmation(title,HeaderText, ContentText) == ButtonType.OK) {
            newCourse();
            System.out.println("Ok pressed");
        } else {
            System.out.println("Cancel pressed");
        }
    }

    /** method to confirm course updating to database
     * after save button is pressed a confirmation is required from user
     * AlertHandler classes method is used to get confirmation from user
     * if confirmed by pressing ok-button updateCourse -method is called
     * else operation is cancelled
     *
     * **/
    public void confirmCourseUpdate(){

        String title ="Confirm student information update";
        String HeaderText = "Confirm inserted data";
        String ContentText = "Are you sure you want to update this student's information?";

        AlertHandler ah = new AlertHandler();

        if (ah.getConfirmation(title, HeaderText, ContentText) == ButtonType.OK) {
            System.out.println("Ok pressed");
            updateCourse();
        } else {
            System.out.println("Cancel pressed");
        }
    }

    /** method to confirm course deleting to database
     * after delete button is pressed a confirmation is required from user
     * AlertHandler classes method is used to get confirmation from user
     * if confirmed by pressing ok-button  dbhandlers deleting method is called
     * else operation is cancelled
     *
     * @param e = actionevent from button
     * **/
    @FXML
    public void confirmCourseDelete(ActionEvent e) {
        Course c = handledCourse;
        DBhandler db = new DBhandler();
        String title ="Confirm delete";
        String HeaderText = "Course will be permanently deleted";
        String ContentText = "Are you sure you want to delete this course?";

        AlertHandler ah = new AlertHandler();

        if (ah.getConfirmation(title, HeaderText, ContentText) == ButtonType.OK) {
            System.out.println("Ok pressed");
            db.deleteCourseFromDatabase(c);
            Cancel(new ActionEvent());
        } else {
            System.out.println("Cancel pressed");
        }
    }

    /**this method is used to navigate from course editing page
     * to student editing page. When doing so, the student page-mode
     * is selected to edit mode and the student edit pages
     * static Student attribute is updated from database by
     * using a dbhandlers query method to get student from
     * database by student id.
     * after this an instance of pageloader class loads the page
     * and shows it to user on a new stage
     * **/
    @FXML
    private void callStudentEditPageFromCourseEditPage()  {

        DBhandler db = new DBhandler();

        StudentEditPage.editMode = true;
        StudentEditPage.deleteMode = false;
        Record r = tbvCourseRecords.getSelectionModel().getSelectedItem();
        try {
            StudentEditPage.handledStudent = db.getStudentByID(r.getStudentID());
            Pageloader loader = new Pageloader();
            Parent root = loader.getPage("GUIstudentEditView");
            Stage stage = new Stage();
            stage.setTitle("View / Edit Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (NullPointerException ex) {
            new AlertHandler().getTableError();
        }
    }

    /**this method is used to navigate to
     * Student select page when adding records via
     * course editing page. Student selection page's
     * mode for select for record is set false
     * since at this stage the user is not navigating
     * from record view
     * **/
    @FXML
    private void callStudentSelectPage(ActionEvent e){
        StudentSelectPage.selectForRecordMode=false;
        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIstudentSelectPage");
        Stage stage = new Stage();
        stage.setTitle("Add record for Course");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /** method to confirm record deleting to database
     * after delete record button is pressed a confirmation is required from user
     * AlertHandler classes method is used to get confirmation from user
     * if confirmed by pressing ok-button  dbhandlers deleting method is called
     * else operation is cancelled
     * **/
    @FXML
    private void confirmDeletionOfRecords(){

        ObservableList<Record> recordsToBeDeleted = tbvCourseRecords.getSelectionModel().getSelectedItems();

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
        }}

    /**this method is used to get this pages
     * object instance, which is required
     * when refreshing tableview information
     * after data has been inserted, updated or deleted
     * **/
    }
    public static CourseEditPage getInstance() {
        return instance;
    }    
}
