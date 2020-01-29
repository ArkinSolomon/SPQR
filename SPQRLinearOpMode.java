package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Custom Linear OpMode class with extra functions.
 *
 * @author Arkin Solomon
 * @author Owen Peterson
 */
public abstract class SPQRLinearOpMode extends LinearOpMode {

    //Variables
    public final double speed = 0.75;
    //public final double turnSpeed = 1;
    //public final long ninetyDegreeRightTime = 990;
    //public final long ninetyDegreeLeftTime = 970;

    //The switch on the robot that dictates what color it is. (red or blue)

    public final double ppr = 280;
    public final double degppr = 21.25;
    public final double powerScalar = 1.25;

    private boolean teamSwitch;
    private Color teamColor;

    //The switch on the robot that determines the starting posistion (front or back)
    private boolean positionSwitch;
    private Position position;

    //The switch on the robot that determines which lane it will primarily travel on (inside, outside)
    private boolean laneSwitch;
    private Lane lane;

    //The switch on the robot that determines whether to try and pull the foundation into the area
    private boolean foundationSwitch;

    //The switch on the robot that determines whether to try and pickup a block
    private boolean blockSwitch;

    //Values for the wheels
    private final double wheelRadius = 5*25.4;
    private final double wheelCircumference = wheelRadius * 2 * Math.PI;

    private int leftFrontEncoder;
    private int rightFrontEncoder;
    private int leftBackEncoder;
    private int rightBackEncoder;


    //Intialize hardware
    public HardwareSPQR robot = new HardwareSPQR();
    public void hardwareInit(){
        this.robot.init(hardwareMap);
    }

    //Makes these easier to type
    public void grabBlock(){
        this.robot.grabBlock();
    }
    public void releaseBlock(){
        this.robot.releaseBlock();
    }

    //Stops the robot over the tape
    public void stopAtTape(int[] tapeColor, int change){
        this.robot.setPowers(.6);
        boolean isOnLine = false;
        while (!isOnLine && this.opModeIsActive()) {
            int[] r = this.plusOrMinus(this.robot.lineParkSensor.red(), change);
            int[] g = this.plusOrMinus(this.robot.lineParkSensor.green(), change);
            int[] b = this.plusOrMinus(this.robot.lineParkSensor.blue(), change);
            isOnLine = (((tapeColor[0] > r[0]) && (tapeColor[0] < r[1])) && ((tapeColor[1] > g[0]) && (tapeColor[1] < g[1])) && ((tapeColor[2] > b[0]) && (tapeColor[2] < b[1])));
        }
        this.robot.lineParkSensor.enableLed(false);
        this.robot.stopMoving();
    }
    //Calculates distance robot has traveled
    public double calculateDistance(){
      double encoder = this.driveAverage();
      return (encoder / ppr) * wheelCircumference;
    }

    public double getAverageEncoder(){
        int[] encoderPositions = {this.robot.leftFrontDrive.getCurrentPosition(), this.robot.rightFrontDrive.getCurrentPosition(), this.robot.leftBackDrive.getCurrentPosition(), this.robot.rightBackDrive.getCurrentPosition()};
        int sum = 0;
        for (int encoderPosition : encoderPositions){
            sum += Math.abs(encoderPosition);
        }
        return sum / encoderPositions.length;
    }

    public double calculateDistance(double start){
        return calculateDistance() - start;
    }

    //Returns an array plus or minus a change given of a value given
    public int[] plusOrMinus(int value, int change){
        return new int[] {value - change, value + change};
    }

    public void turn (double angle, double speed){
        this.robot.setDrivesBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetEncoders();
        double encoderLimit = Math.abs(degppr*angle);
    if (angle > 0) {
        this.robot.leftFrontDrive.setPower(speed);
        this.robot.leftBackDrive.setPower(speed);
        this.robot.rightFrontDrive.setPower(-speed);
        this.robot.rightBackDrive.setPower(-speed);
        } else if (angle < 0) {
        this.robot.leftFrontDrive.setPower(-speed);
        this.robot.leftBackDrive.setPower(-speed);
        this.robot.rightFrontDrive.setPower(speed);
        this.robot.rightBackDrive.setPower(speed);
    }
    while (encoderLimit > getAverageEncoder() && !isStopRequested() && opModeIsActive()){ //
        updateTelemetry();
        checkRate(Orientation.VERTICAL, speed);
        limitRate(encoderLimit);
    }
        this.robot.stopMoving();
        this.sleep(10000);
    }

    public void drive (double distance, double speed){
        this.robot.setDrivesBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetEncoders();
        double encoderLimit = (distance/wheelCircumference)*ppr;
        this.robot.setPowers(speed);
        while(calculateDistance() < distance && !isStopRequested() && opModeIsActive()){
            updateTelemetry();
            checkRate(Orientation.HORIZONTAL, speed);
            limitRate(encoderLimit);
        }
        this.robot.stopMoving();
        sleep(10000);
    }


