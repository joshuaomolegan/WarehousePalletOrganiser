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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import java.io.IOException;

public class BoxDimensionsController {

    @FXML
    TextField labelTextField;

    @FXML
    TextField lengthTextField;

    @FXML
    TextField widthTextField;

    @FXML
    TextField heightTextField;

    @FXML
    Label boxAddedLbl;

    private int boxCount;

    String boxLabel;
    int boxLength;
    int boxWidth;
    int boxHeight;

    Organiser organiser;

    @FXML
    public void initialize() {
        organiser = Context.getInstance().currentOrganiser();
    }


    private boolean validateInput() {
        String lengthText = lengthTextField.getText();
        String widthText = widthTextField.getText();
        String heightText = heightTextField.getText();
        boxLabel = labelTextField.getText();

        if (lengthText.trim().isEmpty() || widthText.trim().isEmpty() || heightText.trim().isEmpty() || boxLabel.trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Null input detected!");
            alert.setContentText("Please make sure all boxes are filled before moving on.");

            alert.showAndWait();
            return false;
        }

        try {
            boxLength = Integer.parseInt(lengthTextField.getText());
            boxWidth = Integer.parseInt(widthTextField.getText());
            boxHeight = Integer.parseInt(heightTextField.getText());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Non-numeric characters detected!");
            alert.setContentText("Please only enter numbers in the length, width and height boxes.");

            alert.showAndWait();
            return false;
        }

        return true;
    }

    public void boxDimensionsNext(ActionEvent event) throws IOException {
        boolean isInputValid = this.validateInput();
        if (!isInputValid) return;

        organiser.addBox(boxLabel, boxLength, boxWidth, boxHeight);

        labelTextField.clear();
        lengthTextField.clear();
        widthTextField.clear();
        heightTextField.clear();

        boxAddedLbl.setText(boxLabel + " was added successfully");
        boxCount += 1;

        if (boxCount == organiser.getNumBoxes()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/joshuaomolegan/palletorganiser/fxml/boxdisplay.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}