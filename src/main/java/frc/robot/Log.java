package frc.robot;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.HALUtil;
import edu.wpi.first.hal.PowerJNI;
import edu.wpi.first.hal.can.CANJNI;
import edu.wpi.first.hal.can.CANStatus;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Log {
	public static boolean initialized = false;

	private static Notifier systemInformationThread;

	public static SEVERITY severity;

	/*
	 * Actually starts logging into a file, otherwise only NT values will be updated
	 */
	public static final void initialize(SEVERITY severity) {
		if (initialized == true)
			return;

		Log.severity = severity;

		if (severity == SEVERITY.NOTHING)
			return;

		DataLogManager.start();
		DriverStation.startDataLog(DataLogManager.getLog());

		if (Log.severity.level >= SEVERITY.DEBUG.level) {
			systemInformationThread = new Notifier(Log::logSystemInformation);
			systemInformationThread.setName("System Information");
			systemInformationThread.startPeriodic(0.02);
		}

		initialized = true;
	}

	public static void data(String name, Sendable sendable, SEVERITY severity) {
		if (severity.level > Log.severity.level)
			return;
		SmartDashboard.putData(name, sendable);
	}

	public static <T> void value(String name, T value, SEVERITY severity) {
		if (severity.level > Log.severity.level)
			return;
		if (value.getClass() == int.class || value.getClass() == double.class || value.getClass() == float.class
				|| value.getClass() == long.class) {
			SmartDashboard.putString(name, value.toString());
		} else {
			SmartDashboard.putString(name, value.toString());
		}
	}

	public static <T> void value(String name, T value) {
		if (severity.level > Log.severity.level)
			return;
		value(name, value, SEVERITY.DEBUG);
	}

	public static final CANStatus status = new CANStatus();
	public static final PowerDistribution pdh = new PowerDistribution(1, ModuleType.kRev);
	public static final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

	public static final void logSystemInformation() {
		value("SystemStats/BrownedOut", HAL.getBrownedOut());
		value("SystemStats/BatteryVoltage", PowerJNI.getVinVoltage());
		value("SystemStats/BatteryCurrent", PowerJNI.getVinCurrent());
		value("SystemStats/3v3Rail/Voltage", PowerJNI.getUserVoltage3V3());
		value("SystemStats/3v3Rail/Current", PowerJNI.getUserCurrent3V3());
		value("SystemStats/3v3Rail/Active", PowerJNI.getUserActive3V3());
		value("SystemStats/3v3Rail/CurrentFaults", PowerJNI.getUserCurrentFaults3V3());
		value("SystemStats/5vRail/Voltage", PowerJNI.getUserVoltage5V());
		value("SystemStats/5vRail/Current", PowerJNI.getUserCurrent5V());
		value("SystemStats/5vRail/Active", PowerJNI.getUserActive5V());
		value("SystemStats/5vRail/CurrentFaults", PowerJNI.getUserCurrentFaults5V());
		value("SystemStats/6vRail/Voltage", PowerJNI.getUserVoltage6V());
		value("SystemStats/6vRail/Current", PowerJNI.getUserCurrent6V());
		value("SystemStats/6vRail/Active", PowerJNI.getUserActive6V());
		value("SystemStats/6vRail/CurrentFaults", PowerJNI.getUserCurrentFaults6V());
		CANJNI.getCANStatus(status);
		value("SystemStats/CANBus/Utilization", status.percentBusUtilization);
		value("SystemStats/CANBus/OffCount", status.busOffCount);
		value("SystemStats/CANBus/TxFullCount", status.txFullCount);
		value("SystemStats/CANBus/ReceiveErrorCount", status.receiveErrorCount);
		value("SystemStats/CANBus/TransmitErrorCount", status.transmitErrorCount);
		value("SystemStats/EpochTimeMicros", HALUtil.getFPGATime());
		value("SystemStats/PowerDistribution/Temperature", pdh.getTemperature());
		value("SystemStats/PowerDistribution/Voltage", pdh.getVoltage());
		SmartDashboard.putNumberArray("SystemStats/PowerDistribution/ChannelCurrent", pdh.getAllCurrents());
		value("SystemStats/PowerDistribution/TotalCurrent", pdh.getTotalCurrent());
		value("SystemStats/PowerDistribution/TotalPower", pdh.getTotalPower());
		value("SystemStats/PowerDistribution/TotalEnergy", pdh.getTotalEnergy());
		value("SystemStats/PowerDistribution/ChannelCount", pdh.getNumChannels());
		value("SystemStats/RAM/FreeMemoryMB", Runtime.getRuntime().freeMemory() / Math.pow(2, 20));
		value("SystemStats/RAM/UsedMemoryMB",
				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Math.pow(2, 20));
		value("SystemStats/CPU/Load", (osBean.getSystemLoadAverage() / osBean.getAvailableProcessors()) * 100);
		value("SystemStats/CPU/TemperatureC", PowerJNI.getCPUTemp());
	}

	public static enum SEVERITY {
		NOTHING(0),
		MINIMAL(2),
		DEBUG(3),
		EVERYTHING(10);

		public final int level;

		SEVERITY(int l) {
			level = l;
		}
	}

}
