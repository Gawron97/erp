package app.controller;

import app.dto.OperatorRegisterCredentialsDto;
import app.factory.PopUpFactory;
import app.rest.Authenticator;
import app.rest.AuthenticatorImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private static final String LOGIN_FXML = "/fxml/login.fxml";

    private PopUpFactory popUpFactory; // napewno bedzie okno info. z potwierdzeniem lub niepowodzeniem rejestracji
    private Authenticator authenticator;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField loginTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private TextField peselTF;

    @FXML
    private Button registerButton;

    public RegisterController(){
        popUpFactory = new PopUpFactory();
        authenticator = new AuthenticatorImpl();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeRegisterButton();
    }

    private void initializeRegisterButton() {
        registerButton.setOnAction(actionEvent -> {
            performRegistrationAuthentication();
        });
    }

    private void performRegistrationAuthentication() {

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Tworzymy konto");
        waitingPopUp.show();

        String pesel = peselTF.getText();
        String login = loginTF.getText();
        String password = passwordTF.getText();

        OperatorRegisterCredentialsDto dto = OperatorRegisterCredentialsDto.of(pesel, login, password);
        authenticator.authenticateRegister(dto, authenticationResult -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                if (authenticationResult)
                    OpenLoginPanelCloseRegisterPage();
                else
                    ShowIncorrectDataMessage();
            });

        });
    }

    private void ShowIncorrectDataMessage() {
        System.out.println("zle dane przy rejestrowaniu");
    }

    private void OpenLoginPanelCloseRegisterPage() {
        try{
            Stage login = new Stage();
            login.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(LOGIN_FXML)), 600, 400);
            login.setScene(scene);
            getStage().close();
            login.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }


}
