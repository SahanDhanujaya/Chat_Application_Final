module lk.ijse.chat_application_final {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens lk.ijse.chat_application_final.Controller to javafx.fxml;
    exports lk.ijse.chat_application_final;
}