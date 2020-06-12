package io.mathan.sparkfun;

import io.mathan.sparkfun.Display.Constants;

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
  public static byte[] fromInt(int value) {
    return fromInt(value, false);
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
  public static byte[] fromFloat(float value, int decimals) {
    return fromFloat(value, decimals, false);
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