    public void limitRate (double limit) {
        int[] encoderPositions = {this.robot.leftFrontDrive.getCurrentPosition(),
                this.robot.rightFrontDrive.getCurrentPosition(),
                this.robot.leftBackDrive.getCurrentPosition(),
                this.robot.rightBackDrive.getCurrentPosition()};
        for (int i = 0; i < encoderPositions.length; i++){
            if (Math.abs(encoderPositions[i]) > limit){
                if (i == 0) {
                    this.robot.leftFrontDrive.setPower(0);
                } else if (i == 1) {
                    this.robot.rightFrontDrive.setPower(0);
                } else if (i == 2) {
                    this.robot.leftBackDrive.setPower(0);
                } else if (i == 3) {
                    this.robot.rightBackDrive.setPower(0);
                }
            }
        }

    }
    public void checkRate (Orientation wheelsCompare, double speed){
        double frontLeft = this.robot.leftFrontDrive.getCurrentPosition();
        double frontRight = this.robot.rightFrontDrive.getCurrentPosition();
        double backLeft = this.robot.leftBackDrive.getCurrentPosition();
        double backRight = this.robot.rightBackDrive.getCurrentPosition();
        double error;
        double power;
        if (wheelsCompare == Orientation.HORIZONTAL){
            power = speed/powerScalar;
            if (frontLeft > frontRight){
                this.robot.leftFrontDrive.setPower(power);
            } else if (frontRight > frontLeft){
                this.robot.rightFrontDrive.setPower(power);
            }
            power = speed/powerScalar;
            if (backLeft > backRight){
                this.robot.leftBackDrive.setPower(power);
            } else if (backRight > backLeft){
                this.robot.rightBackDrive.setPower(power);
            }
        } else if (wheelsCompare == Orientation.VERTICAL){
            power = speed/powerScalar;
            if (frontLeft > backLeft){
                this.robot.leftFrontDrive.setPower(power);
            } else if (backLeft > frontLeft){
                this.robot.leftBackDrive.setPower(power);
            }
            power = speed/powerScalar;
            if (frontRight > backRight){
                this.robot.rightFrontDrive.setPower(power);
                } else if (backRight > frontRight){
                this.robot.rightBackDrive.setPower(power);
            }
        } else if (wheelsCompare == Orientation.DIAGONAL){
            power = speed/this.powerScalar;
            if (frontLeft > backRight){
                this.robot.leftFrontDrive.setPower(power);
            } else if (backRight > frontLeft){
                this.robot.rightBackDrive.setPower(power);
            }
            power = speed/powerScalar;
            if (frontRight > backLeft){
                this.robot.rightFrontDrive.setPower(power);
            } else if (backLeft > frontRight){
                this.robot.leftBackDrive.setPower(power);
            }
        }
    }
    public void resetEncoders(){
        this.leftFrontEncoder += this.robot.leftFrontDrive.getCurrentPosition();
        this.rightFrontEncoder += this.robot.rightFrontDrive.getCurrentPosition();
        this.leftBackEncoder += this.robot.leftBackDrive.getCurrentPosition();
        this.rightBackEncoder += this.robot.rightBackDrive.getCurrentPosition();

        this.robot.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.robot.leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.robot.rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //Get average distance traveled
    public double driveAverage(){
      int[] encoderPositions = {this.robot.leftFrontDrive.getCurrentPosition(), this.robot.rightFrontDrive.getCurrentPosition(), this.robot.leftBackDrive.getCurrentPosition(), this.robot.rightBackDrive.getCurrentPosition()};
      int sum = 0;
      for (int encoderPosition : encoderPositions){
        sum += encoderPosition;
      }
      return sum / encoderPositions.length;
    }
public void updateTelemetry(){
    telemetry.addData("Distance", calculateDistance());
    telemetry.addData("leftFrontTempEncoder", this.robot.leftFrontDrive.getCurrentPosition());
    telemetry.addData("rightFrontTempEncoder", this.robot.rightFrontDrive.getCurrentPosition());
    telemetry.addData("leftBackTempEncoder", this.robot.leftBackDrive.getCurrentPosition());
    telemetry.addData("rightBackTempEncoder", this.robot.rightBackDrive.getCurrentPosition());
    telemetry.addData("leftFrontEncoder", this.leftFrontEncoder);
    telemetry.addData("rightFrontEncoder", this.rightFrontEncoder);
    telemetry.addData("leftBackEncoder", this.leftBackEncoder);
    telemetry.addData("rightBackEncoder", this.rightBackEncoder);
    telemetry.update();
    }

}
