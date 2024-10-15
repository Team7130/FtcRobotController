package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.opencv.videoio.Videoio;

@TeleOp(group = "Iterative OpMode", name = "Basic: Iterative OpMode")
public class MyFIRSTJavaOpMode extends OpMode {
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
        double currentPosition = (double) this.shoulder.getCurrentPosition();
        this.shldsetpoint = currentPosition;
        this.shldIntegral = currentPosition;
        this.telemetry.addData("Status", (Object) "Initialized: " + this.runtime.toString());
    }

    public void init_loop() {
        this.telemetry.addData("Init Loop", (Object) "Init Time: " + this.runtime.toString());
    }

    public void start() {
        this.runtime.reset();
    }

    public void loop() {
        double strafe = (double) this.gamepad1.left_stick_x;
        double drive = (double) this.gamepad1.left_stick_y;
        double turn = (double) this.gamepad1.right_stick_x;
        double rotateShld = (double) this.gamepad2.right_stick_y;
        double openHand = (double) this.gamepad2.left_trigger;
        double closeHand = (double) this.gamepad2.right_trigger;
        boolean wristUp = this.gamepad2.dpad_up;
        boolean wristDown = this.gamepad2.dpad_down;
        double armVal = (double) this.gamepad2.left_stick_y;
        double rotateShld2 = rotateShld;
        if (openHand > 0.1d) {
            this.hand.setPower(1.0d);
        }
        if (closeHand > 0.1d) {
            this.handpos = 0.0d;
            this.hand.setPower(-1.0d);
        }
        else {
            this.hand.setPower(0.0);
        }

        if(wristUp) {
            this.wristpos = 0.6d;
            this.wrist.setPosition(wristpos);
        } else if(wristDown) {
            this.wristpos = 0.0d;
            this.wrist.setPosition(wristpos);
        }

        if (strafe < 0.1d && strafe > -0.1d) {
            strafe = 0.0d;
        }
        if (drive < 0.1d && drive > -0.1d) {
            drive = 0.0d;
        }
        if (turn < 0.1d && turn > -0.1d) {
            turn = 0.0d;
        }

        if(Math.abs(armVal) > 0.1) {
            this.arm.setPower(armVal);
        } else {
            this.arm.setPower(0.0d);
        }
        
        double leftDrivePower = Range.clip(drive - turn, -5.0d, 5.0d);
        double rightDrivePower = Range.clip(drive + turn, -5.0d, 5.0d);
        double strafePower = Range.clip(strafe, -5.0d, 5.0d);
        double shoulderPower = Range.clip(rotateShld2, -1.0d, 1.0d);
        this.leftDriveFront.setPower(leftDrivePower - strafePower);
        this.leftDriveBack.setPower(leftDrivePower + strafePower);
        this.rightDriveFront.setPower(rightDrivePower - strafePower);
        this.rightDriveBack.setPower(rightDrivePower + strafePower);
        this.shoulder.setPower(shoulderPower);
        double d4 = strafe;
        this.telemetry.addData("Status", (Object) "Run Time: " + this.runtime.toString());
        double d5 = drive;
        this.telemetry.addData("Drive Motors", "left (%.2f), right (%.2f)", Double.valueOf(leftDrivePower), Double.valueOf(rightDrivePower));
        this.telemetry.addData("Arm Motor", "shoulder (%.2f)", Double.valueOf((double) this.shoulder.getCurrentPosition()));
    }

    public void stop() {
    }
}
