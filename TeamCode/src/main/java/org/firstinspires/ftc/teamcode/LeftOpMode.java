package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(group = "Iterative OpMode", name = "Autonomous: Blue")
public class LeftOpMode extends OpMode {
    private DcMotor leftDriveBack = null;
    private DcMotor leftDriveFront = null;
    private DcMotor rightDrive = null;
    private DcMotor rightDriveBack = null;
    private DcMotor rightDriveFront = null;
    private ElapsedTime runtime = new ElapsedTime();
    double startSeconds = 0.0d;

    public void init() {
        this.leftDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "frontleftmotor");
        this.leftDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "backleftmotor");
        this.rightDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "frontrightmotor");
        this.rightDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "backrightmotor");
    }

    public void loop() {
        if (this.startSeconds == 0.0d) {
            this.startSeconds = this.runtime.seconds();
        }
        final double time = 10.0d;
        final double power = 0.3d;
        if (this.runtime.seconds() < this.startSeconds + time) {
            this.leftDriveFront.setPower(-power);
            this.leftDriveBack.setPower(power);
            this.rightDriveFront.setPower(-power);
            this.rightDriveBack.setPower(power);
            return;
        }
        this.leftDriveFront.setPower(0.0d);
        this.leftDriveBack.setPower(0.0d);
        this.rightDriveFront.setPower(0.0d);
        this.rightDriveBack.setPower(0.0d);
    }
}
