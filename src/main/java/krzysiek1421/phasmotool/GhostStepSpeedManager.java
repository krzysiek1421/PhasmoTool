package krzysiek1421.phasmotool;
import java.util.ArrayList;
import java.util.List;

public class GhostStepSpeedManager {
    private final List<Long> stepTimestamps = new ArrayList<>();
    private static final double STEPS_PER_MINUTE_TO_METERS_PER_SECOND = 0.93 / 60;

    public void recordStep() {
        stepTimestamps.add(System.currentTimeMillis());
    }

    public double calculateSpeed() {
        if (stepTimestamps.size() < 2) {
            return 0;
        }

        long timeDifference = stepTimestamps.get(stepTimestamps.size() - 1) - stepTimestamps.get(0);
        double timeInMinutes = timeDifference / 60000.0;
        double stepsPerMinute = (stepTimestamps.size() - 1) / timeInMinutes;

        return stepsPerMinute * STEPS_PER_MINUTE_TO_METERS_PER_SECOND;
    }

    public void resetMeasurements() {
        stepTimestamps.clear();
    }
}
