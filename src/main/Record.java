/**
 * this class represents a single record of attendance in a course
 * and the record of completion of a course for a student
 *
 */
package main;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Asko
 */
public class Record implements Serializable {
    /**the attributes of a record**/
    protected LocalDate recordDate;
    protected int grade;
    protected String recordID;
    protected String courseID;
    protected String studentID;

    protected String CourseName;
    protected String StudentName;
    protected int Credits;

    /**
     * constructor
     * @param date = date
     * @param gr = grade
     * @param sID = student id
     * @param cID = course id
     * @param rID = record id
     * @param cName = course name
     * @param sName = student name
     * @param cred = credits
     */
    public Record(LocalDate date, int gr,String sID,  String cID, String rID, String cName, String sName, int cred  ) {
        this.recordDate = date;
        this.grade = gr;
        this.studentID = sID;
        this.courseID = cID;
        this.recordID = rID;
        this.CourseName = cName;
        this.StudentName = sName;
        this.Credits = cred;
    }

    /**
     * getters and setters start here
     * @return = respective value from each attribute
     * */
    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String r) {
        this.recordID = r;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String s) {
        this.studentID = s;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String c) {
        CourseName = c;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String sn) {
        StudentName = sn;
    }

    public int getCredits() {
        return Credits;
    }

    public void setCredits(int cr) {
        Credits = cr;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getAttendeeID() {
        return studentID;
    }

    public void setAttendeeID(String a) {
        this.studentID = a;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int g) {
        this.grade = g;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate r) {
        this.recordDate = r;
    }
    /**getters and setters end here**/

    /**this method is used to get the date attribute in sql format
     * @return = a java sql date
     * **/
    public java.sql.Date getRecordSQLDate() {
        return java.sql.Date.valueOf(recordDate);
    }

    /**toString method mainly used for testing purposes
     * @return a string with field information of this object
     * **/
    @Override
    public String toString(){
        return "Record information: \n RecordID: "+this.recordID+"\nStudent name: "+this.StudentName+" " +
                "\nCourse Name: "+this.CourseName+"\nStudentID: "+this.studentID+"\nCourseID: "+this.courseID+"" +
                "\nRecordID: "+this.recordID+"\nCredits:"+this.Credits+"\nDate of completion: "+this.recordDate+"\n Date in sql form: "+this.getRecordSQLDate();
    }    
}
