package app.factory;

import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        VBox vBox = new VBox();
        vBox.setStyle(waitingPopUpStyle());
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label label = new Label(text);
        label.setStyle(waitingLabelStyle());

        ProgressBar progressBar = new ProgressBar();
        vBox.getChildren().add(label);
        vBox.getChildren().add(progressBar);

        stage.setScene(new Scene(vBox, 200, 100));
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }

    private String waitingLabelStyle() {

        return "-fx-text-fill: #810aa6;";

    }

    private String waitingPopUpStyle() {

        return "-fx-background-color: #1367b5; -fx-border-color: #003366;";

    }


}
