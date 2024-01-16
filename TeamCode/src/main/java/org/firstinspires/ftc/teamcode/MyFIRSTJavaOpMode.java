package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.opencv.videoio.Videoio;

@TeleOp(group = "Iterative OpMode", name = "Basic: Iterative OpMode")
public class MyFIRSTJavaOpMode extends OpMode {
    private Servo airplaneServo = null;
    double airplaneServoPos = 0.0d;
    int airplaneTimerDelay = Videoio.CAP_QT;
    long airplaneTimerMillis = System.currentTimeMillis();
    private Servo hand = null;
    double handpos = 0.0d;
    private DcMotor leftDriveBack = null;
    private DcMotor leftDriveFront = null;
    private DcMotor rightDrive = null;
    private DcMotor rightDriveBack = null;
    private DcMotor rightDriveFront = null;
    private ElapsedTime runtime = new ElapsedTime();
    private double shldIntegral = 0.0d;
    private double shldsetpoint = -50.0d;
    private DcMotor shoulder = null;

    public void init() {
        this.telemetry.addData("Status", (Object) "Initialized");
        this.leftDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "frontleftmotor");
        this.leftDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "backleftmotor");
        this.rightDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "frontrightmotor");
        this.rightDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "backrightmotor");
        Servo servo = (Servo) this.hardwareMap.get(Servo.class, "plane");
        this.airplaneServo = servo;
        servo.setDirection(Servo.Direction.FORWARD);
        this.shoulder = (DcMotor) this.hardwareMap.get(DcMotor.class, "shoulder");
        Servo servo2 = (Servo) this.hardwareMap.get(Servo.class, "hand");
        this.hand = servo2;
        servo2.setDirection(Servo.Direction.FORWARD);
        this.leftDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.leftDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.shoulder.setDirection(DcMotorSimple.Direction.REVERSE);
        this.handpos = 0.0d;
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
        double airplaneTrg = (double) this.gamepad1.left_trigger;
        double rotateShld2 = rotateShld;
        if (openHand > 0.0d) {
            this.handpos = 1.0d;
        }
        if (closeHand > 0.0d) {
            this.handpos = 0.0d;
        }
        this.hand.setPosition(this.handpos);
        if (airplaneTrg > 0.0d) {
            this.airplaneServoPos += 0.25d;
            double d = airplaneTrg;
            this.airplaneTimerMillis = System.currentTimeMillis() + ((long) this.airplaneTimerDelay);
        }
        if (System.currentTimeMillis() > this.airplaneTimerMillis) {
            this.airplaneServoPos = 0.0d;
        }
        this.airplaneServo.setPosition(this.airplaneServoPos);
        if (strafe < 0.1d && strafe > -0.1d) {
            strafe = 0.0d;
        }
        if (drive < 0.1d && drive > -0.1d) {
            drive = 0.0d;
        }
        if (turn < 0.1d && turn > -0.1d) {
            turn = 0.0d;
        }
        double leftDrivePower = Range.clip(drive - turn, -5.0d, 5.0d);
        double rightDrivePower = Range.clip(drive + turn, -5.0d, 5.0d);
        double strafePower = Range.clip(strafe, -5.0d, 5.0d);
        double d2 = closeHand;
        double d3 = openHand;
        double shoulderPower = Range.clip(rotateShld2, -1.0d, 1.0d);
        this.leftDriveFront.setPower(leftDrivePower - strafePower);
        this.leftDriveBack.setPower(leftDrivePower + strafePower);
        this.rightDriveFront.setPower(rightDrivePower - strafePower);
        this.rightDriveBack.setPower(rightDrivePower + strafePower);
        double handposTemp = this.hand.getPosition();
        this.shoulder.setPower(shoulderPower);
        double d4 = strafe;
        this.telemetry.addData("Status", (Object) "Run Time: " + this.runtime.toString());
        double d5 = drive;
        this.telemetry.addData("Drive Motors", "left (%.2f), right (%.2f)", Double.valueOf(leftDrivePower), Double.valueOf(rightDrivePower));
        this.telemetry.addData("Arm Motor", "shoulder (%.2f)", Double.valueOf((double) this.shoulder.getCurrentPosition()));
        this.telemetry.addData("handpos", "hand (%.2f)", Double.valueOf(handposTemp));
    }

    public void stop() {
    }
}
