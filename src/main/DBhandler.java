/**
 * this class handles all database interactions in this app
 * it is a separate entity for the sake of modularity
 * and code readability
 *
 */
package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.Random;
import java.time.LocalDate;
/**
 *
 * @author Asko
 */
public class DBhandler {
    
    /* String local = "jdbc:mariadb://localhost:3306";*/

    protected Connection connection;
    protected String address= "";//your remote database address

    protected String username="";//your database username
    protected String password="";//your database passwd
    
    protected String dbname = "a_cbc_demoday";//your database name

    protected String alertErrorTitle = "Error in database operation";
    protected String alertInfoTitle = "Database operation succesful";
    protected String alertPleaseCheck = "Please check your internet connection or firewall";

    public DBhandler() {
    }

    /** this method is used to establish a database connection
     * @return connection object
     *
     * **/
    public Connection openConnection (){
        try{
            Connection conn = DriverManager.getConnection(this.getCredentials());
            System.out.println("\t >> connection opened");
            this.connection = conn;
            return conn;
        } catch(Exception e){
            e.printStackTrace();
            AlertHandler ah = new AlertHandler();
            ah.getError(this.alertErrorTitle, "Error in connection", this.alertPleaseCheck);
            return null;
        }
    }
    
    /** this method closes a database connection**/
    public void closeConnection(){
        try{
            if (this.connection!=null)
                this.connection.close();
            System.out.println("\t >> connection closed");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**this method is not in use in this app, but is left here for programmers
     * later use in other apps
     * @param s = string for the name of the database
     * **/
    public void useDatabase( String s) {
        openConnection();
        try {
            System.out.println("\t >> connecting to " + s);
            Statement stmt = this.connection.createStatement();
            stmt.execute("USE " + s + ";");
            System.out.println("\t >> " + s + " ready");
        } catch(SQLException e){
            System.out.println("\t >> couldn't reach" + s);
        }
        closeConnection();
    }

    /**this method is not in use in this app, but is left here for programmers
     * later use in other apps
     * @param p = string for name of table
     * **/
    public void createDatabase(String p)  {
        openConnection();
        try {
            System.out.println("starting to create database");
            Statement stmt = this.connection.createStatement();
            //stmt.execute("DROP DATABASE IF EXISTS " + p);
            System.out.print("...");
            stmt.execute("CREATE DATABASE IF NOT EXISTS "+p);
            System.out.println("\t>> database " +p+" created");
            System.out.print("...");
            stmt.execute("USE "+p);
            System.out.println("\t>> using database "+p);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("database was not created");
        }
        closeConnection();
    }
    
    /**this method compiles database credentials from address, username and password
     * @return a string compiled from other strings
     * **/
    public String getCredentials(){
        String part1 = this.address;
        String part2 = "?user="+this.username;
        String part3 = "&password="+this.password;

        String finalAddress=""+part1+part2+part3;

        return finalAddress;
    }
    
    /**this method is not in use in this app, but is left here for programmers
     * later use in other apps
     * **/
    public void createStudentTable() {
        openConnection();
       try {
           Statement stmt = this.connection.createStatement();
           //stmt.executeQuery("USE "+this.dbname);
           String clause = "CREATE TABLE Student (" +
                   "StudentID VARCHAR(4)," +
                   "firstName VARCHAR(20)," +
                   "lastName VARCHAR(20)," +
                   "address VARCHAR(40)," +
                   "ZIPloc VARCHAR(20)," +
                   "ZIP VARCHAR(5)," +
                   "Email VARCHAR(50)," +
                   "phone VARCHAR(10)," +
                   "recordList BLOB," +
                   "PRIMARY KEY (StudentID)" +
                   ");";
           stmt.executeQuery(clause);
           System.out.println("\t >> student table created ");
       } catch (SQLException e){
           e.printStackTrace();
       }
       closeConnection();
    }

    /**this method is not in use in this app, but is left here for programmers
     * later use in other apps
     * **/
    public void createCourseTable()  {
        openConnection();
        try {
            Statement stmt = this.connection.createStatement();
            //stmt.executeQuery("USE "+this.dbname);
            String clause = "CREATE TABLE Course (" +
                    "CourseID VARCHAR(3),"+
                    "coursename VARCHAR(50),"+
                    "credits INT,"+
                    "description VARCHAR(300),"+
                    "attendanceList BLOB,"+
                    "faculty VARCHAR(50),"+
                    "PRIMARY KEY (CourseID)"+
                    ");";
            stmt.executeQuery(clause);
            System.out.println("\t >> course table created ");
        } catch (SQLException e){
            e.printStackTrace();
        }
        closeConnection();
    }

    /**this method is not in use in this app, but is left here for programmers
     * later use in other apps
     * **/
    public void createRecordTable() {
        openConnection();
        try {
            Statement stmt = this.connection.createStatement();
            //stmt.executeQuery("USE "+this.dbname);
            String clause = "CREATE TABLE Record (" +
                    "recordDate DATE,"+
                    "grade INT,"+
                    "StudentID VARCHAR(4),"+
                    "courseID VARCHAR(3),"+
                    "recordID VARCHAR(5),"+
                    "FOREIGN KEY (StudentID) REFERENCES Student(StudentID),"+
                    "FOREIGN KEY (CourseID) REFERENCES Course(CourseID)"+
                    ");";
            stmt.executeQuery(clause);
            System.out.println("\t >> record table created ");
        } catch (SQLException e){
            e.printStackTrace();
        }
        closeConnection();
    }
    
    /**this method is used to insert a student information into the database using a student object
    * alerthandler is used handling database errors
     * basic logic= open connection, operate database, close connection
     * @param s = the student that is to be added into the database
     * no return value
     * **/
   public void insertStudent(Student s) {
       AlertHandler ah = new AlertHandler();
       openConnection();
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            PreparedStatement ps = this.connection.prepareStatement(
                    "INSERT INTO Student " + "(StudentID, firstName, lastName, address, ZIPloc, ZIP, Email, phone)"+
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, s.getStudentID());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getAddress());
            ps.setString(5, s.getZIPloc());
            ps.setString(6, s.getZIP());
            ps.setString(7, s.getEmail());
            ps.setString(8, s.getPhone());
            ps.execute();
            System.out.println("\t>> student inserted ");
            ah.getInformation(this.alertInfoTitle,"Student inserted to database",""+s.firstName+" "+s.lastName+" is now enrolled");
        } catch (SQLException e) {
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Student was not inserted to database", this.alertPleaseCheck );
        }
        closeConnection();
    }
   
