<a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/license-apache2-blue.svg"></a>

# yad2xx extension for Sparkfun 4 digit 7 segment [display] using I2C

This is Java API extension to [yad2xx]. The four digits can be handled as int, float or time values. 

```
// configure an I2C instance
I2C device;
// create a Display instance
Display display = new Display(device);
// clear the display
display.clear();
// display a value
display.setInt(9999);
display.setFloat(99.99);
display.setTime("12:34");
```

## Available API

The following table lists the available API methods.

Method|Parameter(s)|Description|
------|------------|-----------|
clear()| | Clears the display.|
setAddress(byte address)      | The new I2C address for the device. | Please see documentation of the [display] for details about valid addresses. |
setBrightness(byte brightness)   | The new value for brightness. | The brightness of the display can be changed between 0x01 (minimum) and 0xff (maximum).
setFloat(float value, int decimals)| The float value and the number of decimal places.| Displays the float value. The number of decimal places can be 0 to 3. (No leading zeros shown)|
setFloat(float value, int decimals, boolean leadingZeros)| The float value, the number of decimal places and a flag enabling/disabling display of leading zeros.| Displays the float value. The number of decimal places can be 0 to 3. Leading zeros are shown depending on the given flag.|
setInt(int value)| The int value. | Displays the int value without leading zeros.|
setInt(int value, boolean leadingZeros)| The int value and a flag enabling/disabling display of leading zeros.| Displays the int value.  Leading zeros are shown depending on the given flag.|
setTime(String time)| The time in format HH:mm. | Displays the time value.|



## yad2xx linking

This project is using the yad2xx library released under LGPL3. The dynamic linking is done using Maven dependency mechanism. To change the linking to
a different version of the library just change the dependency. A copy of the library binary and license is available in the lib folder. A source artifact
has not been provided by the publisher of the library. You can find the source code on the SourceForge project page of [yad2xx].


## Chip details

The documentation for the [display] can be found on the website or in the doc folder.

[display]: https://www.sparkfun.com/products/11441
[yad2xx]: https://sourceforge.net/projects/yad2xx
