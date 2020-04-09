/**
 * this page is used when selecting courses for a students record
 * it is also used when editing a record from record edit page
 * these two ways of selecting courses are separated with a select mode
 * which is handled with a static boolean
 * when navigating from record edit page to this page, the boolean is
 * set to true and certain events are handled in the methods according
 * to this mode
 *
 */
package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
public class CourseSelectPage extends Application implements Initializable {

    /**
     * @param selectForRecordMode = by default set false, but when navigating from record edit page, is set to true
     * @param facultylist = this list is used for choicebox
     * @param gradelist = this list is used for grades choicebox
     *
     * */
    static boolean selectForRecordMode = false;
    private ChangeListener<String> choiceboxListener;
    FilteredList<Course> courseFilteredList;
    ObservableList<String> facultyList = FXCollections.observableArrayList(null, "Economics", "Humanities", "Philosophy", "Science", "Social Science");
    ObservableList<Integer> gradeList = FXCollections.observableArrayList(1, 2, 3, 4, 5);

    /**tableview for this page**/
    @FXML
    TableView<Course> tbvCourseSelect;
    @FXML
    private TableColumn<Course, String> tbcCourseID;
    @FXML
    private TableColumn<Course, String> tbcName;
    @FXML
    private TableColumn<Course, Integer> tbcCredits;
    @FXML
    private TableColumn<Course, String> tbcDescription;
    @FXML
    private TableColumn<Course, String> tbcFaculty;

    /**choiceboxes**/
    @FXML
    ChoiceBox cbxFaculty;
    @FXML
    ChoiceBox cbxGrade;

    /**buttons**/
    @FXML
    Button btnSaveRecord;
    @FXML
    Button btnCancel;

    /**textfield**/
    @FXML
    TextField txfStudentID;

    /**datepicker**/
    @FXML
    DatePicker dtpDatePicker;

    /**labels**/
    @FXML
    Label lblStudentsGrade;
    @FXML
    Label lblCompletionDate;
    @FXML
    Label lblInstruction;

