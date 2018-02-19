/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.test.devices.mindstorms.ev3;

import android.content.Context;
import android.test.AndroidTestCase;

import org.catrobat.catroid.common.bluetooth.ConnectionDataLogger;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3CommandByte;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3Impl;
import org.catrobat.catroid.devices.mindstorms.ev3.sensors.EV3Sensor;
import org.catrobat.catroid.devices.mindstorms.ev3.sensors.EV3SensorMode;
import org.catrobat.catroid.devices.mindstorms.ev3.sensors.EV3SensorType;
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment;

public class LegoEV3SensorTest extends AndroidTestCase {

	private Context applicationContext;

	private LegoEV3 ev3;
	ConnectionDataLogger logger;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		applicationContext = this.getContext().getApplicationContext();
		ev3 = new LegoEV3Impl(applicationContext);

		logger = ConnectionDataLogger.createLocalConnectionLogger();
		ev3.setConnection(logger.getConnectionProxy());
	}

	public void testTouchSensor() {

		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.EV3_TOUCH;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE0;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.TOUCH, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkPercentValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testNxtLightActiveSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.NXT_LIGHT;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE0; // NXT_LIGHT_ACTIVE

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.NXT_LIGHT_ACTIVE, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkPercentValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testNxtLightSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.NXT_LIGHT;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE1; // NXT_LIGHT

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.NXT_LIGHT, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkPercentValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testColorSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.EV3_COLOR;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE2;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.COLOR, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkRawValueCommand(expectedPort);
	}

	public void testEV3ColorReflectSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.EV3_COLOR;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE1;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.COLOR_REFLECT, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkPercentValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testEV3ColorAmbientSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.EV3_COLOR;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE0;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.COLOR_AMBIENT, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkPercentValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testHiTechnicColorSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.IIC;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE1;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.HT_NXT_COLOR, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkRawValueCommand(expectedPort);
	}

	public void testNxtTemperatureFSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.NXT_TEMPERATURE;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE1;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.NXT_TEMPERATURE_F, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkSiValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testTemperatureCSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.NXT_TEMPERATURE;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE0;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.NXT_TEMPERATURE_C, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkSiValueCommand(expectedType, expectedMode, expectedPort);
	}

	public void testInfraredSensor() {
		final int expectedPort = 0;
		final EV3SensorType expectedType = EV3SensorType.EV3_INFRARED;
		final EV3SensorMode expectedMode = EV3SensorMode.MODE0;

		SettingsFragment.setLegoMindstormsEV3SensorMapping(applicationContext, new EV3Sensor.Sensor[] {
				EV3Sensor.Sensor.INFRARED, EV3Sensor.Sensor.NO_SENSOR,
				EV3Sensor.Sensor.NO_SENSOR, EV3Sensor.Sensor.NO_SENSOR});

		initSensorAndGetValue();

		checkInitializationCommand(expectedType, expectedMode, expectedPort);
		checkPercentValueCommand(expectedType, expectedMode, expectedPort);
	}

	private void checkInitializationCommand(EV3SensorType expectedType, EV3SensorMode expectedMode, int expectedPort) {

		final int expectedCommandCounter = 1;

		byte[] commandBytes = logger.getNextSentMessage(0, 2);

		assertEquals((byte) expectedCommandCounter,
				commandBytes[0]);
		assertEquals(EV3CommandByte.EV3CommandOpCode.OP_INPUT_DEVICE.getByte(), commandBytes[5]);
		assertEquals(EV3CommandByte.EV3CommandByteCode.INPUT_DEVICE_READY_RAW.getByte(), commandBytes[6]);
		assertEquals((byte) expectedPort, commandBytes[8]);
		assertEquals(expectedType.getByte(), commandBytes[10]);
		assertEquals(expectedMode.getByte(), commandBytes[11]);
	}

	private void checkPercentValueCommand(EV3SensorType expectedType, EV3SensorMode expectedMode, int expectedPort) {
		final int expectedCommandCounter = 2;

		byte[] commandBytes = logger.getNextSentMessage(0, 2);

		assertEquals((byte) expectedCommandCounter, commandBytes[0]);
		assertEquals(EV3CommandByte.EV3CommandOpCode.OP_INPUT_READ.getByte(), commandBytes[5]);
		assertEquals((byte) expectedPort, commandBytes[7]);
		assertEquals(expectedType.getByte(), commandBytes[9]);
		assertEquals(expectedMode.getByte(), commandBytes[10]);
	}

	private void checkSiValueCommand(EV3SensorType expectedType, EV3SensorMode expectedMode, int expectedPort) {
		final int expectedCommandCounter = 2;

		byte[] commandBytes = logger.getNextSentMessage(0, 2);

		assertEquals((byte) expectedCommandCounter, commandBytes[0]);
		assertEquals(EV3CommandByte.EV3CommandOpCode.OP_INPUT_READ_SI.getByte(), commandBytes[5]);
		assertEquals((byte) expectedPort, commandBytes[7]);
		assertEquals(expectedType.getByte(), commandBytes[9]);
		assertEquals(expectedMode.getByte(), commandBytes[10]);
	}

	private void checkRawValueCommand(int expectedPort) {
		final int expectedCommandCounter = 2;

		byte[] commandBytes = logger.getNextSentMessage(0, 2);

		assertEquals((byte) expectedCommandCounter, commandBytes[0]);
		assertEquals(EV3CommandByte.EV3CommandOpCode.OP_INPUT_DEVICE.getByte(), commandBytes[5]);
		assertEquals(EV3CommandByte.EV3CommandByteCode.INPUT_DEVICE_GET_RAW.getByte(), commandBytes[6]);
		assertEquals((byte) expectedPort, commandBytes[8]);
	}

	private void initSensorAndGetValue() {
		ev3.initialise();

		ev3.getSensor1().updateLastSensorValue(); // First time the Sensor gets initialized
		ev3.getSensor1().updateLastSensorValue();
	}
}
