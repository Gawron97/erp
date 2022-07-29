package app.controller;

import app.dto.OperatorCredentialsDto;
import app.factory.PopUpFactory;
import app.rest.Authenticator;
import app.rest.AuthenticatorImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private PopUpFactory popUpFactory;

    private Authenticator authenticator;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;

    public LoginController(){
        popUpFactory = new PopUpFactory();
        authenticator = new AuthenticatorImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeExitButton();
        initializeLoginButton();
    }

    private void initializeLoginButton() {
        loginButton.setOnAction(actionEvent -> {
            performAuthentication();
        });
    }

    private void performAuthentication() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Connecting to the server...");
        waitingPopUp.show();

        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        OperatorCredentialsDto dto = new OperatorCredentialsDto();
        dto.setLogin(login);
        dto.setPassword(password);

        authenticator.authenticate(dto, authenticationResult -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                System.out.println(authenticationResult.toString());
            });
        });



    }

    private void initializeExitButton(){
        exitButton.setOnAction(actionEvent -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return ((Stage) loginAnchorPane.getScene().getWindow());

    }
}
