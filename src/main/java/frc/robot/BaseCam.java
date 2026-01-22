package frc.robot;

import java.util.Optional;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;

/**
 * Base Camera Object for handling how cameras will generally be handled
 * 
 * <p>
 * Extend to add functionality for a specific camera
 * 
 * @see LimeLightCam.java
 * @see PhotonVisionCam.java
 */
public abstract class BaseCam {
	public class AprilTagResult {
		public Pose2d pose;
		/*
		 * Time Should Be In Current Time, Same as Phoenix6 Swerve
		 */
		public double time;

		public double distToTag;
		public int tagCount;

		public double ambiguity;

		public AprilTagResult(Pose2d pose, double time, double distToTag, int tagCount, double ambiguity) {
			this.pose = pose;
			this.time = time;
			this.distToTag = distToTag;
			this.tagCount = tagCount;
			this.ambiguity = ambiguity;
		}
	}

	public class measurementTrust {
		public double translation = .7;
		public double rotation = 2.;

		public Vector<N3> asStdDeviations() {
			return VecBuilder.fill(translation, translation, rotation);
		}
	}

	measurementTrust trust = new measurementTrust();

	public abstract Optional<AprilTagResult> getEstimate();

	@FunctionalInterface
	public static interface addVisionEstimate {
		void accept(Pose2d p, double d, Vector<N3> v);
	}

	@FunctionalInterface
	public static interface acceptEstimate {
		boolean test(AprilTagResult latestResult, final measurementTrust stdDeviations);
	}

	// eventually switch this to taking in a addVisionEstimate
	public boolean addVisionEstimate(addVisionEstimate addVisionMeasurement, acceptEstimate checkEstimation) {
		Optional<AprilTagResult> oEstimation = this.getEstimate();

		if (oEstimation.isPresent()) {
			AprilTagResult estimation = oEstimation.get();
			try {
				if (!checkEstimation.test(estimation, trust)) {
					Swerve.get().fieldSim.getObject("Rejected Robot Pose").setPose(estimation.pose);
					return false;
				} else {
					Swerve.get().fieldSim.getObject("Accepted Robot Pose").setPose(estimation.pose);
					addVisionMeasurement.accept(
							estimation.pose,
							estimation.time,
							trust.asStdDeviations());
					return true;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}

	public synchronized boolean addVisionEstimate(addVisionEstimate addVisionMeasurement) {
		return addVisionEstimate(addVisionMeasurement, (latestResult, stdDeviations) -> {
			if (latestResult.distToTag > 4) {
				return false;
			}

			return true;
		});
	}
}
