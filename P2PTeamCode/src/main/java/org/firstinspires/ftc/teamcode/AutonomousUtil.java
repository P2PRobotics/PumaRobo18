package org.firstinspires.ftc.teamcode;

public class AutonomousUtil {
    static double adjustedSpeed(double speed) {
        if (speed < -1.0) return -1.0;
        else if (speed > 1.0) return 1.0;
//        else if (Math.abs(error(targetAngle,angles.firstAngle)) > (((targetAngle-1)/targetAngle)*targetAngle)/targetAngle &&
//                Math.abs(error(targetAngle,angles.firstAngle)) < (((targetAngle+1)/targetAngle)*targetAngle)/targetAngle) return 0;
        else if (speed < 0.2 && speed > 0) return 0.2;
        else if (speed > -0.2 && speed < 0) return -0.2;
        else return speed;
        //Adjust speed so it doesn't slow down too much!!!!!!!
    }

    static double angularDifference(double present, double target) {
        // returns the shortest angular difference
        if (target > 180 || target < -180 || present > 180 || present < -180)
            throw new RuntimeException("Angle has to be between -180 and 180");
        present = present + 180;
        target = target + 180;
        double firstDifference = target - present;
        double secondDifference = firstDifference - 360;
        if (Math.abs(firstDifference) < Math.abs(secondDifference))
            return firstDifference;
        else
            return secondDifference;
    }

    static boolean closeEnough(double present, double target, double tolerance) {
        return Math.abs(angularDifference(present, target)) <= Math.abs(tolerance);
    }

    static double rotatedBy(double present, double offset) {
        double tmp = ( present + offset + 180 ) % 360;
        if (tmp < 0) tmp = 360 + tmp;
        return tmp - 180;
    }
}
