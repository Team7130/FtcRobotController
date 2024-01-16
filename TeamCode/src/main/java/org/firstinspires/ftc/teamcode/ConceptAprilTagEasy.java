package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

@TeleOp(group = "Red auto", name = "Chef's Surprise")
public class ConceptAprilTagEasy extends LinearOpMode {
    private static final boolean USE_WEBCAM = true;
    private AprilTagProcessor aprilTag;
    private DcMotor leftDriveBack = null;
    private DcMotor leftDriveFront = null;
    private DcMotor rightDriveBack = null;
    private DcMotor rightDriveFront = null;
    private TfodProcessor tfod;
    private VisionPortal visionPortal;

    public void runOpMode() {
        initAprilTag();
        this.leftDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "frontleftmotor");
        this.leftDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "backleftmotor");
        this.rightDriveFront = (DcMotor) this.hardwareMap.get(DcMotor.class, "frontrightmotor");
        this.rightDriveBack = (DcMotor) this.hardwareMap.get(DcMotor.class, "backrightmotor");
        this.leftDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.leftDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.telemetry.addData("DS preview on/off", (Object) "3 dots, Camera Stream");
        this.telemetry.addData(">", (Object) "Touch Play to start OpMode");
        this.telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetryAprilTag();
                this.telemetry.update();
                if (this.gamepad1.dpad_down) {
                    this.visionPortal.stopStreaming();
                } else if (this.gamepad1.dpad_up) {
                    this.visionPortal.resumeStreaming();
                }
                double leftDrivePower = Range.clip(LynxServoController.apiPositionFirst - LynxServoController.apiPositionFirst, -5.0d, 5.0d);
                double rightDrivePower = Range.clip(LynxServoController.apiPositionFirst + LynxServoController.apiPositionFirst, -5.0d, 5.0d);
                double strafePower = Range.clip((double) LynxServoController.apiPositionFirst, -5.0d, 5.0d);
                this.leftDriveFront.setPower(leftDrivePower - strafePower);
                this.leftDriveBack.setPower(leftDrivePower + strafePower);
                this.rightDriveFront.setPower(rightDrivePower - strafePower);
                this.rightDriveBack.setPower(rightDrivePower + strafePower);
                sleep(20);
            }
        }
        this.visionPortal.close();
    }

    private void initAprilTag() {
        this.aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        this.visionPortal = VisionPortal.easyCreateWithDefaults((CameraName) this.hardwareMap.get(WebcamName.class, "Webcam1"), this.aprilTag);
    }

    private void initTfod() {
        this.tfod = TfodProcessor.easyCreateWithDefaults();
        this.visionPortal = VisionPortal.easyCreateWithDefaults((CameraName) this.hardwareMap.get(WebcamName.class, "Webcam 1"), this.tfod);
    }

    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = this.aprilTag.getDetections();
        this.telemetry.addData("# AprilTags Detected", (Object) Integer.valueOf(currentDetections.size()));
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                this.telemetry.addLine(String.format("\n==== (ID %d) %s", new Object[]{Integer.valueOf(detection.id), detection.metadata.name}));
                this.telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", new Object[]{Double.valueOf(detection.ftcPose.x), Double.valueOf(detection.ftcPose.y), Double.valueOf(detection.ftcPose.z)}));
                this.telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", new Object[]{Double.valueOf(detection.ftcPose.pitch), Double.valueOf(detection.ftcPose.roll), Double.valueOf(detection.ftcPose.yaw)}));
                this.telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", new Object[]{Double.valueOf(detection.ftcPose.range), Double.valueOf(detection.ftcPose.bearing), Double.valueOf(detection.ftcPose.elevation)}));
            } else {
                this.telemetry.addLine(String.format("\n==== (ID %d) Unknown", new Object[]{Integer.valueOf(detection.id)}));
                this.telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", new Object[]{Double.valueOf(detection.center.x), Double.valueOf(detection.center.y)}));
            }
        }
        this.telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        this.telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        this.telemetry.addLine("RBE = Range, Bearing & Elevation");
    }

    private void telemetryTfod() {
        List<Recognition> currentRecognitions = this.tfod.getRecognitions();
        this.telemetry.addData("# Objects Detected", (Object) Integer.valueOf(currentRecognitions.size()));
        for (Recognition recognition : currentRecognitions) {
            double x = (double) ((recognition.getLeft() + recognition.getRight()) / 2.0f);
            this.telemetry.addData("", (Object) " ");
            this.telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), Float.valueOf(recognition.getConfidence() * 100.0f));
            this.telemetry.addData("- Position", "%.0f / %.0f", Double.valueOf(x), Double.valueOf((double) ((recognition.getTop() + recognition.getBottom()) / 2.0f)));
            this.telemetry.addData("- Size", "%.0f x %.0f", Float.valueOf(recognition.getWidth()), Float.valueOf(recognition.getHeight()));
        }
    }
}
