/**this class is used to load pages
* to the center part of the main windows'
* borderpanel
*/
package main;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author Asko
 */
public class Pageloader {
    /**this pane is the main component of every page loading event
     * all the pages are loaded as this pane's children
     **/
    private Pane view;
    /**this method is used when loading pages in this app
     *
     *
     * @param filename = the filename of the FXML-file without ".fxml"
     * @return = pane to use in main app
     * **/
    public Pane getPage(String filename){

        try {
            URL fileUrl = MainOR.class.getResource("/main/" + filename + ".fxml");
            System.out.println(""+fileUrl.toString());
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML-file not found");
            }
            this.view = new FXMLLoader().load(fileUrl);
        }catch(Exception e){
            System.out.println("no page "+filename+" please check fxmlloader");
            }
        return view;
    }    
}
