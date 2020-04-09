/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Asko
 */
public class StudentSelectPage extends Application implements Initializable {
    /**
     * @param selectForRecordMode = mode when navigating from record editing page to this page, this is set true
     * @param pageToBeUpDatedWhenSelectingNewStudent =
     * @param gradeList = list of grade items as integers, used for choicebox
     *
     * **/
    static boolean selectForRecordMode=false;
    static Class pageToBeUpDatedWhenSelectingNewStudent;
    ObservableList<Integer> gradeList = FXCollections.observableArrayList( 1, 2, 3, 4, 5);

    /**tableview for this page**/
    @FXML
    TableView<Student> tbvStudentSelect;
        @FXML
        private TableColumn<Student, String> tbcStudentID;
        @FXML
        private TableColumn<Student, String> tbcFirstName;
        @FXML
        private TableColumn<Student, Integer> tbcLastName;

    /**choicebox for grades*/
    @FXML
    ChoiceBox cbxGradeC;

    /**buttons*/
    @FXML
    Button btnSaveRecordC;
    @FXML
    Button btnCancelC;

    /**textfields*/
    @FXML
    TextField txfCourseIDC;
    @FXML
    TextField txfCreditsC;

    /**datepicker*/
    @FXML
    DatePicker dtpDatePickerC;


    /***labels*/
    @FXML
    Label lblStudentGrade;
    @FXML
    Label lblCompletionDate;
    @FXML
    Label lblInstruction;

    @FXML
    Label lblAddRecordTo;

    /**main method
     * @param args =..
     * */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    /***initialize method
     * according to mode certain elements are shown or hidden from user
     * */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txfCourseIDC.setText(CourseEditPage.handledCourse.getCourseID());
        txfCreditsC.setText("" + CourseEditPage.handledCourse.getCredits());
        callStudentData();
        if (selectForRecordMode){
            btnSaveRecordC.setText("Select this student");
            cbxGradeC.setVisible(false);
            dtpDatePickerC.setVisible(false);
            lblStudentGrade.setVisible(false);
            lblCompletionDate.setVisible(false);
            lblAddRecordTo.setVisible(false);
            lblInstruction.setText("Please select a new student for RecordID: "+RecordEditPage.handledRecord.getRecordID());
        }
            else {
            cbxGradeC.setItems(gradeList);
            cbxGradeC.setValue(null);
        }
    }

    /** method to populate tableview with student records
     * prepares the tableview by clearing it
     * and setting up the columns and setting selection mode
     * to multiple row -selection mode. When this is done
     * uses an instance of DBhandler class to get data
     * from database using DBhandlers query methods
     * finally sets an action listener for mouse double clicks
     * doesnt use any parameters
     * and doesnt have a return value
     * **/
    public void callStudentData() {

        tbvStudentSelect.getItems().clear();

        DBhandler dbh = new DBhandler();
        System.out.println("\t >> trying to populate course table");

        tbcStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        tbcFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tbcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        System.out.println("\t >> table prepped. Getting courses...");

        ObservableList<Student> a = dbh.loadAvailableStudentsForCourse(CourseEditPage.handledCourse);
        System.out.println("\t >> Course retrieval done. Populating course table...");

        tbvStudentSelect.getItems().addAll(a);

        System.out.println("\t >> student table populated.");

        tbvStudentSelect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ) {
                    try {
                        saveButtonPressed(new ActionEvent());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**method to add a record for a student
     * user selects a course, a grade and a date
     * from according elements
     * **/
    @FXML
    public void addSelectedRecordForCourse() {
        DBhandler db = new DBhandler();
        AlertHandler ah = new AlertHandler();

        String title = "confirm operation";
        String header = " Course and Student will receive a record";
        String content = "Are you sure you want to add record?";
        Student s = tbvStudentSelect.getSelectionModel().getSelectedItem();
        Record r = null;

        try {
            Object grade = cbxGradeC.getValue();
            int intgrade = (Integer) grade;
            r = new Record(
                    dtpDatePickerC.getValue(),
                    intgrade,
                    s.getStudentID(),
                    CourseEditPage.handledCourse.getCourseID(),
                    db.getNewRecordID(),
                    CourseEditPage.handledCourse.getName(),
                    s.getName(),
                    CourseEditPage.handledCourse.getCredits());
            if (ah.getConfirmation(title, header, content) == ButtonType.OK) {
                db.insertRecord(r);
            } else {
                System.out.println("Record was not added");
            }
        } catch (NullPointerException exception) {
            ah.getError("Incomplete Record", "Please add all information", "Please Select a student from the table, a grade from the dropdown menu, and a date from the datepicker");
            callStudentData();
        }
        callStudentData();
        cbxGradeC.setValue(null);
    }

    /**
     * method to handle the save button pressing
     * if user is coming from record edit page
     * the selected student will be used to populate record edit page fields
     * if user is adding a record for a course, a database action will be handled
     * @param e = actionevent from button
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    public void saveButtonPressed(ActionEvent e) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DBhandler db = new DBhandler();
        try {
            if (selectForRecordMode) {
                Student s = db.getStudentByID(tbvStudentSelect.getSelectionModel().getSelectedItem().getStudentID());
                RecordEditPage.handledStudent = s;
                System.out.println(RecordEditPage.handledStudent.toString());
                Method m = RecordEditPage.getInstance().getClass().getDeclaredMethod("populateStudentFields");
                m.invoke(RecordEditPage.getInstance());
                Cancel(new ActionEvent());
            }
            else {
                addSelectedRecordForCourse();
            }
        } catch (NullPointerException exce) {
            new AlertHandler().getTableError();
        }
    }

    /**
     * method to close stage according to mode
     * stage will be closed in both modes,
     *  but if user is coming from
     *  record edit page, a method
     *  will be invoked to refresh record edit
     *  page's textfields
     * **/
    @FXML
    private void Cancel (ActionEvent e)  {
        System.out.print(selectForRecordMode);
        Stage stage = (Stage) btnCancelC.getScene().getWindow();
        stage.hide();
        stage.close();
        try {
            if (selectForRecordMode ==false) {
                Method m = CourseEditPage.getInstance().getClass().getDeclaredMethod("populateRecords");
                m.invoke(CourseEditPage.getInstance());
                selectForRecordMode = false;
                System.out.println("tried to populate course edit page tableview");
            }
        }catch(NoSuchMethodException ne){
            ne.printStackTrace();
        }
        catch(InvocationTargetException ie) {
            ie.printStackTrace();
        }
        catch(IllegalAccessException ae){
            ae.printStackTrace();

        }
    }    
}
