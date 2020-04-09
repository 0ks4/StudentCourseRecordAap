/**
 * this class represents a course
 *
 */
package main;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Asko
 */
public class Course implements Serializable {

    /**the attributes of a course
     */
    protected String courseID;
    protected String name;
    protected int credits;
    protected String description;
    protected String faculty;
    protected LinkedList<Record> attendanceList = new LinkedList<Record>();

    /**
     * nonparametric constructor
     */
    public Course (){}

    /**
     * parametric constructor
     * @param kid = kurssi_id
     * @param n = name
     * @param cr = credits
     * @param k = description
     * @param f = faculty
     */
    public Course (String kid, String n, int cr, String k, String f){
        this.courseID = kid;
        this.name = n;
        this.credits = cr;
        this.description = k;
        this.faculty = f;
    }

    /**getters and setters start here
     * @return getters return their respective attribute values
     *
     * **/
    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String c) {
        this.courseID = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedList<Record> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(LinkedList<Record> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String f) {
        this.faculty = f;
    }
    /**getters and setters end here**/

    /**
     * overridden toString -method mostly used in program testing
     * **/
    @Override
    public String toString(){
        return "Course information: \n" +
                "CourseID: "+this.courseID +"\n" +
                "Name: "+this.name+"\n" +
                "Study credits: "+this.credits+"\n" +
                "Faculty: "+this.faculty+"\n"+
                "Description : "+this.description+"\n";
    }    
}
