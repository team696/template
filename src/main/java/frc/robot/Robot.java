// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.Auto.NamedCommand;
import frc.robot.Log.SEVERITY;
import frc.robot.subsystems.Swerve;

public class Robot extends TimedRobot {
	private Command m_autonomousCommand;

	public Robot() {
		Log.initialize(SEVERITY.EVERYTHING);

		Log.data("Swerve", Swerve.get(), SEVERITY.MINIMAL);

		Auto.initialize(
				new NamedCommand("PRINT", new InstantCommand(() -> System.out.println(1))));

		Binds.DriverStation2025.bind();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		m_autonomousCommand = Auto.getSelectedAuto();

		if (m_autonomousCommand != null) {
			m_autonomousCommand.schedule();
		}
	}

	@Override
	public void teleopInit() {
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	@Override
	public void testInit() {
		CommandScheduler.getInstance().cancelAll();
	}
}
