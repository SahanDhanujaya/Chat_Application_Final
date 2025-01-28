package lk.ijse.chat_application_final.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Base64;

public class ClientFormController {
    public static String userName;
    public Label lblUserName;
    public TextField txtMessage;
    public Button btnSend;
    public Button btnImoji;
    public TextArea txtChatWall;
    public Button btnFile;
    public ScrollPane scrollPane;
    public AnchorPane vBox;
    public VBox vBox1;
    public Button btnDisconnect;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String message;

    public void initialize(){
        lblUserName.setText(userName);
        String text = txtMessage.getText();
        message = text;
        connectToServer();
        messageReciever();
    }

    private void messageReciever() {
        Thread messageReceiver = new Thread(()->{
            try {

                while (true) {
                    Label lblName = new Label();
                    lblName.setStyle("-fx-background-color: black;-fx-effect: shadow; -fx-font-size: 13;-fx-text-fill: #ffff;-fx-alignment: center;-fx-padding: 5px;");

                    Label lblMessage = new Label();
                    lblMessage.setStyle("-fx-background-color: black;-fx-font-size: 16;-fx-text-fill: #ffff;-fx-alignment: center;-fx-padding: 5px;");

                    ImageView imgReserved = new ImageView();
                    imgReserved.setFitWidth(200);
                    imgReserved.setPreserveRatio(true);

                    String[] data = bufferedReader.readLine().split("/#sendingClientName#/");
                    String clientName = data[0];
                    String message = data[1];
                    if (message != null) {
                        Platform.runLater(() -> {
                            HBox hBoxMessage = new HBox();
                            hBoxMessage.setSpacing(10);
                            hBoxMessage.setStyle("-fx-alignment: baseline-left;-fx-pref-width: 584;-fx-padding: 10px 0 0 0");

                            lblName.setText(clientName);
                            if (message.startsWith("Image:")) {
                                byte[] imageData = Base64.getDecoder().decode(message.substring(6));
                                imgReserved.setImage(new Image(new ByteArrayInputStream(imageData)));

                                HBox hBoxImage = new HBox(imgReserved);
                                hBoxImage.setStyle("-fx-background-color: black;-fx-background-radius:15;-fx-alignment: center;-fx-padding: 20px 5px;");

                                hBoxMessage.getChildren().addAll(lblName, hBoxImage);
                            } else {
                                lblMessage.setText(message);
                                hBoxMessage.getChildren().addAll(lblName, lblMessage);
                            }
                            vBox1.getChildren().add(hBoxMessage);
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        messageReceiver.setDaemon(true);
        messageReceiver.start();
    }

    private void connectToServer() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println(userName + " Connected to server");
            System.out.println("Socket created : "+socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void txtMessageOnAction(ActionEvent actionEvent) {
        btnSendOnAction(actionEvent);
    }

    private void sendMessage(String message, HBox hBox) throws IOException {
        hBox.setStyle("-fx-alignment: center-right;-fx-pref-width: 584;-fx-padding: 10px 0 0 0");
        vBox1.getChildren().add(hBox);
        message = lblUserName.getText()+"/#sendingClientName#/"+message;
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    @FXML
    public void btnSendOnAction(ActionEvent actionEvent) {
        String message = txtMessage.getText();
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-background-color: GRAY;-fx-font-size: 16;-fx-alignment: center;-fx-padding: 5px;");

        if (!message.isEmpty()) {
            try {
                sendMessage(message, new HBox(messageLabel));
                txtMessage.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void btnImojiOnAction(ActionEvent actionEvent) {
        String selectedEmoji = "😍";
        txtMessage.setText(selectedEmoji);
    }

    public void btnFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                byte[] imageData = Files.readAllBytes(file.toPath());
                String encodedImage = Base64.getEncoder().encodeToString(imageData);
                String message = "Image:" + encodedImage;
                ImageView imageView = new ImageView(new Image(file.getPath()));
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);

                HBox imageHbox = new HBox(imageView);
                imageHbox.setStyle("-fx-border-color: black;-fx-background-radius:15;-fx-alignment: center;-fx-padding: 20px 5px;");

                sendMessage(message, new HBox(imageHbox));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnDisconnectOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) txtMessage.getScene().getWindow();
        Label lblName = new Label();
        sendMessage("I'm Disconnected",new HBox(new Label("I'm Disconnected")));
        stage.close();
    }
}
