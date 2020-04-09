/**this class is an auxiliary class used to generate data
 * for the different instances of Student and Course
 * basic logic of all methods is the following
 * a random number generator is created
 * a random array index is generated from the Random -instance
 * Strings are returned according random generation
 * **/
package main;

import java.util.Random;

/**
 *
 * @author Asko
 */
public class Generator {
    /**this method generates a lastname for a person
     * @return string for a name
     * **/
    public String lastName(){
        Random r = new Random();
        int a = r.nextInt(9);

        String [] items = new String[10];
        items[0] = "Kuparinen";
        items[1] = "Koskinen";
        items[2] = "Penttinen";
        items[3] = "Tötterström";
        items[4] = "Siegfried";
        items[5] = "Donner";
        items[6] = "Harakka";
        items[7] = "Dunhof";
        items[8] = "Fenhof";
        items[9] = "Janakkala";

        return items[a];
    }

    /**this method generates a first name for a person
     * @return string for a name
     * **/
    public String firstName(){
        Random r = new Random();
        int a = r.nextInt(9);

        String [] items = new String[10];
        items[0] = "Pekka";
        items[1] = "Saana";
        items[2] = "Gottfried";
        items[3] = "Eva";
        items[4] = "Jussi";
        items[5] = "Heili";
        items[6] = "Dennis";
        items[7] = "Bonnie";
        items[8] = "Clyde";
        items[9] = "Denna";

        return items[a];
    }

    /**this method generates an address
     * @return string for an address
     * **/
    public String address(){
        Random r = new Random();
        int a = r.nextInt(9);

        String [] items = new String[10];
        items[0] = "Puu";
        items[1] = "Teräs";
        items[2] = "Aamu";
        items[3] = "Rautatie";
        items[4] = "Kauppa";
        items[5] = "Mannerheimin";
        items[6] = "Posti";
        items[7] = "Kalastajan";
        items[8] = "Asemamestarin";
        items[9] = "Tuulen";

        int b = r.nextInt(9);

        String [] items2 = new String[10];
        items2[0] = "kuja";
        items2[1] = "katu";
        items2[2] = "tie";
        items2[3] = "väylä";
        items2[4] = "risteys";
        items2[5] = "polku";
        items2[6] = "piha";
        items2[7] = "tori";
        items2[8] = "satama";
        items2[9] = "asema";

        int c = r.nextInt(9);

        String [] items3 = new String[10];
        items3[0] = "A";
        items3[1] = "B";
        items3[2] = "C";
        items3[3] = "D";
        items3[4] = "E";
        items3[5] = "F";
        items3[6] = "G";
        items3[7] = "H";
        items3[8] = "I";
        items3[9] = "J";

        return items[a]+items2[b]+" "+(r.nextInt(35)+1)+" "+items3[c]+" "+(r.nextInt(99)+1);
    }

    /**this method generates an email address
     * @return string for an email address
     * **/
    public String email(){
        Random r = new Random();
        int a = r.nextInt(9);
        int b = r.nextInt(9);
        int c = r.nextInt(9);

        String [] items = new String[10];
        items[0] = "saltsu";
        items[1] = "uffe";
        items[2] = "corolla";
        items[3] = "vertsu";
        items[4] = "kasse";
        items[5] = "bosse";
        items[6] = "dobby";
        items[7] = "fast";
        items[8] = "daddy";
        items[9] = "semppu";

        String [] items2 = new String[10];
        items2[0] = "boy";
        items2[1] = "girl";
        items2[2] = "daddy";
        items2[3] = "furious";
        items2[4] = "eazy";
        items2[5] = "boring";
        items2[6] = "boingboing";
        items2[7] = "mama";
        items2[8] = "uulala";
        items2[9] = "yeah";


        String [] items3 = new String[10];
        items3[0] = "email.com";
        items3[1] = "hotmail.com";
        items3[2] = "yahoo.com";
        items3[3] = "google.com";
        items3[4] = "hotmail.fi";
        items3[5] = "elisanet.fi";
        items3[6] = "teliasonera.fi";
        items3[7] = "dna.fi";
        items3[8] = "bambam.com";
        items3[9] = "telecom.com";
        return ""+items[a]+"."+items2[b]+"@"+items3[c];
    }

