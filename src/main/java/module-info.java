module krzysiek.phasmotool {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires java.logging;


    opens krzysiek1421.phasmotool to javafx.fxml;
    exports krzysiek1421.phasmotool;
}