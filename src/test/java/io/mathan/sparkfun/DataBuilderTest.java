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
    byte[] actual = DataBuilder.fromInt(0);
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
    byte[] actual = DataBuilder.fromFloat(0f, 1);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void floatZeroWithTwoDecimals() throws Exception {
    byte[] expected = {Constants.EMPTY, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DIGIT_0, Constants.DECIMALS_2};
    byte[] actual = DataBuilder.fromFloat(0f, 2);
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
    byte[] actual = DataBuilder.fromFloat(0f, 3);
    Assertions.assertArrayEquals(expected, actual);
  }
}
