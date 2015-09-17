/*
 * Copyright (c) 2015, Tomasz Kapuściński
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pl.tomaszkax86.math;

/**
 * Class for storage and conversion between numeric formats and
 * half-precision floating-point format (half-floats).
 *
 * @author Tomasz Kapuściński
 */
public final class Half extends Number
{
    // binary representation of this half-float
    private final short half;

    /**
     * Creates new half-float using {@code int} value.
     * @param value value to use
     */
    public Half(int value)
    {
        this.half = toHalf(value);
    }

    /**
     * Creates new half-float using {@code long} value.
     * @param value value to use
     */
    public Half(long value)
    {
        this.half = toHalf(value);
    }

    /**
     * Creates new half-float using {@code float} value.
     * @param value value to use
     */
    public Half(float value)
    {
        this.half = toHalf(value);
    }

    /**
     * Creates new half-float using {@code double} value.
     * @param value value to use
     */
    public Half(double value)
    {
        this.half = toHalf(value);
    }

    /**
     * Returns binary representation of this value as {@code short}.
     * @return short value
     */
    public short toShortBits()
    {
        return half;
    }

    /**
     * Returns binary representation of this value as {@code int}.
     * Only 16 lower bits are used.
     * @return int value
     */
    public int toIntBits()
    {
        return Short.toUnsignedInt(half);
    }

    @Override
    public int intValue()
    {
        return toInt(half);
    }

    @Override
    public long longValue()
    {
        return toLong(half);
    }

    @Override
    public float floatValue()
    {
        return toFloat(half);
    }

    @Override
    public double doubleValue()
    {
        return toDouble(half);
    }

    @Override
    public String toString()
    {
        return Float.toString(toFloat(half));
    }

    @Override
    public int hashCode()
    {
        return half;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this) return true;

        if (object instanceof Half)
        {
            Half other = (Half) object;

            return this.half == other.half;
        }

