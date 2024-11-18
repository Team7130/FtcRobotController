package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(group = "Iterative OpMode", name = "Autonomous: Basket")
public class AutoBasket extends OpMode {
    private CRServo hand = null;
    private Servo wrist = null;
    double handpos = 0.0d;
    double wristpos = 0.0d;
    private DcMotor leftDriveBack = null;
    private DcMotor leftDriveFront = null;
    private DcMotor rightDrive = null;
    private DcMotor rightDriveBack = null;
    private DcMotor rightDriveFront = null;

    private DcMotor arm = null;
    private ElapsedTime runtime = new ElapsedTime();
    private double shldIntegral = 0.0d;
    private double shldsetpoint = -50.0d;
    private DcMotor shoulder = null;

    public void init() {
        this.telemetry.addData("Status", (Object) "Initialized");
        this.leftDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "FLMotor");
        this.leftDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "BLMotor");
        this.rightDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "FRMotor");
        this.rightDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "BRMotor");
        this.shoulder = (DcMotor) this.hardwareMap.get(DcMotor.class, "shoulder");
        this.arm = (DcMotor) this.hardwareMap.get(DcMotor.class, "arm");

        this.hand = (CRServo) this.hardwareMap.get(CRServo.class, "hand");

        Servo servo3 = (Servo) this.hardwareMap.get(Servo.class, "wrist");
        this.wrist = servo3;
        servo3.setDirection(Servo.Direction.FORWARD);

        this.leftDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.leftDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.shoulder.setDirection(DcMotorSimple.Direction.REVERSE);
        this.handpos = 0.0d;
        this.wristpos = 0.0d;

        this.shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double currentPosition = (double) this.shoulder.getCurrentPosition();
        this.shldsetpoint = currentPosition;
        this.shldIntegral = currentPosition;
        this.telemetry.addData("Status", (Object) "Initialized: " + this.runtime.toString());
    }

    public void loop() {
        this.wrist.setPosition(0.6d);
        if(shoulder.getCurrentPosition() < 3000) {
            shoulder.setPower(-1);
        } else {
            shoulder.setPower(0);
            int forwardDistance = 1000;
            if (leftDriveFront.getCurrentPosition() > -forwardDistance && rightDriveFront.getCurrentPosition() < forwardDistance) {
                leftDriveFront.setPower(0.5d);
                rightDriveFront.setPower(0.5d);
                leftDriveBack.setPower(-0.5d);
                rightDriveBack.setPower(-0.5d);
            } else{
                leftDriveFront.setPower(0);
                rightDriveFront.setPower(0);
                leftDriveBack.setPower(0);
                rightDriveBack.setPower(0);
                if(arm.getCurrentPosition() < 4000) {
                    arm.setPower(1.0d);
                } else {
                    arm.setPower(0);
                    hand.setPower(1.0d);
                }
            }
        }
        this.telemetry.addData("Shoulder Position", "shoulder (%.2f)", Double.valueOf(shoulder.getCurrentPosition() ));
        this.telemetry.addData("Arm Position", "arm (%.2f)", Double.valueOf(arm.getCurrentPosition()));
        this.telemetry.addData("Wheel Position", "FLeft (%.2f), FRight (%.2f)", Double.valueOf(leftDriveFront.getCurrentPosition()), Double.valueOf(rightDriveFront.getCurrentPosition()));

    }
}
