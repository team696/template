package frc.robot.subsystems;

import java.util.function.Supplier;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.LimeLightCam;
import frc.robot.TunerConstants;
import frc.robot.BaseCam.AprilTagResult;
import frc.robot.BaseCam.measurementTrust;
import frc.robot.TunerConstants.TunerSwerveDrivetrain;

public final class Swerve extends TunerSwerveDrivetrain implements Subsystem, Sendable {
	private static Swerve m_Swerve;

	LimeLightCam exampleCamera = new LimeLightCam("limelight-front");
	Field2d fieldSim = new Field2d();	

	public static synchronized Swerve get() {
		if (m_Swerve == null)
			m_Swerve = TunerConstants.createDrivetrain();
		return m_Swerve;
	}

	public Swerve(
			SwerveDrivetrainConstants drivetrainConstants, SwerveModuleConstants<?, ?, ?>... modules) {
		super(drivetrainConstants, 0, modules);
		if (Utils.isSimulation()) {
			simulationInit();
		}
	}

	@Override
	public void periodic() {
		exampleCamera.addVisionEstimate(this::addVisionMeasurement, this::acceptEstimate);
		fieldSim.setRobotPose(this.getState().Pose);	
		SmartDashboard.putData("field", fieldSim);
	}

	// Untested Code, Not Meant to Actually be used, just an example.
	boolean acceptEstimate(AprilTagResult latestResult, measurementTrust stdDeviations) {
		if (latestResult.distToTag > 4)
			return false; // Disregard measurements if we are more than 4 meters away
		if (this.getState().Speeds.omegaRadiansPerSecond > 2 * Math.PI)
			return false; // Disregard measurement if we are spinning faster than 360 deg / second

		// Trust The measurement less the further we are
		double stdDeviationCoefficient = latestResult.distToTag * latestResult.distToTag / 12 ;

		stdDeviations.translation = .5 * stdDeviationCoefficient;
		stdDeviations.rotation = .5 * stdDeviationCoefficient;

		return true;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.addDoubleProperty("Value", () -> 1, null);
	}

	public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
		return run(() -> this.setControl(requestSupplier.get()));
	}

	@Override
	public void addVisionMeasurement(Pose2d visionRobotPoseMeters, double timestampSeconds) {
		super.addVisionMeasurement(visionRobotPoseMeters, Utils.fpgaToCurrentTime(timestampSeconds));
	}

	@Override
	public void addVisionMeasurement(
			Pose2d visionRobotPoseMeters,
			double timestampSeconds,
			Matrix<N3, N1> visionMeasurementStdDevs) {
		super.addVisionMeasurement(visionRobotPoseMeters, Utils.fpgaToCurrentTime(timestampSeconds),
				visionMeasurementStdDevs);
	}

	double m_lastSimTime;
	Notifier simUpdate;

	private void simulationInit() {
		m_lastSimTime = Utils.getCurrentTimeSeconds();
		simUpdate = new Notifier(() -> {
			final double currentTime = Utils.getCurrentTimeSeconds();
			double deltaTime = currentTime - m_lastSimTime;
			m_lastSimTime = currentTime;

			/* use the measured time delta, get battery voltage from WPILib */
			updateSimState(deltaTime, RobotController.getBatteryVoltage());
		});
		simUpdate.setName("Swerve Simulation Update");
		simUpdate.startPeriodic(0.005);
	}
}