    /**this method generates a phone number
     * @return string for a phone number
     * **/
    public String phone(){
        Random r = new Random();
        int b = r.nextInt(5);

        String [] items = new String[5];
        items[0] = "050";
        items[1] = "044";
        items[2] = "046";
        items[3] = "045";
        items[4] = "040";
        String a="";
        for (int j=0;j<7;j++){
            int c = r.nextInt(9);
            a = ""+a+c;
        }
        return ""+items[b]+a;
    }
    
    /**this method generates a ZIP code or a finnish postal area code
     * @return string for a ZIP code or a finnish postal area code
     * **/
    public String ZIP() {
        Random r = new Random();
        String a="";
        for (int j=0;j<5;j++){
            int b = r.nextInt(9);
            a = ""+a+b;
        }
        return ""+a;
    }

    /**this method generates a finnish postal area location
     * @return string for a finnish postal area location
     * **/
    public String ZIPloc(){

        Random r = new Random();
        int a = r.nextInt(9);
        String [] items = new String[10];
        items[0] = "KIELO";
        items[1] = "HARJAVALTA";
        items[2] = "HELSINKI";
        items[3] = "TURKU";
        items[4] = "OULU";
        items[5] = "JOENSUU";
        items[6] = "HAMMASLAHTI";
        items[7] = "PÄLKÄNE";
        items[8] = "JOUTSA";
        items[9] = "ROVANIEMI";

        return items[a];
    }

    /**this method generates a name for a course
     * @return string for a name
     * **/
    public String courseName(){
        Random r = new Random();
        int a = r.nextInt(6);
        int b = r.nextInt(9);
        int c = r.nextInt(9);

        String [] items = new String[6];
        items[0] = "Basics of ";
        items[1] = "Advanced Course of ";
        items[2] = "Welcome to ";
        items[3] = "Preliminary Course to ";
        items[4] = "Expert class in ";
        items[5] = "Designing of ";


        String [] items2 = new String[10];
        items2[0] = "Datamining ";
        items2[1] = "Statistical Science ";
        items2[2] = "Philosophical Arts ";
        items2[3] = "User Interface Programming ";
        items2[4] = "Economics ";
        items2[5] = "Logic ";
        items2[6] = "The Art of Debate ";
        items2[7] = "Quantum Physics ";
        items2[8] = "Explosive Chemistry ";
        items2[9] = "Psychoanalysis ";


        String [] items3 = new String[10];
        items3[0] = "1";
        items3[1] = "2";
        items3[2] = "3";
        items3[3] = "4";
        items3[4] = "";
        items3[5] = "";
        items3[6] = "";
        items3[7] = "";
        items3[8] = "";
        items3[9] = "";
        return ""+items[a]+""+items2[b]+""+items3[c];
    }
    
    /**this method generates a description for a course
     * @return string for a course description
     * **/
    public String description(){

        Random r = new Random();
        int a = r.nextInt(9);

        String [] items = new String[10];
        items[0] = "After this course you will attain necessary skills in everything you can imagine. ";
        items[1] = "Despite this courses' name, in this course you will learn how to play the cello. ";
        items[2] = "We should make something up for this description, just like we did with the course content. ";
        items[3] = "In this course you will receive expert guidance on the subject matter, if we have time over our research. ";
        items[4] = "In this course you will become an absolute expert in the subject matter, by studying it alone in Moodle. ";
        items[5] = "In this course everyone will be served cookies. ";
        items[6] = "After this course you will become an international secret agent equipped with deadly skills and surrounded by an aura of mystery. ";
        items[7] = "In this course you will study the subject matter likely not the amount of study credits it gives you. ";
        items[8] = "It is probable that you will be surprised in this course. ";
        items[9] = "In this course you will work in a project with unequally contributing project members. ";

        return items[a];
    }
    
    /**this method generates a faculty for a course
     * @return string for a course faculty
     * **/
    public String faculty() {
        Random r = new Random();
        int a = r.nextInt(5);

        String[] items = new String[5];
        items[0] = "Philosophy";
        items[1] = "Social Science";
        items[2] = "Economics";
        items[3] = "Humanities";
        items[4] = "Science";

        return items[a];
    }    
}
