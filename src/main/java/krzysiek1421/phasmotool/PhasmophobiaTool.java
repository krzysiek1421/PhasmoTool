package krzysiek1421.phasmotool;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Application;
import javafx.scene.Scene;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static org.jnativehook.NativeInputEvent.CTRL_MASK;

public class PhasmophobiaTool extends Application {
    private final GhostStepSpeedManager ghostStepSpeedManager = new GhostStepSpeedManager();
    private Label ghostSpeedLabel;
    private TimerManager smudgeTimerManager;
    private TimerManager crucifixTimerManager;
    private final Logger toolLogger = Logger.getLogger(String.valueOf(PhasmophobiaTool.class));

    @Override
    public void start(Stage primaryStage) {
        Label smudgeTimerLabel;
        Label crucifixTimerLabel;
        // Turn off logging JNativeHook
        disableNativeHookLogging();

        // Smudge Timer Label
        smudgeTimerLabel = new Label("Smudge Timer: 00:00");
        smudgeTimerLabel.setStyle("-fx-font-size: 32; -fx-text-fill: lightgreen;");

        // Smudge Timer Manager
        smudgeTimerManager = new TimerManager(() -> updateTimerLabel(smudgeTimerLabel, "Smudge Timer: ", smudgeTimerManager));



        // Crucifix Timer Label
        crucifixTimerLabel = new Label("Crucifix Timer: 00:00");
        crucifixTimerLabel.setStyle("-fx-font-size: 32; -fx-text-fill: red;");

        // Crucifix Timer Manager
        crucifixTimerManager = new TimerManager(() -> updateTimerLabel(crucifixTimerLabel, "Crucifix Timer: ", crucifixTimerManager));


        ghostSpeedLabel = new Label("Ghost Speed: 0.0 m/s");
        ghostSpeedLabel.setStyle("-fx-font-size: 32; -fx-text-fill: orange;");
        // Layout
        VBox layout = new VBox(10, smudgeTimerLabel, crucifixTimerLabel, ghostSpeedLabel);
        layout.setStyle("-fx-background-color: transparent;");
        setupGlobalHotkey();

        //Set scene and window
        setupStage(primaryStage, layout);

        primaryStage.show();
    }


    private void updateSpeedDisplay(double speed) {
        Platform.runLater(() -> ghostSpeedLabel.setText(String.format("Ghost Speed: %.2f m/s", speed)));
    }


    private void positionStageOnScreen(Stage stage, Scene scene) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = visualBounds.getWidth();
        stage.setX(screenWidth - scene.getWidth() - 20);
        stage.setY(20);
    }
    private void setupStage(Stage stage, VBox layout) {
        Scene scene = new Scene(layout, 350, 200);
        scene.setFill(null);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setScene(scene);
        positionStageOnScreen(stage, scene);
    }
    private void disableNativeHookLogging() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }
    private void updateTimerLabel(Label label, String prefix, TimerManager manager) {
        label.setText(prefix + manager.getCurrentTime());
    }

    private void setupGlobalHotkey() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            toolLogger.warning("There was a problem registering the native hook.");
            toolLogger.warning(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_T && (e.getModifiers() & CTRL_MASK) != 0) {
                    smudgeTimerManager.handleTimerAction();
                } else if (e.getKeyCode() == NativeKeyEvent.VC_F && (e.getModifiers() & CTRL_MASK) != 0) {
                    crucifixTimerManager.handleTimerAction();
                }

                if (e.getKeyCode() == NativeKeyEvent.VC_BACKQUOTE) {
                    if ((e.getModifiers() & CTRL_MASK) != 0) {
                        ghostStepSpeedManager.resetMeasurements();
                        // Reset speed in gui
                    } else {
                        ghostStepSpeedManager.recordStep();
                        //Update speed in GUI
                        double speed = ghostStepSpeedManager.calculateSpeed();
                        updateSpeedDisplay(speed);
                    }
                }

            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
                //Implementation not necessary for this application
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
                //Implementation not necessary for this application
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        GlobalScreen.unregisterNativeHook();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
