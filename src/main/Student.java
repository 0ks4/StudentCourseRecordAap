/**
 * this class represents a single student
 *
 */
package main;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Asko
 */
public class Student implements Serializable {

    /**the attributes of a student**/
    protected String studentID;
    protected String firstName;
    protected String lastName;
    protected String address;
    protected String ZIP;
    protected String ZIPloc;
    protected String email;
    protected String phone;

    protected LinkedList<Record> recordList = new LinkedList<Record>();

    /**
     * constructor
     * @param n = studentID
     * @param e = firstName
     * @param s = surName
     * @param l = address
     * @param po = ZIPloc
     * @param pn = ZIP
     * @param em = email
     * @param ph = phone
     */

    public Student (String n, String e, String s, String l, String po, String pn, String em, String ph ){
        this.studentID = n;
        this.firstName = e;
        this.lastName = s;
        this.address = l;
        this.ZIP = po;
        this.ZIPloc = pn;
        this.email = em;
        this.phone = ph;
    }

    /**getters and setters
     * @return = value for each getter
     * **/
    public String getStudentID() {
        return studentID;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSurName(String surName) {
        this.lastName = surName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZIPloc() {
        return ZIPloc;
    }

    public void setZIPloc(String ZIPloc) {
        this.ZIPloc = ZIPloc;
    }

    public String getZIP() {
        return ZIP;
    }

    public void setZIP(String ZIP) {
        this.ZIP = ZIP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LinkedList<Record> getStudentRecords() {
        return recordList;
    }

    public String getName(){
        return ""+this.firstName+" "+this.lastName;
    }
    /**getters and setters end here**/


    /**toString -method mainly for testing purposes**/
    @Override
    public String toString(){

        return "Student information: \n" +
                "StudentID: "+this.studentID+"\n"+
                "First Name: "+this.firstName +"\n" +
                "Last Name: "+this.lastName+"\n" +
                "Address: "+address+"\n" +
                "ZIP "+this.ZIP+"\n"+
                ZIPloc+"\n"+
                "Phone no. : "+this.phone+"\n" +
                "Email: "+this.email;
    }    
}
