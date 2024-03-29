package app.factory;

import app.handler.ButtonInitializer;
import app.handler.OnEndedAction;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopUpFactory {

    public Stage createWaitingPopUp(String text){
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();
        vBox.setStyle(popUpStyle());
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label label = new Label(text);
        label.setStyle(labelStyle());

        ProgressBar progressBar = new ProgressBar();
        vBox.getChildren().add(label);
        vBox.getChildren().add(progressBar);

        stage.setScene(new Scene(vBox, 200, 100));

        return stage;
    }

    private String labelStyle() {

        return "-fx-text-fill: #810aa6;";

    }

    private String popUpStyle() {

        return "-fx-background-color: #1367b5; -fx-border-color: #003366;";

    }

    private String okButtonStyle(){
        return "-fx-text-fill: #003366;\n" +
                "-fx-background-color: #1d9081;\n" +
                "-fx-border-color: #88bf41;\n" +
                "-fx-background-radius: 0;";
    }


    private String okButtonHoverStyle(){
        return "-fx-background-color: #339b8d;";
    }


    public Stage createInfoPopUp(String text, ButtonInitializer initializer) {

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        VBox vBox = new VBox();
        vBox.setStyle(popUpStyle());
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label label = new Label(text);
        label.setStyle(labelStyle());

        Button okButton = new Button("OK");

        okButton.setStyle(okButtonStyle());
        okButton.setOnMouseEntered(mouseEvent -> okButton.setStyle(okButtonHoverStyle()));
        okButton.setOnMouseExited(mouseEvent -> okButton.setStyle(okButtonStyle()));
        okButton.setOnAction(actionEvent -> {
            stage.close();
            initializer.init();
        });

        vBox.getChildren().addAll(label, okButton);

        stage.setScene(new Scene(vBox, 200, 100));
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }

    public Stage createErrorPopUp(String text, ButtonInitializer initializer){

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        VBox vBox = new VBox();
        vBox.setStyle(popUpStyle());
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label label = new Label(text);
        label.setStyle(labelStyle());

        Button okButton = new Button("OK");

        okButton.setStyle(okButtonStyle());
        okButton.setOnMouseEntered(mouseEvent -> okButton.setStyle(okButtonHoverStyle()));
        okButton.setOnMouseExited(mouseEvent -> okButton.setStyle(okButtonStyle()));
        okButton.setOnAction(actionEvent -> {
            stage.close();
            initializer.init();
        });

        vBox.getChildren().addAll(label, okButton);

        stage.setScene(new Scene(vBox, 200, 100));
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;

    }

    public Stage createErrorPopUp(String text){

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        VBox vBox = new VBox();
        vBox.setStyle(popUpStyle());
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label label = new Label(text);
        label.setStyle(labelStyle());

        Button okButton = new Button("OK");

        okButton.setStyle(okButtonStyle());
        okButton.setOnMouseEntered(mouseEvent -> okButton.setStyle(okButtonHoverStyle()));
        okButton.setOnMouseExited(mouseEvent -> okButton.setStyle(okButtonStyle()));
        okButton.setOnAction(actionEvent -> {
            stage.close();
        });

        vBox.getChildren().addAll(label, okButton);

        stage.setScene(new Scene(vBox, 200, 100));
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;

    }
    //TODO refactor?

}
