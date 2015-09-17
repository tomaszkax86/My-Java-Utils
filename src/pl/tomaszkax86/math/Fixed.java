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
 * Class for storage, calculations, and conversion between numberic formats
 * and fixed-point Q16.16 format.
 * 
 * @author Tomasz Kapuściński
 */
public final class Fixed extends Number
{
    // binary representation of this fixed value
    private final int fixed;


    // Creates new fixed-point using fixed-point bits
    private Fixed(int fixed, boolean nul)
    {
        this.fixed = fixed;
    }
    
    /**
     * Creates new fixed-point value from {@code short}
     * @param value {@code short} value
     */
    public Fixed(short value)
    {
        this.fixed = toFixed(value);
    }

    /**
     * Creates new fixed-point value from {@code int}
     * @param value {@code int} value
     */
    public Fixed(int value)
    {
        this.fixed = toFixed(value);
    }

    /**
     * Creates new fixed-point value from {@code float}
     * @param value {@code float} value
     */
    public Fixed(float value)
    {
        this.fixed = toFixed(value);
    }

    /**
     * Creates new fixed-point value from {@code double}
     * @param value {@code double} value
     */
    public Fixed(double value)
    {
        this.fixed = toFixed(value);
    }

    @Override
    public int intValue()
    {
        return toInt(fixed);
    }

    @Override
    public long longValue()
    {
        return toLong(fixed);
    }

    @Override
    public float floatValue()
    {
        return toFloat(fixed);
    }

    @Override
    public double doubleValue()
    {
        return toDouble(fixed);
    }
    
    @Override
    public String toString()
    {
        return Float.toString(toFloat(fixed));
    }

    /**
     * Converts {@code short} value to fixed-point representation.
     * @param value {@code short} value
     * @return fixed-point representation as {@code int}
     */
    public static int toFixed(short value)
    {
        return (value << BIT_SHIFT);
    }

    /**
     * Converts {@code int} value to fixed-point representation.
     * @param value {@code int} value
     * @return fixed-point representation as {@code int}
     */
    public static int toFixed(int value)
    {
        return (value << BIT_SHIFT);
    }

    /**
     * Converts {@code long} value to fixed-point representation.
     * @param value {@code long} value
     * @return fixed-point representation as {@code int}
     */
    public static int toFixed(long value)
    {
        return (int)(value << BIT_SHIFT);
    }

    /**
     * Converts {@code float} value to fixed-point representation.
     * @param value {@code float} value
     * @return fixed-point representation as {@code int}
     */
    public static int toFixed(float value)
    {
        return (int) Math.scalb(value, BIT_SHIFT);
    }

    /**
     * Converts {@code double} value to fixed-point representation.
     * @param value {@code double} value
     * @return fixed-point representation as {@code int}
     */
    public static int toFixed(double value)
    {
        return (int) Math.scalb(value, BIT_SHIFT);
    }

    /**
     * Converts fixed-point representation to {@code short} value.
     * @param fixed fixed-point value
     * @return {@code short} value
     */
    public static short toShort(int fixed)
    {
        return (short)(fixed >> BIT_SHIFT);
    }

    /**
     * Converts fixed-point representation to {@code int} value.
     * @param fixed fixed-point value
     * @return {@code int} value
     */
    public static int toInt(int fixed)
    {
        return (fixed >> BIT_SHIFT);
    }

    /**
     * Converts fixed-point representation to {@code long} value.
     * @param fixed fixed-point value
     * @return {@code long} value
     */
    public static long toLong(int fixed)
    {
        return (fixed >> BIT_SHIFT);
    }

    /**
     * Converts fixed-point representation to {@code float} value.
     * @param fixed fixed-point value
     * @return {@code float} value
     */
    public static float toFloat(int fixed)
    {
        return Math.scalb((float) fixed, -BIT_SHIFT);
    }

    /**
     * Converts fixed-point representation to {@code double} value.
     * @param fixed fixed-point value
     * @return {@code double} value
     */
    public static double toDouble(int fixed)
    {
        return Math.scalb((double) fixed, -BIT_SHIFT);
    }
    
    /**
     * Returns {@code Fixed} object from fixed-point representation.
     * @param fixed fixed-point representation
     * @return {@code Fixed} object
     */
    public static Fixed asFixed(int fixed)
    {
        return new Fixed(fixed, false);
    }

    /**
     * Adds two fixed-point values.
     * @param x first value
     * @param y second value
     * @return sum of two values
     */
    public static int add(int x, int y)
    {
        return x + y;
    }

    /**
     * Subtracts two fixed-point values.
     * @param x first value
     * @param y second value
     * @return difference between two values
     */
    public static int subtract(int x, int y)
    {
        return x - y;
    }

    /**
     * Multiplies two fixed-point values.
     * @param x first value
     * @param y second value
     * @return product of two values
     */
    public static int multiply(int x, int y)
    {
        return (int) (((long)x * (long)y) >> BIT_SHIFT);
    }

    /**
     * Divides two fixed-point values.
     * @param x first value
     * @param y second value
     * @return quotient of two values
     */
    public static int divide(int x, int y)
    {
        return (int) (((long)x << BIT_SHIFT) / (long)y);
    }

    /**
     * Computes square root of fixed-point value.
     * @param fixed fixed-point value
     * @return square root as fixed-point value
     */
    public static int sqrt(int fixed)
    {
        long fix = (long)fixed << BIT_SHIFT;
        long result = fixed >> 1;

        for(int i=0; i<5; i++)
        {
            result = (result + fix / result) >> 1;
        }

        return (int) result;
    }

    
    /**
     * The number of bytes used to represent fixed-point value.
     */
    public static final int BYTES = 4;

    /**
     * The number of bits used to represent fixed-point value.
     */
    public static final int SIZE = 32;

    /**
     * A constant holding the value of 0 in fixed-point format.
     */
    public static final int ZERO = 0b00000000_00000000_00000000_00000001;

    /**
     * A constant holding the value of 1 in fixed-point format.
     */
    public static final int ONE = 0b00000000_00000001_00000000_00000000;

    /**
     * A constant holding the largest positive fixed-point value.
     */
    public static final int MAX_VALUE = (int) 0b01111111_111111111_11111111_11111111;

    /**
     * A constant holding the smallest positive nonzero fixed-point value.
     */
    public static final int MIN_VALUE = 0b00000000_00000000_00000000_00000001;

    
    // constant bit shift for Q16.16 format
    private static final int BIT_SHIFT = 16;
}
