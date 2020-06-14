/*
 * mathan-sparkfun-4-digit-7-segment-i2c
 * Copyright (c) 2020 Matthias Hanisch
 * matthias@mathan.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mathan.sparkfun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import net.sf.yad2xx.FTDIException;
import net.sf.yad2xx.mpsse.I2C;

public class Display {
  
  public interface Constants {

    /**
     * Default address of the Sparkfun Serial 7 Segment Display. The address can be changed using
     * {@link Display#setAddress(byte)}.
     */
    byte ADDRESS_DEFAULT = 0x71;

    /**
     * Display data for a digit, all segments off.
     */
    byte EMPTY = (byte) 0x00;
    /**
     * Display data for a digit, showing digit 0.
     */
    byte DIGIT_0 = (byte) 0x3F;
    /**
     * Display data for a digit, showing digit 1.
     */
    byte DIGIT_1 = (byte) 0x06;
    /**
     * Display data for a digit, showing digit 2.
     */
    byte DIGIT_2 = (byte) 0x5b;
    /**
     * Display data for a digit, showing digit 3.
     */
    byte DIGIT_3 = (byte) 0x4f;
    /**
     * Display data for a digit, showing digit 4.
     */
    byte DIGIT_4 = (byte) 0x66;
    /**
     * Display data for a digit, showing digit 5.
     */
    byte DIGIT_5 = (byte) 0x6d;
    /**
     * Display data for a digit, showing digit 6.
     */
    byte DIGIT_6 = (byte) 0x7d;
    /**
     * Display data for a digit, showing digit 7.
     */
    byte DIGIT_7 = (byte) 0x07;
    /**
     * Display data for a digit, showing digit 8.
     */
    byte DIGIT_8 = (byte) 0x7f;
    /**
     * Display data for a digit, showing digit 9.
     */
    byte DIGIT_9 = (byte) 0x6f;

    /**
     * Decimal data for displaying data with 3 decimal places.
     */
    byte DECIMALS_3 = (byte) 0x01;
    /**
     * Decimal data for displaying data with 2 decimal places.
     */
    byte DECIMALS_2 = (byte) 0x02;
    /**
     * Decimal data for displaying data with 1 decimal place.
     */
    byte DECIMALS_1 = (byte) 0x04;
    /**
     * Decimal data for displaying colon (used for time representation hh:mm).
     */
    byte DECIMALS_TIME = (byte) 0x10;
    /**
     * Decimal data for displaying apostrophe.
     */
    byte DECIMALS_APOSTROPHE = (byte)0x02;

    byte CMD_BRIGHTNESS = 0x7A;
    byte CMD_DECIMALS = 0x77;
    byte CMD_1ST = 0x7b;
    byte CMD_2ND = 0x7c;
    byte CMD_3RD = 0x7d;
    byte CMD_4TH = 0x7e;
    byte CMD_INIT = 0x76;
    byte CMD_ADDRESS = (byte) 0x80;

    byte BRIGHTNESS_MIN = 0x01;
    byte BRIGHTNESS_MAX = (byte)0xff;

    byte[] DIGITS = {DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4, DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9};

    /**
     * Default command list when updating data to the device. Will update all digits, decimals and brightness.
     */
    byte[] COMMANDS = {CMD_1ST, CMD_2ND, CMD_3RD, CMD_4TH, CMD_DECIMALS, CMD_BRIGHTNESS};
    /**
     * Default data after initialization, everything empty and brightness set to maximum.
     */
    byte[] INITIAL_DATA = { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BRIGHTNESS_MAX};

  }
  private final byte[] buffer = Arrays.copyOf(Constants.INITIAL_DATA, Constants.INITIAL_DATA.length);

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
  private final I2C device;
  private byte address;

  public Display(I2C device) throws FTDIException {
    this(device, Constants.ADDRESS_DEFAULT);
  }

  public Display(I2C device, byte address) throws FTDIException {
    this.device = device;
    this.address = address;
    device.transactWrite(this.address, Constants.CMD_INIT);
    for(int i=0;i<Constants.COMMANDS.length;i++) {
      device.transactWrite(this.address, Constants.COMMANDS[i], buffer[i]);
    }
    device.delay(200);
  }

  /**
   * Changes the address of the connected device to the given address. Please note that after a call of this method the address of this instance of {@link Display} is changed also. Further API calls
   * will use the new address provided here.
   * @param address The new address for the device.
   */
  public void setAddress(byte address) throws FTDIException {
    device.transactWrite(this.address, Constants.CMD_ADDRESS, address);
    device.delay(200);
    this.address = address;
  }

  /**
   * Changes the brightness of the display.
   * @param brightness The new brightness for the display. Minimum value is <code>0x01</code> and maximum value is <code>0xff</code>.
   */
  public void setBrightness(byte brightness) throws FTDIException {
    device.transactWrite(this.address, Constants.CMD_BRIGHTNESS, brightness);
    device.delay(200);
  }

  /**
   * Clears the display.
   */
  public void clear() throws FTDIException {
    device.delay(500);
    update(DataBuilder.empty(), true);
    device.delay(500);
  }

  /**
   * Updates the display with the given data. Only changed values will be sent to the device. If you need to force to update all values use {@link #update(byte[], boolean) update(byte[], true)}
   * @param data The new data buffer.
   */
  private void update(byte[] data) throws FTDIException {
    update(data, false);
  }

  /**
   * Updates the display with the given data. If <i>force</i> is set to <code>true</code>, all commands of {@link Constants#COMMANDS} will be sent to the device. If set to <code>false</code> only
   * commands for changed data will be sent.
   * @param data The new data buffer.
   * @param force If <code>true</code> all data is sent to the device, if <code>false</code> only changed data is sent.
   */
  private void update(byte[] data, boolean force) throws FTDIException {
    for(int i=0;i<data.length;i++) {
      if (force || buffer[i]!=data[i]) {
        device.transactWrite(this.address, Constants.COMMANDS[i], data[i]);
        buffer[i] = data[i];
      }
    }
  }

  /**
   * Displays the given float value with the provided number of decimal places. This will not show leading zeros. If you need to display leading zeros, use {@link #setFloat(float, int, boolean)}
   * instead.
   * @param value The float value.
   * @param decimals The number of decimal places. Can be between 0 and 3.
   */
  public void setFloat(float value, int decimals) throws FTDIException {
    setFloat(value, decimals, false);
  }

  /**
   * Displays the given float value with the provided number of decimal places. This will show leading zeros depending on the provided flag.
   * instead.
   * @param value The float value.
   * @param decimals The number of decimal places. Can be between 0 and 3.
   * @param leadingZeros If <code>true</code> leading zeros will be display, otherwise corresponding digits will remain empty.
   */
  public void setFloat(float value, int decimals, boolean leadingZeros) throws FTDIException {
    update(DataBuilder.fromFloat(value, decimals, leadingZeros));
  }

  /**
   * Displays the given int value. This will not show leading zeros. If you need to display leading zeros, use {@link #setInt(int, boolean)}
   * instead.
   * @param value The int value.
   */
  public void setInt(int value) throws FTDIException {
    setInt(value, false);
  }

  /**
   * Displays the given int value. This will not show leading zeros. This will show leading zeros depending on the provided flag.
   * instead.
   * @param value The int value.
   * @param leadingZeros If <code>true</code> leading zeros will be display, otherwise corresponding digits will remain empty.
   */
  public void setInt(int value, boolean leadingZeros) throws FTDIException {
    update(DataBuilder.fromInt(value, leadingZeros));
  }

  /**
   * Displays the given time value.
   * @param value The time value provided in format HH:mm.
   */
  public void setTime(String value) throws FTDIException {
    update(DataBuilder.fromTime(value));
  }
}
