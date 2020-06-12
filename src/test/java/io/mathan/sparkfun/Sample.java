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
import java.util.Date;
import net.sf.yad2xx.Device;
import net.sf.yad2xx.FTDIException;
import net.sf.yad2xx.FTDIInterface;
import net.sf.yad2xx.mpsse.I2C;

public class Sample {
  public static final byte ADDRESS_BLUE =0x72;
  public static final byte ADDRESS_RED =0x73;
  public static final byte ADDRESS_WHITE =0x74;

  public static void main(String[] args) throws FTDIException, InterruptedException {
    System.loadLibrary("FTDIInterface");
    // Get all available FTDI Devices
    Device[] devices = FTDIInterface.getDevices();
    Device device = devices[0];
    I2C d = new I2C(device);
    d.open();
    Display white = new Display(d, ADDRESS_WHITE);
    Display red = new Display(d, ADDRESS_RED);

    int value= 0;
    white.clear();
    red.clear();
    DateFormat df1 = new SimpleDateFormat("hh:mm");
    DateFormat df2 = new SimpleDateFormat("mm:ss");
    for(int i=0;i<1000;i++) {
      Date date = new Date();
      white.setTime(df1.format(date));
      red.setTime(df2.format(date));
      d.delay(20);
      value++;
    }
    white.clear();
    red.clear();
//    d.delay(200);
    d.close();
  }

}
