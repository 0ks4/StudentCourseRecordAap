/**this class is used to handle all events and interactions of the database and the user
 * like the course editing page, it has two modes, one for editing
 * and the other for deleting
 * ui-elements are shown or hidden according to use mode
 * **/
package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
/**
 *
 * @author Asko
 */
public class RecordEditPage extends Application implements Initializable {
    /**
     * @param instance = is used to get the object instance of this page, for invoking its methods
     * @param handledRecord = when navigating to this page, this object is defined when picking
     *                      the record for editing
     * @param handledStudent = the student information for this record is acquired through
     *                       this object, defined from recordpage StudentID, with sql -query
     * @param handledcourse = the student information for this record is acquired through
     *                         this object, defined from recordpage StudentID, with sql -query
     * @param gradelist = the observable list of contents required for the grade -choicebox
     *
     * **/
    private static RecordEditPage instance;
    static Record handledRecord;
    static Student handledStudent;
    static Course handledCourse;

    static ObservableList<Integer> gradeList = FXCollections.observableArrayList(1, 2, 3, 4, 5);

    /**textfield for recordID, datepicker and choicebox for grades**/
    @FXML
    protected TextField txfRrecordID;
    @FXML
    protected DatePicker dprRdate;
    @FXML
    protected ChoiceBox cbxRgrades;

    /**textfields**/
    @FXML
    protected TextField txfRStudentID;
    @FXML
    protected TextField txfRfirstName;
    @FXML
    protected TextField txfRlastName;
    @FXML
    protected TextField txfRaddress;
    @FXML
    protected TextField txfRZIP;
    @FXML
    protected TextField txfRZIPLoc;
    @FXML
    protected TextField txfRemail;
    @FXML
    protected TextField txfRphone;

    @FXML
    protected TextField txfRcourseID;
    @FXML
    protected TextField txfRcredits;
    @FXML
    protected TextField txfRcourseName;
    @FXML
    protected TextField txfRfaculty;

    /**textarea for course description**/
    @FXML
    protected TextArea txaRdescription;

    /**buttons**/
    @FXML
    protected Button btnReditRecord;
    @FXML
    protected Button btnReditStudent;
    @FXML
    protected Button btnRchangeStudent;
    @FXML
    protected Button btnReditCourse;
    @FXML
    protected Button btnRchangeCourse;
    @FXML
    protected Button btnRrefreshRecord;
    @FXML
    protected Button btnRdeleteRecord;
    @FXML
    protected Button btnRcancel;
    @FXML
    protected Button btnRsave;

    /**main method
     * @param args =..
     * **/
    public static void main(String[] args) {
        launch(args);
    }
    
