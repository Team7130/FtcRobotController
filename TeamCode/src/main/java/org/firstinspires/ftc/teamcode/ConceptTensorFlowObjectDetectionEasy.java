package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

@TeleOp(group = "Concept", name = "Concept: TensorFlow Object Detection Easy")
public class ConceptTensorFlowObjectDetectionEasy extends LinearOpMode {
    private static final boolean USE_WEBCAM = true;
    private TfodProcessor tfod;
    private VisionPortal visionPortal;

    public void runOpMode() {
        initTfod();
        this.telemetry.addData("DS preview on/off", (Object) "3 dots, Camera Stream");
        this.telemetry.addData(">", (Object) "Touch Play to start OpMode");
        this.telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetryTfod();
                this.telemetry.update();
                if (this.gamepad1.dpad_down) {
                    this.visionPortal.stopStreaming();
                } else if (this.gamepad1.dpad_up) {
                    this.visionPortal.resumeStreaming();
                }
                sleep(20);
            }
        }
        this.visionPortal.close();
    }

    private void initTfod() {
        this.tfod = TfodProcessor.easyCreateWithDefaults();
        this.visionPortal = VisionPortal.easyCreateWithDefaults((CameraName) this.hardwareMap.get(WebcamName.class, "Webcam1"), this.tfod);
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