    /**this method is used to insert course information to database
     * alerthandler is used handling database errors
     * basic logic= open connection, operate database, close connection
     * @param c = the course that is to be added into the database
     * no return value
     * **/
    public void insertCourse(Course c) {
        AlertHandler ah = new AlertHandler();
       openConnection();
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            PreparedStatement ps = this.connection.prepareStatement(
                    "INSERT INTO Course " + "(CourseID, coursename, credits, description, faculty)" +
                            " VALUES (?, ?, ?, ?, ? )"
            );
            ps.setString(1, c.getCourseID());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getDescription());
            ps.setString(5, c.getFaculty());
            ps.execute();
            System.out.println("\t>> course inserted ");
            ah.getInformation(this.alertInfoTitle, "Course was inserted to database", ""+c.getName()+" is now ready");
        } catch (SQLException e) {
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Course was not inserted to database", this.alertPleaseCheck);
        }
        openConnection();
    }

    /**this method is used to insert record information to database
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param r= the record that is to be added into the database
     * no return value
     * **/
    public void insertRecord(Record r) {
        AlertHandler ah = new AlertHandler();
        openConnection();
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");

            PreparedStatement ps = this.connection.prepareStatement(
                    "INSERT INTO Record " + "(recordDate, grade, StudentID, courseID, recordID, CourseName, StudentName, Credits)" +
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ? )"
            );
            ps.setDate(1,r.getRecordSQLDate());
            ps.setInt(2, r.getGrade());
            ps.setString(3, r.getStudentID());
            ps.setString(4, r.getCourseID());
            ps.setString(5, r.getRecordID() );
            ps.setString(6, r.getCourseName() );
            ps.setString(7, r.getStudentName() );
            ps.setInt(8, r.getCredits() );
            ps.execute();
            System.out.println("\t>> course inserted ");
            ah.getInformation(this.alertInfoTitle, "Record was inserted to database", "Record No. "+r.getRecordID()+" is now registered to "+r.getStudentID());
        } catch (SQLException e) {
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Course was not inserted to database", this.alertPleaseCheck);
        }
        openConnection();
    }

    /** this method gets all student data from database
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Student> loadStudentData() {
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Student> data =FXCollections.observableArrayList();
       try {
           Statement stmt = this.connection.createStatement();
           stmt.executeQuery("USE a_cbc_demoday");
           ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM Student;"); // executing query to a resultset
           while (rs.next()){
               data.add(new Student( //adding data to the observable list, one student instance at a time
                       rs.getString(1),
                       rs.getString(2),
                       rs.getString(3),
                       rs.getString(4),
                       rs.getString(6),
                       rs.getString(5),
                       rs.getString(7),
                       rs.getString(8)
                       ));
           }
       } catch(SQLException e){
           System.out.println("\t >> failed to load student data");
           e.printStackTrace();
           ah.getError(this.alertErrorTitle, "Failed to load student data", this.alertPleaseCheck);
       }
       closeConnection();
        return data;
    }

    /** this method gets all course data from database
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Course> loadCourseData(){
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Course>data = FXCollections.observableArrayList();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM Course;"); // executing query to a resultset
            while (rs.next()) {
                data.add(new Course( //adding data to the observable list, one Course instance at a time
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load course data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Failed to load course data", this.alertPleaseCheck);
        }
        closeConnection();
        return data;
    }
    
    /** this method gets all course data from database, that is available for a select student
     * meaning all the courses, that this student is yet to complete
     * first query is to get the courses that the student has completed
     * and add them in to a list
     * then a second query is made with NOT IN -clause and all
     * completed course IDs are added in this query resulting
     * in a query that gives the courses that the student has not completed yet
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param s = the student to whom the user is adding records to
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Course> loadAvailableCoursesForStudent(Student s){
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Course>data =FXCollections.observableArrayList();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ObservableList<String> completedCoursesList = FXCollections.observableArrayList();
            ResultSet CompletedCourses = this.connection.createStatement().executeQuery("SELECT CourseID FROM Record WHERE StudentID ='"+s.getStudentID()+"'");
            while(CompletedCourses.next()){
                completedCoursesList.add(CompletedCourses.getString(1));
            }

            String addCourseIDtoQuery;
            String query = "SELECT * FROM Course WHERE CourseID NOT IN(";
            for(String a: completedCoursesList){
                addCourseIDtoQuery= "'"+a+"',";
                query += addCourseIDtoQuery;
            }
            query+="'0');";

            System.out.println(query);

            ResultSet rs = this.connection.createStatement().executeQuery(query); // executing query to a resultset
            while (rs.next()) {
                data.add(new Course( //adding data to the observable list, one Course instance at a time
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load course data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Failed to load course data", this.alertPleaseCheck);
        }
        closeConnection();
        return data;
    }

    /** this method gets all student data from database, that is available for a select course
     * meaning all the students, that haven't completed this course yet.
     * first query is to get the students that have completed the course
     * and add them in to a list
     * then a second query is made with NOT IN -clause and all
     * students who have completed the course, have their student IDs added in this query resulting
     * in a query that gives the students that havent yet completed this course
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param c = the course to which the user is adding the records to
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Student> loadAvailableStudentsForCourse(Course c){
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Student>data =FXCollections.observableArrayList();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ObservableList<String> attendedStudentList = FXCollections.observableArrayList();
            ResultSet attendedStudents = this.connection.createStatement().executeQuery("SELECT StudentID FROM Record WHERE CourseID ='"+c.getCourseID()+"'");
            while(attendedStudents.next()){
                attendedStudentList.add(attendedStudents.getString(1));
            }

            String addStudentIDtoQuery;
            String query = "SELECT * FROM Student WHERE StudentID NOT IN(";

            for(String a: attendedStudentList){
                addStudentIDtoQuery= "'"+a+"',";
                query += addStudentIDtoQuery;
            }
            query+="'0');";

            System.out.println(query);

            ResultSet rs = this.connection.createStatement().executeQuery(query); // executing query to a resultset
            while (rs.next()) {
                data.add(new Student( //adding data to the observable list, one student object at a time
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(6),
                        rs.getString(5),
                        rs.getString(7),
                        rs.getString(8)
                ));
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load course data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Failed to load course data", this.alertPleaseCheck);
        }
        closeConnection();
        return data;
    }

    /** this method gets all record data from database
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Record> loadRecordData(){
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Record> data = FXCollections.observableArrayList();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM Record;"); // executing query to a resultset
            while (rs.next()) {
                data.add(new Record( //adding data to the observable list, one Record instance at a time
                        rs.getDate(1).toLocalDate(),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(8)
                        ));
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load record data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Failed to load course data", this.alertPleaseCheck);
        }
        closeConnection();
        return data;
    }

    /** this method gets all record data from database from a certain student
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param ID= student id which is used in the query as parameter
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Record> loadStudentsRecordData(String ID){
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Record> data = FXCollections.observableArrayList();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM Record WHERE StudentID= '"+ID+"'"); // executing query to a resultset
            while (rs.next()) {
                data.add(new Record( //adding data to the observable list, one Record instance at a time
                        rs.getDate(1).toLocalDate(),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(8)
                ));
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load record data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Failed to load course data", this.alertPleaseCheck);
        }
        closeConnection();
        return data;
    }

    /** this method gets all record data from database from a certain course
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param ID= course id which is used in the query as parameter
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public ObservableList<Record> loadCoursesRecordData(String ID){
        AlertHandler ah = new AlertHandler();
        openConnection();
        ObservableList<Record>data = FXCollections.observableArrayList();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM Record WHERE CourseID= '"+ID+"'"); // executing query to a resultset
            while (rs.next()) {
                data.add(new Record( //adding data to the observable list, one Record instance at a time
                        rs.getDate(1).toLocalDate(),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(8)
                ));
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load record data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Failed to load course data", this.alertPleaseCheck);
        }
        closeConnection();
        return data;
    }

    /** this method gets all data from database from a certain student
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param id= student id which is used in the query as parameter
     * @return = observablelist that is usually used to populate tableview elements
     * **/
    public Student getStudentByID(String id){
        AlertHandler ah = new AlertHandler();
        openConnection();
           Student searched=null;
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM Student WHERE StudentID='"+id+"';"); // executing query to a resultset
            while(rs.next()){
             searched = new Student(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(6),
                        rs.getString(5),
                        rs.getString(7),
                        rs.getString(8)
                );}
        } catch (SQLException e){
            System.out.println("\t >> failed to load student data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not find Student", "please check search parameters");
        }
        closeConnection();
        return searched;
    }

    /** this method gets all data from database from a certain course
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param id= course id which is used in the query as parameter
     * @return searched = Course instance that is queried
     * **/
    public Course getCourseByID(String id){
        AlertHandler ah = new AlertHandler();
        openConnection();
        Course searched=null;
        try{
            Statement stmt = this.connection.createStatement();
            String sql = "SELECT *\n" +
                    "FROM Course\n" +
                    "WHERE CourseID = '"+id+"';";
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery(sql); // executing query to a resultset
            while(rs.next()) {
                searched = new Course(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5)
                );
            }
        } catch (SQLException e){
            System.out.println("\t >> failed to load course data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not find course", "please check search parameters");
        }
        return searched;
    }

    /** this method updates student data
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param s= the student whose information is bein updated
     * **/
    public void updateStudentToDatabase(Student s){
        AlertHandler ah = new AlertHandler();
        openConnection();
        try{
            System.out.println("UPDATE Student \n" +
                    "SET firstName = '"+s.getFirstName()+"',\n" +
                    " lastName = '"+s.getLastName()+"', address='"+s.getAddress()+"', \n" +
                    "ZIPloc = '"+s.getZIPloc()+"', ZIP = '"+s.getZIP()+"', \n" +
                    "Email ='"+s.getEmail()+"', phone='"+s.getPhone()+"'\n" +
                    "WHERE StudentID = '"+s.getStudentID()+"'");
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            stmt.executeQuery("UPDATE Student \n" +
                    "SET firstName = '"+s.getFirstName()+"',\n" +
                    " lastName = '"+s.getLastName()+"', address='"+s.getAddress()+"', \n" +
                    "ZIPloc = '"+s.getZIPloc()+"', ZIP = '"+s.getZIP()+"', \n" +
                    "Email ='"+s.getEmail()+"', phone='"+s.getPhone()+"'\n" +
                    "WHERE StudentID = '"+s.getStudentID()+"'");
            
            stmt.executeQuery("UPDATE Record SET StudentName = '"+s.getName()+"' WHERE StudentID ='"+s.getStudentID()+"'");
            
            System.out.println("UPDATE Student \n" +
                    "SET firstName = '"+s.getFirstName()+"',\n" +
                    " lastName = '"+s.getLastName()+"', address='"+s.getAddress()+"', \n" +
                    "ZIPloc = '"+s.getZIPloc()+"', ZIP = '"+s.getZIP()+"', \n" +
                    "Email ='"+s.getEmail()+"', phone='"+s.getPhone()+"'\n" +
                    "WHERE StudentID = '"+s.getStudentID()+"'");
            ah.getInformation(this.alertInfoTitle, "Student information was updated to database", "Student information for ID "+s.getStudentID()+" is now updated");
        } catch (SQLException e){
            System.out.println("\t >> failed to load student data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not update student data", this.alertPleaseCheck);
        }
        closeConnection();
    }

    /** this method updates course data
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param c= the course whose information is bein updated
     * **/
    public void updateCourseToDatabase(Course c){
        AlertHandler ah = new AlertHandler();
        openConnection();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            PreparedStatement ps = this.connection.prepareStatement("UPDATE Course \n" +
                    "SET coursename = ?,\n" +
                    " credits = ?," +
                    "Description = ?, Faculty = ? WHERE CourseID = ?");
            ps.setString(1,c.getName());
            ps.setInt(2,c.getCredits());
            ps.setString(3,c.getDescription());
            ps.setString(4,c.getFaculty());
            ps.setString(5,c.getCourseID());
            ps.execute();

            stmt.executeQuery("UPDATE Record SET courseName ='"+c.getName()+"', credits ="+c.getCredits()+" WHERE CourseID = '"+c.getCourseID()+"'");

            ah.getInformation(this.alertInfoTitle, "Course information was updated to database", "Course information for ID "+c.getCourseID()+" is now updated");
        } catch (SQLException e){
            System.out.println("\t >> failed to update course data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not update course data", this.alertPleaseCheck);
        }
        closeConnection();
    }

    /** this method updates record data
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param r= the record whose information is bein updated
     * **/
    public void updateRecord(Record r){
        AlertHandler ah = new AlertHandler();
        openConnection();
        try{
            System.out.println(""+r.toString());
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            stmt.executeQuery("UPDATE Record \n" +
                    "SET recordDate = '"+r.getRecordSQLDate()+"',\n" +
                    " grade = '"+r.getGrade()+"', StudentID='"+r.getStudentID()+"', \n" +
                    "courseID = '"+r.getCourseID()+"', \n" +
                    "CourseName ='"+r.getCourseName()+"', StudentName='"+r.getStudentName()+"'\n" + ", Credits = '"+r.getCredits()+"'" +
                    "WHERE RecordID = '"+r.getRecordID()+"'"); //
            ah.getInformation(this.alertInfoTitle, "Record information was updated to database", "Record information for ID "+r.getRecordID()+" is now updated");
        } catch (SQLException e){
            System.out.println("\t >> failed to load Record data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not update Record data", this.alertPleaseCheck);
        }
        closeConnection();
    }

    /**method to get student ID numbers for the numbering of new students
     * the logic of numbering is to parse the greatest integer
     * from all students, and +1 to this number
     * this means a deleted student's studentID may be assigned to a new student in
     * certain situations
     * if there are no students in the database, a new sequence is started
     * with the number 1000 assigned to the first student that is inserted into the database
     * alerthandler is used handling errors and informing user
     * @return newStudentNumber = the next number in the student numbering sequence
     * **/
    public String getNewStudentID()  {
        AlertHandler ah = new AlertHandler();
        openConnection();
        String newStudentNumber="";
        Integer count=0;
        Integer greatest=0;
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT StudentID FROM Student;");
            while (rs.next()){
                String a = rs.getString(1);
                count = count.parseInt(a);
                if(count>greatest){
                    greatest=count;
                }
            }
            if(greatest==0){
                greatest = 1000;
            }
            else{greatest = greatest+1;}
            newStudentNumber = ""+greatest;
            System.out.println("\t >> course numbers loaded, new course number = "+newStudentNumber);
        }
        catch (SQLException e) {
            System.out.println("\t >> failed to load course numbering data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "failed to load course numbering data", this.alertPleaseCheck);
        }
        closeConnection();
        return newStudentNumber;
        }

    /**method to get course ID numbers for the numbering of new courses
     * the logic of numbering is to parse the greatest integer
     * from all courses courseIDs, and add +1 to this number
     * this means a deleted course's courseID may be assigned to a new course in
     * certain situations
     * if there are no courses in the database, a new sequence is started
     * with the number 100 assigned to the first course that is inserted into the database
     * alerthandler is used handling errors and informing user
     * @return newCourseNumber = the next number in the course numbering sequence
     * **/
    public String getNewCourseID() {
        AlertHandler ah = new AlertHandler();
        openConnection();
        String newCourseNumber="";
        Integer count=0;
        Integer greatest=0;
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT CourseID FROM Course;");
            while (rs.next()){
                String a = rs.getString(1);
                count = count.parseInt(a);
                if(count>greatest){
                    greatest=count;
                }
            }
            if(greatest==0){
                greatest = 100;
            }
            else{greatest = greatest+1;}
            newCourseNumber = ""+greatest;
            System.out.println("\t >> course numbers loaded, new course number = "+newCourseNumber);
        }
        catch (SQLException e) {
            System.out.println("\t >> failed to load course numbering data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "failed to load course numbering data", this.alertPleaseCheck);
        }
        closeConnection();
        return newCourseNumber;
    }

    /** method to get record ID numbers for the numbering of new records
     * the logic of numbering is to parse the greatest integer
     * from all recordIDs, and add +1 to this number
     * this means a deleted record's recordID may be assigned to a new record in
     * certain situations
     * if there are no records in the database, a new sequence is started
     * with the number 10000 assigned to the first record that is inserted into the database
     * alerthandler is used handling errors and informing user
     * @return newRecordNumber = the next number in the record numbering sequence
     * **/
    public String getNewRecordID() {
        AlertHandler ah = new AlertHandler();
        openConnection();
        String newRecordNumber="";
        Integer count=0;
        Integer greatest=0;
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT recordID FROM Record;");
            while (rs.next()){
                String a = rs.getString(1);
                count = count.parseInt(a);
                if(count>greatest){
                    greatest=count;
                }
            }
            if(greatest==0){
                greatest = 10000;
            }
            else{
                greatest = greatest+1;
            }
            newRecordNumber = ""+greatest;
            System.out.println("\t >> record numbers loaded, new record number = "+newRecordNumber);
        }
        catch (SQLException e) {
            System.out.println("\t >> failed to load record numbering data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "failed to load record numbering data", this.alertPleaseCheck);
        }
        closeConnection();
        return newRecordNumber;
    }

    /** this method deletes student data
     * this means that all student's record data will be deleted completely as well
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param s= the student whose information is being deleted
     * **/
    public void deleteStudentFromDatabase(Student s){
        AlertHandler ah = new AlertHandler();
        openConnection();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            stmt.executeQuery("DELETE FROM Record WHERE StudentID='"+s.getStudentID()+"';");
            stmt.executeQuery("DELETE FROM Student WHERE StudentID='"+s.getStudentID()+"';"); //
            ah.getInformation(this.alertInfoTitle, "Student was deleted from database", "StudentID "+s.getStudentID()+" is now deleted");
        } catch (SQLException e){
            System.out.println("\t >> failed to load student data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not delete Student", this.alertPleaseCheck);
        }
        closeConnection();
    }

    /** this method deletes student data, except that it deletes a list of students and their records
     * this means that all student's record data will be deleted completely as well
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param deleteList= the students whose information is being deleted
     * **/
    public void massDeleteStudentFromDatabase(ObservableList<Student> deleteList){
        AlertHandler ah = new AlertHandler();
        openConnection();
        for (int i = 0; i < deleteList.size(); i++) {
            Student s = deleteList.get(i);
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            stmt.executeQuery("DELETE FROM Record WHERE StudentID='"+s.getStudentID()+"';");
            stmt.executeQuery("DELETE FROM Student WHERE StudentID='"+s.getStudentID()+"';"); //
        } catch (SQLException e){
            System.out.println("\t >> failed to load student data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not delete Student", this.alertPleaseCheck);
        }
        }
        closeConnection();
    }

    /** this method deletes course data
     * this means that all course's record data will be deleted completely as well
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param c= the student whose information is being deleted
     * **/
    public void deleteCourseFromDatabase(Course c){
        AlertHandler ah = new AlertHandler();
        openConnection();
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            stmt.executeQuery("DELETE FROM Record WHERE CourseID = '"+c.getCourseID()+"';");
            stmt.executeQuery("DELETE FROM Course WHERE CourseID = '"+c.getCourseID()+"';"); //
            ah.getInformation(this.alertInfoTitle, "Course was deleted from database", "CourseID "+c.getCourseID()+" is now deleted");
        } catch (SQLException e){
            System.out.println("\t >> failed to load student data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not delete course", this.alertPleaseCheck);
        }
        closeConnection();
    }

    /** this method deletes course data, except that it deletes a list of courses and their records
     * this means that all course's record data will be deleted completely as well
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param deleteList= the courses whose information is being deleted
     * **/
    public void massDeleteCourseFromDatabase(ObservableList<Course>deleteList){
        AlertHandler ah = new AlertHandler();
        openConnection();
        for (int i = 0; i < deleteList.size(); i++) {
            Course c = deleteList.get(i);
        try{
            Statement stmt = this.connection.createStatement();
            stmt.executeQuery("USE a_cbc_demoday");
            stmt.executeQuery("DELETE FROM Record WHERE CourseID = '"+c.getCourseID()+"';");
            stmt.executeQuery("DELETE FROM Course WHERE CourseID = '"+c.getCourseID()+"';");
        } catch (SQLException e){
            System.out.println("\t >> failed to load student data");
            e.printStackTrace();
            ah.getError(this.alertErrorTitle, "Could not delete course", this.alertPleaseCheck);
        }
        }
        closeConnection();
    }

    /** this method deletes record data, except that it deletes a list of records 87
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param deleteList= the records which will be deleted
     * **/
    public void deleteRecordsFromDatabase(ObservableList<Record> deleteList){
        AlertHandler ah = new AlertHandler();
        openConnection();
        for (int i = 0; i < deleteList.size(); i++) {
            Record r = deleteList.get(i);
            try {
                Statement stmt = this.connection.createStatement();
                stmt.executeQuery("USE a_cbc_demoday");
                stmt.executeQuery("DELETE FROM Record WHERE recordID = '" + r.getRecordID() + "';"); //
            } catch (SQLException e) {
                System.out.println("\t >> failed to load record data");
                e.printStackTrace();
                ah.getError(this.alertErrorTitle, "Could not delete record: "+r.getRecordID(), this.alertPleaseCheck);
            }
        }
        ah.getInformation(this.alertInfoTitle, "Record(s) were deleted from database", "operation complete");
        closeConnection();
    }
    
    /** this method deletes record data, by single recordID
     * alerthandler is used handling errors and informing user
     * basic logic= open connection, operate database, close connection
     * @param RecordID= the record which will be deleted
     * **/
    public void deleteRecordByID(String RecordID){
        AlertHandler ah = new AlertHandler();
        openConnection();
            try {
                Statement stmt = this.connection.createStatement();
                stmt.executeQuery("USE a_cbc_demoday");
                stmt.executeQuery("DELETE FROM Record WHERE recordID = '" + RecordID+ "';"); //
            } catch (SQLException e) {
                System.out.println("\t >> failed to load record data");
                e.printStackTrace();
                ah.getError(this.alertErrorTitle, "Could not delete student's records. ", this.alertPleaseCheck);
            }
        ah.getInformation(this.alertInfoTitle, "Record(s) were deleted from database", "operation complete");
        closeConnection();
    }

    /**getters and setters
     * @return respective attributes from each getter
     * **/
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String a1) {
        this.address = a1;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String u1) {
        this.username = u1;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p1) {
        this.password = p1;
    }

    public String getDbname() {
        return this.dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public Connection getConnection() {
        return this.connection;
    }    
}
