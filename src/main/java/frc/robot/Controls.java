// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/*
 *  Define Controls as seen on control, DO NOT NAME IT WHAT IT ACTUALLY DOES
 *
 *  All Joysticks Should have Positive be forward
 */
public final class Controls {
	public static final class DriverPanel {
		public static final Joystick DriverPanel = new Joystick(0);

		public static final JoystickButton leftJoyButton = new JoystickButton(DriverPanel, 1);
		public static final JoystickButton rightJoyButton = new JoystickButton(DriverPanel, 2);

		public static final DoubleSupplier leftJoyY = () -> -DriverPanel.getRawAxis(1);
		public static final DoubleSupplier leftJoyX = () -> DriverPanel.getRawAxis(0);
		public static final DoubleSupplier rightJoyX = () -> -DriverPanel.getRawAxis(2);
	}

	public static final class OperatorPanel2025 {
		public static final Joystick OperatorPanel = new Joystick(1);

		public static final JoystickButton L1 = new JoystickButton(OperatorPanel, 9);
		public static final JoystickButton L2 = new JoystickButton(OperatorPanel, 10);
		public static final JoystickButton L3 = new JoystickButton(OperatorPanel, 11);
		public static final JoystickButton L4 = new JoystickButton(OperatorPanel, 12);
		public static final JoystickButton releaseCoral = new JoystickButton(OperatorPanel, 7);
		public static final JoystickButton pickupAlgae = new JoystickButton(OperatorPanel, 8);
		public static final JoystickButton gyro = new JoystickButton(OperatorPanel, 13);
		public static final JoystickButton Climb1 = new JoystickButton(OperatorPanel, 4);
		public static final JoystickButton Climb2 = new JoystickButton(OperatorPanel, 3);
		public static final JoystickButton SouceCoral = new JoystickButton(OperatorPanel, 5);
		public static final JoystickButton GroundCoral = new JoystickButton(OperatorPanel, 6);
		public static final JoystickButton Barge = new JoystickButton(OperatorPanel, 2);
		public static final JoystickButton Processor = new JoystickButton(OperatorPanel, 1);
		public static final JoystickButton leftOrRight = new JoystickButton(OperatorPanel, 14);
		public static final JoystickButton unlabedSwitch = new JoystickButton(OperatorPanel, 15);
		public static final JoystickButton deepOrSwitch = new JoystickButton(OperatorPanel, 16);
	}

	public static final class SingleXboxController {
		public static final CommandXboxController controller = new CommandXboxController(5);

		public static final DoubleSupplier leftJoyY = () -> -controller.getRawAxis(1);
		public static final DoubleSupplier leftJoyX = () -> -controller.getRawAxis(0);
		public static final DoubleSupplier rightJoyX = () -> -controller.getRawAxis(4);

		public static final Trigger A = controller.a();
		public static final Trigger B = controller.b();
		public static final Trigger X = controller.x();
		public static final Trigger Y = controller.y();

		public static final Trigger RB = controller.rightBumper();
		public static final Trigger LB = controller.leftBumper();

		public static final Trigger LT = controller.leftTrigger(0.5);
		public static final Trigger RT = controller.rightTrigger(0.5);
	}
}
