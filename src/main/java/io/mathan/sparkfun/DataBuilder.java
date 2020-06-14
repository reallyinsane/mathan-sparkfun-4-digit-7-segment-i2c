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

import io.mathan.sparkfun.Display.Constants;

/**
 * The DataBuilder provides methods for generating the data arrays which can be sent to the device. The data array will be of length 5 always and contains the data for the commands
 * {@link Constants#CMD_1ST}, {@link Constants#CMD_2ND}, {@link Constants#CMD_3RD}, {@link Constants#CMD_4TH} and {@link Constants#CMD_DECIMALS} in this particular order.
 */
public class DataBuilder {

  public static byte[] empty() {
    byte[] data = new byte[5];
    data[0] = Constants.EMPTY;
    data[1] = Constants.EMPTY;
    data[2] = Constants.EMPTY;
    data[3] = Constants.EMPTY;
    data[4] = Constants.EMPTY;
    return data;
  }

  public static byte[] fromTime(String value) {
    byte[] data = new byte[5];
    data[0] = Constants.DIGITS[Integer.parseInt(""+value.charAt(0))];
    data[1] = Constants.DIGITS[Integer.parseInt(""+value.charAt(1))];
    data[2] = Constants.DIGITS[Integer.parseInt(""+value.charAt(3))];
    data[3] = Constants.DIGITS[Integer.parseInt(""+value.charAt(4))];
    data[4] = Constants.DECIMALS_TIME;
    return data;
  }

  public static byte[] fromInt(int value, boolean leadingZeros) {
    return fromInt(value, leadingZeros, leadingZeros, leadingZeros);
  }

  private static byte[] fromInt(int value, boolean leadingZerosThousands, boolean leadingZeroHundreds, boolean leadingZeroTens) {
    value = value%10000;
    int thousands = value/1000;
    value%=1000;
    int hundrets = value/100;
    value%=100;
    int tens = value/10;
    value%=10;
    byte[] data = new byte[5];
    data[4] = Constants.EMPTY;
    if(thousands>0) {
      data[0] = Constants.DIGITS[thousands];
    } else {
      data[0] = leadingZerosThousands ? Constants.DIGIT_0 : Constants.EMPTY;
    }
    if(hundrets>0||thousands>0) {
      data[1] = Constants.DIGITS[hundrets];
    } else {
      data[1] = leadingZeroHundreds ? Constants.DIGIT_0 : Constants.EMPTY;
    }
    if(tens>0||thousands>0||hundrets>0) {
      data[2] = Constants.DIGITS[tens];
    } else {
      data[2] = leadingZeroTens ? Constants.DIGIT_0 : Constants.EMPTY;
    }
    if(value>0||thousands>0||hundrets>0||tens>0) {
      data[3] = Constants.DIGITS[value];
    } else {
      data[3] = Constants.DIGIT_0;
    }
    return data;
  }

  public static byte[] fromFloat(float value, int decimals, boolean leadingZeros) {
    if (decimals == 0) {
      return fromInt(Float.valueOf(value).intValue(), leadingZeros);
    } else {
      byte[] data = new byte[5];
      byte d =0x00;
      switch(decimals) {
        case 1:
          data = fromInt((int) (value*10), leadingZeros, leadingZeros, true);
          data[4] = Constants.DECIMALS_1;
          break;
        case 2:
          data = fromInt((int) (value*100), leadingZeros, true, true);
          data[4] = Constants.DECIMALS_2;
          break;
        case 3:
          data = fromInt((int) (value*1000), true, true, true);
          data[4] = Constants.DECIMALS_3;
          break;
      }
      return data;
    }
  }


}
