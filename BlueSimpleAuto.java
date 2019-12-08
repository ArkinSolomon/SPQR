package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.lang.System.*;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.pow;

@Autonomous(name="Blue Simple Auto")
public class BlueSimpleAuto extends LinearOpMode {
    private boolean isInitialized = false;
    private HardwareSPQR robot = new HardwareSPQR();
    public long ninetyDegreeTime = 750;

    private double speed = -0.5;
    @Override
    public void runOpMode() {
        if (!isInitialized) {
            isInitialized = true;
            this.robot.init(hardwareMap);
//            this.robot.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        waitForStart();

        if(opModeIsActive() && !isStopRequested()) {
            this.robot.forwardTime(3000, speed);
            this.robot.turnTime("left", ninetyDegreeTime, speed);
            this.robot.forwardTime(8000, speed);
            this.robot.turnTime("right",ninetyDegreeTime , speed);
            this.robot.tow.setPosition(1);
            }
        }
    }

