package sample;

import javafx.application.Application;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


import java.io.*;
import java.util.*;

import javafx.application.Platform;

import javafx.scene.shape.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Popup on start
        Platform.setImplicitExit(false);
        Stage popup = new Stage();
        popup.setTitle("Select");

        FileChooser fileChooser = new FileChooser();

        Button closeButton = new Button("Finish labelling");
        Button openNextButton = new Button("Open next picture"); // Not implemented
        Button openPrevButton = new Button("Open previous picture"); // Not implemented
        Button beginButton = new Button("Begin labelling");
        TextField categoryField = new TextField ("Type in current category here"); // Category input

        RadioButton radioYOLO = new RadioButton("YOLO");
        RadioButton radioVOC = new RadioButton("VOC");
        RadioButton radioCOCO = new RadioButton("COCO");
        RadioButton radioImageNet = new RadioButton("ImageNet");
        RadioButton radioOpenImages = new RadioButton("OpenImages");

        ToggleGroup formatGroup = new ToggleGroup();
        radioCOCO.setToggleGroup(formatGroup);
        radioImageNet.setToggleGroup(formatGroup);
        radioOpenImages.setToggleGroup(formatGroup);
        radioVOC.setToggleGroup(formatGroup);
        radioYOLO.setToggleGroup(formatGroup);

        VBox vbox = new VBox(radioCOCO, radioImageNet, radioOpenImages, radioVOC, radioYOLO, closeButton, openNextButton, openPrevButton, beginButton, categoryField);

        Stage imageStage = new Stage(); // Creating new stage so I can showAndWait

        beginButton.setOnAction((event) -> {
            File imageFolder = new File("images/");
            File[] listOfImages = imageFolder.listFiles();
            for (int i = 0; i < listOfImages.length; i++) { // Iterate over all images images directory
                StackPane sp = new StackPane();
                // Load in current image, exception bc may be null
                File currentFile = listOfImages[i];
                Image currentImage = new Image("file:" + listOfImages[i].toString());
                imageStage.setTitle("Image " + listOfImages[i].getName());
                ImageView viewCurrentImage = new ImageView(currentImage);
                sp.getChildren().add(viewCurrentImage);

                ArrayList<Double> coordinates = new ArrayList<>();

                viewCurrentImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent me) {
                        coordinates.add(me.getX());
                        coordinates.add(me.getY());
                        System.out.println(coordinates.toString() + ", ");

                        if (coordinates.size() % 4 == 0 && coordinates.size() != 0) { // Needs to be modified for continuous rendering
                            Rectangle r = new Rectangle();
                            r.setWidth(RectangleCreator.rWidth((coordinates.get(coordinates.size() - 4)), coordinates.get(coordinates.size() - 2)));
                            r.setHeight(RectangleCreator.rHeight((coordinates.get(coordinates.size() - 3)), coordinates.get(coordinates.size() - 1)));
                            r.setTranslateX(RectangleCreator.rX((coordinates.get(coordinates.size() - 4)), (coordinates.get(coordinates.size() - 2)), sp.getWidth(), r.getWidth()));
                            r.setTranslateY(RectangleCreator.rY((coordinates.get(coordinates.size() - 3)), (coordinates.get(coordinates.size() - 1)), sp.getHeight(), r.getHeight()));
                            r.setFill(Color.TRANSPARENT);
                            r.setStroke(Color.color(Math.random(), Math.random(), Math.random())); // Needs to be implemented by category
                            r.setStrokeWidth(5);
                            sp.getChildren().add(r);
                        }
                    }
                });

                openNextButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        try {
                            FileWriter writer = new FileWriter(new File("output/output" + currentFile.getName().substring(0, currentFile.getName().indexOf('.')) + ".txt"));
                            for (Double k : coordinates) {
                                writer.write(String.valueOf(k) + " "); // Write the labels to a text file
                            }
                            writer.close();
                            imageStage.hide();
                        } catch (IOException ex) {
                            imageStage.hide();
                        }
                    }
                });


                Scene scene = new Scene(sp);
                scene.setCursor(Cursor.CROSSHAIR);
                imageStage.setResizable(false); // Will be changed once I figure out scaling
                imageStage.setScene(scene);
                imageStage.showAndWait(); // Ensures that stage waits for user input
            }

        });

        popup.setOnCloseRequest((event) -> {
            System.exit(0);
        });

        closeButton.setOnAction((event) -> {
            System.exit(0);
        });

        Scene popupScene = new Scene(vbox);
        popup.setScene(popupScene);
        popup.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
