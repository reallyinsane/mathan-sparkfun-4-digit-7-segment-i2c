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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataBuilderTest {

  @Test
  public void time() throws Exception {
    byte[] expected = {Constants.DIGIT_1, Constants.DIGIT_2, Constants.DIGIT_3, Constants.DIGIT_4, Constants.DECIMALS_TIME};
    byte[] actual = DataBuilder.fromTime("12:34");
    Assertions.assertArrayEquals(expected, actual);
  }
  @Test
  public void intZero() throws Exception {
    byte[] expected = {Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.DIGIT_0, Constants.EMPTY};
    byte[] actual = DataBuilder.fromInt(0, false);
    Assertions.assertArrayEquals(expected, actual);
  }
  @Test
  public void intZeroWithLeadingZeros() throws Exception {
    byte[] expected = {Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.EMPTY};
    byte[] actual = DataBuilder.fromInt(0, true);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithOneDecimal() throws Exception {
    byte[] expected = {Constants.EMPTY, Constants.EMPTY, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DECIMALS_1};
    byte[] actual = DataBuilder.fromFloat(0f, 1, false);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithTwoDecimals() throws Exception {
    byte[] expected = {Constants.EMPTY, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DECIMALS_2};
    byte[] actual = DataBuilder.fromFloat(0f, 2, false);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithOneDecimalWithLeadingZeros() throws Exception {
    byte[] expected = {Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DECIMALS_1};
    byte[] actual = DataBuilder.fromFloat(0f, 1, true);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithTwoDecimalsWithLeadingZeros() throws Exception {
    byte[] expected = {Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DECIMALS_2};
    byte[] actual = DataBuilder.fromFloat(0f, 2, true);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithThreeDecimals() throws Exception {
    byte[] expected = {Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DECIMALS_3};
    byte[] actual = DataBuilder.fromFloat(0f, 3, false);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithNoDecimals() throws Exception {
    byte[] expected = {Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.DIGIT_0, Constants.EMPTY};
    byte[] actual = DataBuilder.fromFloat(0f, 0, false);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithNoDecimalsWithLeadingZeros() throws Exception {
    byte[] expected = {Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.EMPTY};
    byte[] actual = DataBuilder.fromFloat(0f, 0, true);
    Assertions.assertArrayEquals(expected, actual);
  }

}
