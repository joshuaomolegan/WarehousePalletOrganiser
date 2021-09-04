package com.joshuaomolegan.palletorganiser.controllers;


import com.joshuaomolegan.palletorganiser.model.Context;
import com.joshuaomolegan.palletorganiser.model.Organiser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PalletDimensionsController {
    @FXML
    TextField lengthTextField;

    @FXML
    TextField widthTextField;

    @FXML
    TextField boxnumTextField;

    private int numBoxes;
    int palletDim1;
    int palletDim2;

    Organiser organiser;

    @FXML
    public void initialize() { organiser = Context.getInstance().currentOrganiser(); }

    private boolean validateInput() {
        String boxnumText = boxnumTextField.getText();
        String lengthText = lengthTextField.getText();
        String widthText = widthTextField.getText();

        if (boxnumText.trim().isEmpty() || lengthText.trim().isEmpty() || widthText.trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Null input detected!");
            alert.setContentText("Please make sure all boxes are filled before moving on.");

            alert.showAndWait();
            return false;
        }

        try {
            numBoxes = Integer.parseInt(boxnumText);
            palletDim1 = Integer.parseInt(lengthText);
            palletDim2 = Integer.parseInt(widthText);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Non-numeric characters detected!");
            alert.setContentText("Please only enter numbers in the length, width and numBoxes boxes.");

            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void palletDimensionsNext(ActionEvent event) throws IOException {
        boolean isInputValid = this.validateInput();
        if (!isInputValid) return;

        organiser.setPalletWidth(palletDim1);
        organiser.setPalletDepth(palletDim2);
        organiser.setPalletHeight(20); // Arbitrary value

        organiser.setNumBoxes(numBoxes);

        Parent root = FXMLLoader.load(getClass().getResource("/com/joshuaomolegan/palletorganiser/fxml/boxdimensions.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