    /**start method**/
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("GUIrecordEditView"));
        primaryStage.setTitle("Edit/View Record");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

    /**initialize method
     * adds choicebox items from gradelist
     * defines this objects instance
     * and populates all textfields
     *
     * **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbxRgrades.setItems(gradeList);
        instance = this;
        populateFields();
    }

    /**
     * populating all fields, datepicker, and gradepicker
     **/
    public void populateFields() {
        populateRecordFields();
        populateStudentFields();
        populateCourseFields();
    }

    /**
     * populating student information
     **/
    public void populateStudentFields() {
        txfRStudentID.setText(handledStudent.getStudentID());
        txfRfirstName.setText(handledStudent.getFirstName());
        txfRlastName.setText(handledStudent.getLastName());
        txfRaddress.setText(handledStudent.getAddress());
        txfRZIP.setText(handledStudent.getZIP());
        txfRZIPLoc.setText(handledStudent.getZIPloc());
        txfRemail.setText(handledStudent.getEmail());
        txfRphone.setText(handledStudent.getPhone());
    }

    /**
     * populating course information
     **/
    public void populateCourseFields() {
        txfRcourseID.setText(handledCourse.getCourseID());
        txfRcredits.setText("" + handledCourse.getCredits());
        txfRcourseName.setText(handledCourse.getName());
        txfRfaculty.setText(handledCourse.getFaculty());
        txaRdescription.setText(handledCourse.getDescription());
    }

    /**
     * populating record information
     */
    public void populateRecordFields() {
        txfRrecordID.setText(handledRecord.getRecordID());
        dprRdate.setValue(handledRecord.getRecordDate());
        cbxRgrades.setValue(handledRecord.getGrade());
    }

    /**
     *  method to enable record field editing
     * @param e = button event
     **/
    public void enableRecordFieldsForEditing(ActionEvent e) {
        if (cbxRgrades.isDisable()) {
            cbxRgrades.setDisable(false);
            dprRdate.setDisable(false);
        } else {
            cbxRgrades.setDisable(true);
            dprRdate.setDisable(true);
        }
    }

    /**
     *  method to change student for this record
     * @param e = actionevent from button
     **/
    public void changeStudentForThisRecord(ActionEvent e) {
        StudentSelectPage.selectForRecordMode = true;
        CourseEditPage.handledCourse = RecordEditPage.this.handledCourse;
        StudentSelectPage.pageToBeUpDatedWhenSelectingNewStudent = this.getClass();

        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIstudentSelectPage");
        Stage stage = new Stage();
        stage.setTitle("Select a new student for the Record");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**
     * method to change Course for inspected record in record edit page
     * calls course select page in select for record mode on
     * @param e = actionevent from button
     **/
    public void changeCourseForThisRecord(ActionEvent e) {
        CourseSelectPage.selectForRecordMode = true;
        StudentEditPage.handledStudent = RecordEditPage.this.handledStudent;

        Pageloader loader = new Pageloader();
        Parent root = loader.getPage("GUIcourseSelectPage");
        Stage stage = new Stage();
        stage.setTitle("Select a new Course for the Record");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**
     * method to go to Student edit page from record page
     * calls student select page in select for record mode on
     * @param e = actionevent from button
     **/
    public void callStudentEditPageFromRecords(ActionEvent e) {
        StudentEditPage.handledStudent = RecordEditPage.this.handledStudent;
        StudentEditPage.editMode = true;
        StudentEditPage.deleteMode = false;
        try {
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

    /**
     *  method to go to Course edit page from record page
     *  navigates to course edit page in edit mode
     * @param e = actionevent from button
     **/
    public void callCourseEditPageFromRecords(ActionEvent e) {
        CourseEditPage.handledCourse = RecordEditPage.this.handledCourse;

        CourseEditPage.editMode = true;
        CourseEditPage.deleteMode = false;
        try {
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

    /**
     * this method asks for confirmation from user
     * and then saves data to database via DBhandler
     * closes page after saving
     * @param e = actionevent from button
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    public void ConfirmSaveAllData(ActionEvent e) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DBhandler db = new DBhandler();
        AlertHandler ah = new AlertHandler();

        String title = "Confirm Record update";
        String header = "All changes will be saved to database";
        String content = "Are you sure you want to save all changes";

        Record r = new Record(
                dprRdate.getValue(),
                (int) cbxRgrades.getValue(),
                txfRStudentID.getText(),
                txfRcourseID.getText(),
                txfRrecordID.getText(),
                txfRcourseName.getText(),
                handledStudent.getName(),
                handledCourse.getCredits()
        );
        if (ah.getConfirmation(title, header, content) == ButtonType.OK) {
            db.updateRecord(r);
            Cancel(new ActionEvent());
        }
    }
    
    /**
     * closes the recordeditpage and calls a refresh
     * for the recordpage tableview
     * @param e = actionevent from button
     * @throws NoSuchMethodException = ..
     * @throws InvocationTargetException = ..
     * @throws IllegalAccessException = ..
     * **/
    @FXML
    private void Cancel(ActionEvent e) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Stage stage = (Stage) btnRcancel.getScene().getWindow();
        stage.hide();
        stage.close();
        Method m = MainOR.getInstance().getClass().getDeclaredMethod("callRecordData", ActionEvent.class);
        m.invoke(MainOR.getInstance(),new ActionEvent());
    }

    /**this method returns the object instance of this page
     * for use of this pages methods outside this page
     * it is used for refreshing the elements
     * after inserts or updates
     * @return = instance of this page
     * **/
    public static RecordEditPage getInstance() {
        return instance;
    }

    /**this method deletes the record at hand
     * @param e = actionevent from button
     * **/
    @FXML
    public void deleteRecord(ActionEvent e) {
        try {
            String title = "Delete in progress";
            String header = "This operation will delete the selected record";
            String confirm = "Are you sure you wish to proceed? ";
            AlertHandler ah = new AlertHandler();
            DBhandler db = new DBhandler();

            if (ah.getConfirmation(title, header, confirm) == ButtonType.OK) {
                db.deleteRecordByID(txfRrecordID.getText());
                Cancel(new ActionEvent());
            } else {
                ah.getInformation("Delete cancelled", "Record information was not deleted", "");
            }

        }catch(NullPointerException xce){
            new AlertHandler().getTableError();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }    
}
