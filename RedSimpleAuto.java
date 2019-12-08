package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static java.lang.Math.abs;

@Autonomous(name="Red Simple Auto")
public class RedSimpleAuto extends SPQRLinearOpMode {

    @Override
    public void runOpMode() {
        this.hardwareInit();

        waitForStart();

        if(opModeIsActive() && !isStopRequested()) {
            this.driveForTime(this.speed, justSpeed(250, this.speed));
            this.turnForTime(Dir.RIGHT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(this.speed, justSpeed(3500, this.speed));
            this.turnForTime(Dir.LEFT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(this.speed, this.justSpeed(1000, this.speed));
            this.robot.tow.setPosition(1);
            this.sleep(1000);
            this.driveForTime(-this.speed, this.justSpeed(2000, this.speed));
            this.robot.tow.setPosition(0);
            this.turnForTime(Dir.LEFT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(this.speed, this.justSpeed(1500, this.speed));
            this.turnForTime(Dir.RIGHT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(speed, this.justSpeed(1000, this.speed));
            this.turnForTime(Dir.RIGHT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(this.speed, this.justSpeed(1500, this.speed));
            this.turnForTime(Dir.RIGHT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(this.speed, this.justSpeed(500, this.speed));
            this.driveForTime(-this.speed, this.justSpeed(250, this.speed));
            this.turnForTime(Dir.RIGHT, this.turnSpeed, justSpeed(this.ninetyDegreeTime, this.turnSpeed));
            this.driveForTime(speed, justSpeed(1250, this.speed));
            if (true){
                this.robot.setPowers(0);
            }
        }
    }
}

