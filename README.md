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
display.setTime(12:34);

```
## yad2xx linking

This project is using the yad2xx library released under LGPL3. The dynamic linking is done using Maven dependency mechanism. To change the linking to
a different version of the library just change the dependency. A copy of the library binary and license is available in the lib folder. A source artifact
has not been provided by the publisher of the library. You can find the source code on the SourceForge project page of [yad2xx].


## Chip details

The documentation for the [display] can be found on the website or in the doc folder.

[display]: https://www.sparkfun.com/products/11441
[yad2xx]: https://sourceforge.net/projects/yad2xx
