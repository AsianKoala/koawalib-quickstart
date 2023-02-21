package teamcode.rr.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;

/*
 * Constants shared between multiple drive types.
 *
 * TODO: Tune or adjust the following constants to fit your robot. Note that the non-final
 * fields may also be edited through the dashboard (connect to the robot's WiFi network and
 * navigate to https://192.168.49.1:8080/dash). Make sure to save the values here after you
 * adjust them in the dashboard; **config variable changes don't persist between app restarts**.
 *
 * These are not the only parameters; some are located in the localizer classes, drive base classes,
 * and op modes themselves.
 */
@Config
public class DriveConstants {
    public static final boolean RUN_USING_ENCODER = false;

    public static double TRACK_WIDTH = 13.93; // in

    public static double kV = 0.015;
    public static double kA = 0.0025;
    public static double kStatic = 0.107;

    public static double MAX_VEL = 55;
    public static double MAX_ACCEL = 55;
    public static double MAX_ANG_VEL = Math.toRadians(180);
    public static double MAX_ANG_ACCEL = Math.toRadians(180);

//    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(16.5, 0, 1.0);
//    public static PIDCoefficients HEADING_PID = new PIDCoefficients(14.0, 0, 0.5);

    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(8.0, 0.0, 0.3);
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(8.0, 0, 0.3);

    public static double encoderTicksToInches(double ticks) {
        return ticks / 1892.368643585353;
    }
}
