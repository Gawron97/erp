//package app.controller;
//
//import app.factory.PopUpFactory;
//import app.rest.ItemRestClient;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//import org.springframework.http.HttpStatus;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class DeleteItemController implements Initializable {
//
//    private final PopUpFactory popUpFactory;
//    private final ItemRestClient itemRestClient;
//
//    @FXML
//    private BorderPane borderPane;
//
//    @FXML
//    private Button cancelButton;
//
//    private Integer idItem;
//
//
//    public DeleteItemController(){
//        popUpFactory = new PopUpFactory();
//        itemRestClient = new ItemRestClient();
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        initializeCancelButton();
////        initializeDeleteButton();
//    }
//
////    private void initializeDeleteButton() {
////        deleteButton.setOnAction(actionEvent -> {
////            deleteItem();
////        });
////    }
//
////    private void deleteItem(){
////
////        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Usuwamy przedmiot");
////        waitingPopUp.show();
////
////        itemRestClient.deleteItem(idItem, httpStatus -> {
////            Platform.runLater(() -> {
////                waitingPopUp.close();
////                if(httpStatus.equals(HttpStatus.OK)) {
////                    Stage infoPopUp = popUpFactory.createInfoPopUp("Przedmiot zostal usuniety", () -> getStage().close());
////                    infoPopUp.show();
////                }else {
////                    Stage errorPopUp = popUpFactory.createErrorPopUp("Blad przy usuwaniu przedmiotu")
////                }
////            });
////        });
////    }
//
//    private void initializeCancelButton() {
//        cancelButton.setOnAction(actionEvent -> getStage().close());
//    }
//
//    private Stage getStage(){
//        return ((Stage) borderPane.getScene().getWindow());
//    }
//
//    public void saveIdItem(Integer idItem) {
//        this.idItem = idItem;
//    }
//}
