package lk.ijse.chat_application_final.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    public TextField txtUserName;
    public AnchorPane rootNode;
    public Button btnJoin;

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        try {
            btnJoinOnAction(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnJoinOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Chat Room");
        ClientFormController.userName = txtUserName.getText();
        Parent root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/view/clientForm.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
