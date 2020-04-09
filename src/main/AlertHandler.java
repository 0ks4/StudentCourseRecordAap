/**
 * this class is used as an auxiliary class, that handles exceptions
 * using alerts and dialogpanels to inform users about errors
 * all alerts and their elements use buttonStyle.css styling
 * 
 */
package main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import java.util.Optional;

/**
 *
 * @author Asko
 */
public class AlertHandler {
    /**
     * attributes
     * dialogpane
     * readymade strings to use
     * */
    DialogPane dialogPane;

    String noItem = "No item selected";
    String selectItem = "Please select an item from the table";
    String titleWrongFormat="Information format error";
    String headerUseNumerals = "Only integers can be inserted to selected field";

    /**
     * method to get confirmation alert, demands user to choose between ok and cancel buttons
     * uses strings for dialogpane's different fields as parameters
     * @param title = title of stage
     * @param HeaderText = header of dialogpane
     * @param ContentText = content text of the dialogpane
     * @return buttontype depending on the user's choice
     * **/
    public ButtonType getConfirmation(String title, String HeaderText, String ContentText) {

        System.out.println("waiting for confirmation");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(HeaderText);
            alert.setContentText(ContentText);
            alert.getButtonTypes().clear();

        ButtonType okbtn = ButtonType.OK;
        ButtonType Cancelbtn = ButtonType.CANCEL;

        this.dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
            //dialogPane.getStyleClass().add("myDialog");
            alert.getButtonTypes().addAll(Cancelbtn, okbtn);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get();
}

    /**
     * method to get information alert,
     * uses strings for dialogpane's different fields as parameters
     * returns null, but shows alert to user when called
     * @param title = title of stage
     * @param HeaderText = header of dialogpane
     * @param ContentText = content text of the dialogpane
     *
     * **/
    public void getInformation(String title, String HeaderText, String ContentText){
    System.out.println("waiting for information");
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);

    DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
    alert.showAndWait();

}
    /**
     * method to get error alert,
     * uses strings for dialogpane's different fields as parameters
     * returns null, but shows alert to user when called
     * @param title = title of stage
     * @param HeaderText = header of dialogpane
     * @param ContentText = content text of the dialogpane
     * **/
    public void getError(String title, String HeaderText, String ContentText){
        System.out.println("waiting for information");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        alert.showAndWait();

    }

    /**
     * method to get ready-made error alert when operating with tables,
     * mostly used with handling null exception errors,
     * since users are able to proceed in program with nothing
     * selected on the necessary tableview element
     * returns null, but shows alert to user when called
     * prompting user to making all necessary selections
     * required in the action at hand
     * **/
    public void getTableError(){
        System.out.println("waiting for information");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.noItem);
        alert.setHeaderText(this.selectItem);
        alert.setContentText("Please choose all necessary information for this operation");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        alert.showAndWait();
    }

    public void getDataTypeError(){
        System.out.println("waiting for information");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.titleWrongFormat);
        alert.setHeaderText(this.headerUseNumerals);
        alert.setContentText("Please use only whole numbers for grade or credits field");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());
        alert.showAndWait();
    }    
}
