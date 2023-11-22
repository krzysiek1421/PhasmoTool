package krzysiek1421.phasmotool;

import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private Timer timer;
    private int secondsPassed = 0;
    private final Runnable updateAction;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public TimerManager(Runnable updateAction) {
        this.updateAction = updateAction;
    }

    public void handleTimerAction() {
        if (!isRunning) {
            startTimer();
        } else if (!isPaused) {
            pauseTimer();
        } else {
            resumeTimer();
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        secondsPassed = 0;
        isRunning = true;
        isPaused = false;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
                Platform.runLater(updateAction);
            }
        }, 1000, 1000);
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.cancel();
            isPaused = true;
        }
    }

    private void resumeTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        secondsPassed = 0;
        isPaused = false;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
                Platform.runLater(updateAction);
            }
        }, 1000, 1000);
    }

    public String getCurrentTime() {
        return String.format("%02d:%02d", secondsPassed / 60, secondsPassed % 60);
    }
}
