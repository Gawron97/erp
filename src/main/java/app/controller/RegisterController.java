package app.controller;

import app.factory.PopUpFactory;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private PopUpFactory popUpFactory; // napewno bedzie okno info. z potwierdzeniem lub niepowodzeniem rejestracji

    public RegisterController(){
        popUpFactory = new PopUpFactory();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




}