        return false;
    }

    /**
     * Converts half-float to {@code int}.
     * @param half half-float to convert
     * @return value as {@code int}
     */
    public static int toInt(short half)
    {
        return (int) toFloat(half);
    }

    /**
     * Converts half-float to {@code long}
     * @param half half-float to convert
     * @return value as {@code long}
     */
    public static long toLong(short half)
    {
        return (int) toFloat(half);
    }

    /**
     * Converts half-float to {@code float}
     * @param half half-float to convert
     * @return value as {@code float}
     */
    public static float toFloat(short half)
    {
        int sign = (half >= 0 ? 1 : -1);
        int exponent = ((EXPONENT_MASK & half) >> 10) - 15;
        int significand = SIGNIFICAND_MASK & half;

        // infinity or NaN
        if (exponent == 16)
        {
            // infinity
            if (significand == 0)
            {
                return Math.copySign(Float.POSITIVE_INFINITY, sign);
            }
            // generic NaN, no decoding
            else
            {
                return Math.copySign(Float.NaN, sign);
            }
        }
        // zero or subnormal
        else if (exponent == -15)
        {
            // signed zero
            if (significand == 0)
            {
                return Math.copySign(0.0f, sign);
            }
            // subnormal
            else
            {
                return Math.copySign(Math.scalb(significand, exponent - 10), sign);
            }
        }
        // normal value
        else
        {
            // add leading bit
            significand |= SIGN_MASK;

            return Math.copySign(Math.scalb(significand, exponent - 10), sign);
        }
    }

    /**
     * Converts half-float to {@code double}
     * @param half half-float to convert
     * @return converted value as {@code double}
     */
    public static double toDouble(short half)
    {
        return toFloat(half);
    }

    /**
     * Converts {@code int} value to half-float and returns as short.
     * @param value {@code int} value
     * @return half-float value
     */
    public static short toHalf(int value)
    {
        return toHalf((float) value);
    }

    /**
     * Converts {@code long} value to half-float and returns as short.
     * @param value {@code long} value
     * @return half-float value
     */
    public static short toHalf(long value)
    {
        return toHalf((float) value);
    }

    /**
     * Converts {@code float} value to half-float and returns as short.
     * @param value {@code float} value
     * @return half-float value
     */
    public static short toHalf(float value)
    {
        int signBit = (Math.signum(value) >= 0 ? 0 : 1 << 15);

        // NaN
        if (Float.isNaN(value))
        {
            return (short) (signBit | NaN);
        }

        int exponent = Math.getExponent(value);

        // clamp to infinity
        if (exponent > 15)
        {
            return (short) (signBit | POSITIVE_INFINITY);
        }
        // clamp to zero
        else if (exponent < -14)
        {
            return (short) (signBit);
        }

        int exponentBits = (0b11111 & (exponent + 15)) << 10;
        int significandBits = SIGNIFICAND_MASK & (int) Math.scalb(Math.abs(value), 10 - exponent);

        return (short) (signBit | exponentBits | significandBits);
    }

    /**
     * Converts {@code double} value to half-float and returns as short.
     * @param value {@code double} value
     * @return half-float value
     */
    public static short toHalf(double value)
    {
        return toHalf((float) value);
    }

    /**
     * Converts half-float to {@code String}.
     * @param half half-float
     * @return returned string
     */
    public static String toString(short half)
    {
        return Float.toString(toFloat(half));
    }

    /**
     * Adds two half-float values.
     * This method is provided to simplify simple operations. Internally it
     * converts half-floats to {@code float} and then back to half-float.
     * For more complex calculations it is preferred that you perform
     * calculations in {@code float} or {@code double} formats.
     * @param x first half-float
     * @param y second half-float
     * @return sum of two half-floats
     */
    public static short add(short x, short y)
    {
        return toHalf(toFloat(x) + toFloat(y));
    }

    /**
     * Subtracts two half-float values.
     * This method is provided to simplify simple operations. Internally it
     * converts half-floats to {@code float} and then back to half-float.
     * For more complex calculations it is preferred that you perform
     * calculations in {@code float} or {@code double} formats.
     * @param x first half-float
     * @param y second half-float
     * @return difference between two half-floats
     */
    public static short subtract(short x, short y)
    {
        return toHalf(toFloat(x) - toFloat(y));
    }

    /**
     * Multiplies two half-float values.
     * This method is provided to simplify simple operations. Internally it
     * converts half-floats to {@code float} and then back to half-float.
     * For more complex calculations it is preferred that you perform
     * calculations in {@code float} or {@code double} formats.
     * @param x first half-float
     * @param y second half-float
     * @return product of two half-floats
     */
    public static short multiply(short x, short y)
    {
        return toHalf(toFloat(x) * toFloat(y));
    }

    /**
     * Divides two half-float values.
     * This method is provided to simplify simple operations. Internally it
     * converts half-floats to {@code float} and then back to half-float.
     * For more complex calculations it is preferred that you perform
     * calculations in {@code float} or {@code double} formats.
     * @param x first half-float
     * @param y second half-float
     * @return result of division of two half-floats
     */
    public static short divide(short x, short y)
    {
        return toHalf(toFloat(x) / toFloat(y));
    }

    /**
     * The number of bytes used to represent half-float value.
     */
    public static final int BYTES = 2;

    /**
     * The number of bits used to represent half-float value.
     */
    public static final int SIZE = 16;

    /**
     * Maximum exponent a finite half-float can have.
     */
    public static final int MAX_EXPONENT = 15;

    /**
     * Minimum exponent a finite half-float can have.
     */
    public static final int MIN_EXPONENT = -14;

    /**
     * A constant holding Not-a-Number (NaN) half-float value.
     */
    public static final short NaN = (short) 0b0_11111_1111111111;

    /**
     * A constant holding the positive infinity half-float value.
     */
    public static final short POSITIVE_INFINITY = (short) 0b0_11111_0000000000;

    /**
     * A constant holding the negative infinity half-float value.
     */
    public static final short NEGATIVE_INFINITY = (short) 0b1_11111_0000000000;

    /**
     * A constant holding the value of 0 in half-float format.
     */
    public static final short ZERO = (short) 0b0_00000_0000000000;

    /**
     * A constant holding the value of 1 in half-float format.
     */
    public static final short ONE = (short) 0b0_01111_0000000000;

    /**
     * A constant holding the largest positive finite half-float value.
     */
    public static final short MAX_VALUE = (short) 0b0_11110_1111111111;

    /**
     * A constant holding the smallest positive nonzero half-float value.
     */
    public static final short MIN_VALUE = (short) 0b0_00000_0000000001;

    /**
     * A constant holding the smallest positive normal half-float value.
     */
    public static final short MIN_NORMAL = (short) 0b0_00001_0000000000;

    private static final int SIGN_MASK = 0b1_00000_0000000000;
    private static final int EXPONENT_MASK = 0b0_11111_0000000000;
    private static final int SIGNIFICAND_MASK = 0b0_00000_1111111111;
}
