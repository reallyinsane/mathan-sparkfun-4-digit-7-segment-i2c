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
import net.sf.yad2xx.FTDIException;
import net.sf.yad2xx.mpsse.I2C;

public class Display {
  
  public interface Constants {

    byte ADDRESS_DEFAULT = 0x71;

    byte EMPTY = (byte) 0x00;
    byte DIGIT_0 = (byte) 0x3F;
    byte DIGIT_1 = (byte) 0x06;
    byte DIGIT_2 = (byte) 0x5b;
    byte DIGIT_3 = (byte) 0x4f;
    byte DIGIT_4 = (byte) 0x66;
    byte DIGIT_5 = (byte) 0x6d;
    byte DIGIT_6 = (byte) 0x7d;
    byte DIGIT_7 = (byte) 0x07;
    byte DIGIT_8 = (byte) 0x7f;
    byte DIGIT_9 = (byte) 0x6f;

    byte DECIMALS_3 = (byte) 0x01;
    byte DECIMALS_2 = (byte) 0x02;
    byte DECIMALS_1 = (byte) 0x04;
    byte DECIMALS_TIME = (byte) 0x10;

    byte CMD_BRIGHTNESS = 0x7A;
    byte CMD_DECIMALS = 0x77;
    byte CMD_1ST = 0x7b;
    byte CMD_2ND = 0x7c;
    byte CMD_3RD = 0x7d;
    byte CMD_4TH = 0x7e;
    byte CMD_INIT = 0x76;
    byte CMD_ADDRESS = (byte) 0x80;

    byte[] DIGITS = {DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4, DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9};
    byte[] COMMANDS = {CMD_1ST, CMD_2ND, CMD_3RD, CMD_4TH, CMD_DECIMALS, CMD_BRIGHTNESS};
  }
  private byte[] buffer = { Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, (byte) 0xff};

  private static DateFormat DF = new SimpleDateFormat("HH:mm");
  private final I2C device;
  private final byte address;

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

  public void setAddress(byte address) throws FTDIException {
    device.transactWrite(this.address, Constants.CMD_ADDRESS, address);
    device.delay(200);
  }

  public void clear() throws FTDIException {
    device.delay(500);
    update(DataBuilder.empty(), true);
    device.delay(500);
  }

  public void update(byte[] data) throws FTDIException {
    update(data, false);
  }

  public void update(byte[] data, boolean force) throws FTDIException {
    for(int i=0;i<data.length;i++) {
      if (force || buffer[i]!=data[i]) {
        device.transactWrite(this.address, Constants.COMMANDS[i], data[i]);
        buffer[i] = data[i];
      }
    }
  }

  public void setFloat(float value, int decimals) throws FTDIException {
    setFloat(value, decimals, false);
  }

  public void setFloat(float value, int decimals, boolean leadingZeros) throws FTDIException {
    update(DataBuilder.fromFloat(value, decimals, leadingZeros));
  }


  public void setInt(int value) throws FTDIException {
    setInt(value, false);
  }

  public void setInt(int value, boolean leadingZeros) throws FTDIException {
    update(DataBuilder.fromInt(value, leadingZeros));
  }

  public void setTime(String value) throws FTDIException {
    update(DataBuilder.fromTime(value));
  }
}