    /**main method
     * @param args =..
     * **/
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    /**initialize method
     * first the tableview populating method is called
     * and the choicebox items are set from the lists
     * additionally the student id from the student edit page
     * static attribute is set in the textfield
     * then the page mode is checked and elements are
     * either hidden or shown depending on the mode
     * **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        callCourseData();
        txfStudentID.setText(StudentEditPage.handledStudent.getStudentID());
        if (selectForRecordMode) {
            cbxGrade.setVisible(false);
            dtpDatePicker.setVisible(false);
            lblStudentsGrade.setVisible(false);
            lblCompletionDate.setVisible(false);
            lblInstruction.setText("Please select a new course for recordID: " + RecordEditPage.handledRecord.getRecordID());
            btnSaveRecord.setText("Select this Course");
        } else {
            cbxGrade.setItems(gradeList);
            cbxGrade.setValue(null);
        }
    }

    /** method to populate tableview with course records
     * prepares the tableview by clearing it
     * and setting up the columns and setting selection mode
     * to multiple row -selection mode. When this is done
     * uses an instance of DBhandler class to get data
     * from database using DBhandlers query methods
     * finally sets an action listener for mouse double clicks
     * and choicebox filter action listener
     * doesnt use any parameters
     * and doesnt have a return value
     * **/
    public void callCourseData() {
        //tbvCourseSelect.getItems().clear();

        DBhandler dbh = new DBhandler();
        System.out.println("\t >> trying to populate course table");
        cbxFaculty.setItems(facultyList);
        cbxFaculty.setValue(null);

        tbcCourseID.setCellValueFactory(new PropertyValueFactory<Course, String>("CourseID"));
        tbcName.setCellValueFactory(new PropertyValueFactory<Course, String>("Name"));
        tbcCredits.setCellValueFactory(new PropertyValueFactory<Course, Integer>("Credits"));
        tbcFaculty.setCellValueFactory(new PropertyValueFactory<Course, String>("Faculty"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<Course, String>("Description"));

        System.out.println("\t >> table prepped. Getting courses...");

        ObservableList<Course> a= FXCollections.observableArrayList();
        a.removeAll();

        a = dbh.loadAvailableCoursesForStudent(StudentEditPage.handledStudent);
        System.out.println("\t >> Course retrieval done. Populating course table...");
        tbvCourseSelect.getItems().removeAll();
        tbvCourseSelect.refresh();
        tbvCourseSelect.setItems(a);

        System.out.println("\t >> Course table populated.");

        tbvCourseSelect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
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
        setCourseChoiceBoxSortListener();
    }

    /**method to insert record to database
     * a student is selected from the tableview
     * and this method is invoked. Confirmation from the user is then required
     * using the alerthandlers confirmation method
     * if ok button is pressed, a record is saved to database
     * else, operation is cancelled
     * null pointer exception is handled since users are
     * able to proceed without making necessary selections
     * from all the elements
     * **/
    @FXML
    public void addSelectedRecordForStudent() {
        DBhandler db = new DBhandler();
        AlertHandler ah = new AlertHandler();

        String title = "confirm operation";
        String header = " Student will receive a record";
        String content = "Are you sure you want to add record for this Student?";

        Course c = tbvCourseSelect.getSelectionModel().getSelectedItem();
        Record r = null;
        try {
            Object grade = cbxGrade.getValue();
            int intgrade = (Integer) grade;
            r = new Record(
                    dtpDatePicker.getValue(),
                    intgrade,
                    StudentEditPage.handledStudent.getStudentID(),
                    c.getCourseID(),
                    db.getNewRecordID(),
                    c.getName(),
                    StudentEditPage.handledStudent.getName(),
                    c.getCredits());
            if (ah.getConfirmation(title, header, content) == ButtonType.OK) {
                db.insertRecord(r);
            } else {
                System.out.println("Record was not added");
            }
        } catch (NullPointerException exception) {
            ah.getTableError();
        }
        callCourseData();
        cbxGrade.setValue(null);
    }

    /**method to handle save button press
     * depending on page mode events are triggered
     * if selectforrecordmode = true,
     * the course that is selected on the tableview is queryed from database by course id
     * and set as the recordeditpage's attribute Course
     * this page is then closed and the recordeditpage's course information is updated
     *
     * if selectforrecordmode = false,
     * the selected course, with grade and date information is added to the student's own records
     *
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
                System.out.println("phase 1");
                Course c = db.getCourseByID(tbvCourseSelect.getSelectionModel().getSelectedItem().getCourseID());
                RecordEditPage.handledCourse = c;
                System.out.println(c.toString());
                System.out.println(RecordEditPage.handledCourse.toString());
                System.out.println("phase 2");
                Method m = RecordEditPage.getInstance().getClass().getDeclaredMethod("populateCourseFields");
                m.invoke(RecordEditPage.getInstance());
                Cancel(new ActionEvent());
            } else {
                addSelectedRecordForStudent();
            }
        } catch (NullPointerException exd) {
            new AlertHandler().getTableError();
        }
    }

    /** method to cancel editing and close stage
     * also if selectforrecord is false
     * student edit pages tableview is refreshed,
     * since the user is returning back to student edit page
     * with a new record added to the student
     * **/
    @FXML
    private void Cancel(ActionEvent e)  {
        System.out.print(selectForRecordMode);
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.hide();
        stage.close();
        try {
            if (selectForRecordMode == false) {
                System.out.println("phase 3");
                Method m = StudentEditPage.getInstance().getClass().getDeclaredMethod("populateRecords");
                selectForRecordMode=false;
                m.invoke(StudentEditPage.getInstance());
            }
        } catch(NoSuchMethodException nse){
            nse.printStackTrace();
        }
        catch(InvocationTargetException ite){
            ite.printStackTrace();
        }
        catch(IllegalAccessException iae){
            iae.printStackTrace();
        }
    }

    /**
     *  method to set a choicebox filter listener
     *  for record page tableview element
     * similarly to textfield search, it uses a filteredlist object
     * that is comprised of readymade strings corresponding to faculties
     *  it then filters the tableview contents according to chosen faculty
     **/
    public void setCourseChoiceBoxSortListener(){
        courseFilteredList= new FilteredList<>(tbvCourseSelect.getItems(), b -> true);
        cbxFaculty.valueProperty().addListener(choiceboxListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                courseFilteredList.setPredicate(newValue== null ? null : (Course) -> newValue.equals(Course.getFaculty()));
            }
        });
        tbvCourseSelect.setItems(courseFilteredList);
    }    
}
