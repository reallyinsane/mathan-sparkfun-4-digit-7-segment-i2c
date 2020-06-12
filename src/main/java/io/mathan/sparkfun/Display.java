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

  private static final byte ADDRESS_DEFAULT = 0x71;

  private static final byte EMPTY =  (byte) 0x00;
  private static final byte DIGIT_0 =  (byte) 0x3F;
  private static final byte DIGIT_1 =  (byte) 0x06;
  private static final byte DIGIT_2 =  (byte) 0x5b;
  private static final byte DIGIT_3 =  (byte) 0x4f;
  private static final byte DIGIT_4 =  (byte) 0x66;
  private static final byte DIGIT_5 =  (byte) 0x6d;
  private static final byte DIGIT_6 =  (byte) 0x7d;
  private static final byte DIGIT_7 =  (byte) 0x07;
  private static final byte DIGIT_8 =  (byte) 0x7f;
  private static final byte DIGIT_9 =  (byte) 0x6f;

  private static final byte DECIMALS_3 = (byte) 0x01;
  private static final byte DECIMALS_2 = (byte) 0x02;
  private static final byte DECIMALS_1 = (byte) 0x04;
  private static final byte DECIMALS_TIME = (byte)0x10;

  private static final byte CMD_BRIGHTNESS = 0x7A;
  private static final byte CMD_DECIMALS = 0x77;
  private static final byte CMD_1ST = 0x7b;
  private static final byte CMD_2ND = 0x7c;
  private static final byte CMD_3RD = 0x7d;
  private static final byte CMD_4TH = 0x7e;
  private static final byte CMD_INIT= 0x76;
  private static final byte CMD_ADDRESS = (byte)0x80;

  private static final byte[] DIGITS = {DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4, DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9};
  private static final byte[] COMMANDS = {CMD_1ST, CMD_2ND, CMD_3RD, CMD_4TH, CMD_DECIMALS, CMD_BRIGHTNESS};
  private byte[] buffer = {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, (byte) 0xff};

  private static DateFormat DF = new SimpleDateFormat("HH:mm");
  private final I2C device;
  private final byte address;

  public Display(I2C device) throws FTDIException {
    this(device, ADDRESS_DEFAULT);
  }

  public Display(I2C device, byte address) throws FTDIException {
    this.device = device;
    this.address = address;
    device.transactWrite(this.address, CMD_INIT);
    for(int i=0;i<COMMANDS.length;i++) {
      device.transactWrite(this.address, COMMANDS[i], buffer[i]);
    }
    device.delay(200);
  }

  public void setAddress(byte address) throws FTDIException {
    device.transactWrite(this.address, CMD_ADDRESS, address);
    device.delay(200);
  }

  public void clear() throws FTDIException {
    device.delay(500);
    byte[] data = new byte[5];
    data[0] = EMPTY;
    data[1] = EMPTY;
    data[2] = EMPTY;
    data[3] = EMPTY;
    data[4] = EMPTY;
    update(data, true);
    device.delay(500);
  }

  public void update(byte[] data) throws FTDIException {
    update(data, false);
  }

  public void update(byte[] data, boolean force) throws FTDIException {
    for(int i=0;i<data.length;i++) {
      if (force || buffer[i]!=data[i]) {
        device.transactWrite(this.address, COMMANDS[i], data[i]);
        buffer[i] = data[i];
      }
    }
  }

  public void setFloat(float value, int decimals) throws FTDIException {
    if (decimals == 0) {
      setInt(Float.valueOf(value).intValue(), false);
    } else {
      byte[] data = new byte[5];
      byte d =0x00;
      switch(decimals) {
        case 1:
          data = getInt((int) (value*10), true);
          data[4] = DECIMALS_1;
          break;
        case 2:
          data = getInt((int) (value*100), true);
          data[4] = DECIMALS_2;
          break;
        case 3:
          data = getInt((int) (value*1000), true);
          data[4] = DECIMALS_3;
          break;
      }
      update(data);
    }
  }

  public void setInt(int value) throws FTDIException {
    setInt(value, false);
  }

  public void setInt(int value, boolean leadingZero) throws FTDIException {
    update(getInt(value, leadingZero));
  }

  public void setTime(String value) throws FTDIException {
    byte[] data = new byte[5];
    data[0] = DIGITS[Integer.parseInt(""+value.charAt(0))];
    data[1] = DIGITS[Integer.parseInt(""+value.charAt(1))];
    data[2] = DIGITS[Integer.parseInt(""+value.charAt(3))];
    data[3] = DIGITS[Integer.parseInt(""+value.charAt(4))];
    data[4] = DECIMALS_TIME;
    update(data);
  }

  private byte[] getInt(int value, boolean leadingZero) {
    value = value%10000;
    int thousands = value/1000;
    value%=1000;
    int hundrets = value/100;
    value%=100;
    int tens = value/10;
    value%=10;
    byte[] data = new byte[5];
    data[4] = EMPTY;
    if(thousands>0) {
      data[0] = DIGITS[thousands];
    } else {
      data[0] = leadingZero ? DIGIT_0 : EMPTY;
    }
    if(hundrets>0||thousands>0) {
      data[1] = DIGITS[hundrets];
    } else {
      data[1] = leadingZero ? DIGIT_0 : EMPTY;
    }
    if(tens>0||thousands>0||hundrets>0) {
      data[2] = DIGITS[tens];
    } else {
      data[2] = leadingZero ? DIGIT_0 : EMPTY;
    }
    if(value>0||thousands>0||hundrets>0||tens>0) {
      data[3] = DIGITS[value];
    } else {
      data[3] = DIGIT_0;
    }
    return data;
  }
}
