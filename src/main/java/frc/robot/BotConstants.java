package frc.robot;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.Log.SEVERITY;

public class BotConstants {
	public static final SEVERITY LOGGING_SEVERITY = SEVERITY.EVERYTHING; 

	public static final CANBus RIOBUS = new CANBus("rio");
	public static final CANBus CANIVORE_BUS = new CANBus("cv");

	// Example Subsystem
	//
	public class ExampleSubSystem {
		public static final int motorID = 0;
		public static final TalonFXConfiguration motor_config = new TalonFXConfiguration();
		static {
			motor_config.Slot0.kP = 0;

			motor_config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
		}
	}

	public static final class SWERVE {

	}
}
