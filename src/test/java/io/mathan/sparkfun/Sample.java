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

import net.sf.yad2xx.Device;
import net.sf.yad2xx.FTDIException;
import net.sf.yad2xx.FTDIInterface;
import net.sf.yad2xx.mpsse.I2C;

public class Sample {

  public static void main(String[] args) throws FTDIException, InterruptedException {
    System.loadLibrary("FTDIInterface");
    // Get all available FTDI Devices
    Device[] devices = FTDIInterface.getDevices();
    Device device = devices[0];
    I2C d = new I2C(device);
    d.open();
    Display display = new Display(d);
    int value= 0;
    display.clear();
    for(int i=0;i<1000;i++) {
      display.setInt(value);
      d.delay(20);
      value++;
    }
    display.clear();
    d.delay(200);
    d.close();
  }

}
