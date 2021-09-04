package com.joshuaomolegan.palletorganiser.controllers;

import com.joshuaomolegan.palletorganiser.model.Context;
import com.joshuaomolegan.palletorganiser.model.Organiser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Random;

import java.io.IOException;

public class BoxDisplayController {

    class SmartGroup extends Group {

        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }

    double WIDTH;
    double HEIGHT;
    int numBoxes;
    boolean arrangementExists;

    Organiser organiser;
    Box pallet;
    SmartGroup group;

    //Tracks drag starting point for x and y
    private double anchorX, anchorY;
    //Keep track of current angle for x and y
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    //We will update these after drag. Using JavaFX property to bind with object
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @FXML
    SubScene subScene;

    @FXML
    Button next;

    @FXML
    Label done;

    @FXML
    public void initialize() {
        organiser = Context.getInstance().currentOrganiser();
        numBoxes = organiser.getNumBoxes();
        arrangementExists = organiser.arrangeBoxes();

        WIDTH = subScene.getWidth();
        HEIGHT = subScene.getHeight();

        pallet = new Box();
        pallet.setWidth(organiser.getPalletWidth());
        pallet.setDepth(organiser.getPalletDepth());
        pallet.setHeight(organiser.getPalletHeight());
        pallet.getTransforms().add(new Rotate(180, Rotate.X_AXIS));

        PhongMaterial grey = new PhongMaterial();
        grey.setDiffuseColor(Color.DARKGREY);
        pallet.setMaterial(grey);

        group = new SmartGroup();
        group.translateXProperty().set(WIDTH/2);
        group.translateYProperty().set(HEIGHT/2);
        group.getChildren().add(pallet);

        subScene.setRoot(group);
        subScene.setFill(Color.LIGHTSKYBLUE);

        Camera camera = new PerspectiveCamera();
        subScene.setCamera(camera);

        initMouseControl(group, subScene);
    }

    private void initMouseControl(SmartGroup group, SubScene subScene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        subScene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        subScene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        subScene.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY();
            //Add it to the Z-axis location.
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    public void boxDisplayNext(ActionEvent event) {
        int[] boxDim = organiser.getNextResult();

        if (!arrangementExists){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No valid arrangement can be found");
            alert.setContentText("Please try with different boxes");

            alert.showAndWait();

            Stage stage = (Stage) subScene.getScene().getWindow();
            stage.close();
            return;
        }

        if (numBoxes > 0) {
            int width = boxDim[1] - boxDim[0];
            int depth = boxDim[3] - boxDim[2];
            int height = boxDim[5] - boxDim[4];

            Box currentBox = new Box();
            currentBox.setWidth(width);
            currentBox.setDepth(depth);
            currentBox.setHeight(height);

            // Generate random rgb value
            Random rand = new Random(System.currentTimeMillis());
            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);

            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.rgb(red, green, blue));
            currentBox.setMaterial(material);

            // Space array treats (0, 0) as top left corner so translate points
            double x = boxDim[0] - organiser.getPalletWidth()/2 + width/2;
            double y = -(boxDim[4] + organiser.getPalletHeight()/2 + height/2);
            double z = boxDim[2] - organiser.getPalletDepth()/2 + depth/2;

            currentBox.getTransforms().add(new Translate(x, y, z));
            group.getChildren().add(currentBox);
            numBoxes -= 1;

            if (numBoxes == 0) {
                done.setVisible(true);
                next.setText("Close");
            }

        } else {
            Stage stage = (Stage) subScene.getScene().getWindow();
            stage.close();
        }
    }

}
