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

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class implements vectors of floating-point numbers and static
 * methods for vector operations.
 * @author Tomasz Kapuściński
 */
public final class Vector implements Serializable
{
    private final float[] values;


    /**
     * Creates new vector with specific length.
     * @param length the length of new vector
     */
    public Vector(int length)
    {
        this.values = new float[length];
    }

    /**
     * Creates new vector from {@code float} array.
     * @param values the float array to initialize new vector
     */
    public Vector(float... values)
    {
        this.values = values.clone();
    }

    /**
     * Creates new vector from {@code float} array range.
     * @param values the array to initialize new vector
     * @param offset the offset within the array
     * @param count the number of elements to copy and length of new vector
     */
    public Vector(float[] values, int offset, int count)
    {
        this.values = Arrays.copyOfRange(values, offset, offset + count);
    }

    /**
     * Creates new vector from other vector.
     * @param other the other vector to copy values from.
     */
    public Vector(Vector other)
    {
        this(other.values);
    }

    /**
     * Creates new vector from other vector.
     * @param other the other vector to copy values from
     * @param count the number of elements to copy and length of new vector
     */
    public Vector(Vector other, int count)
    {
        this(other.values, 0, count);
    }

    /**
     * Creates new vector from other vector.
     * @param other the other vector to copy values from
     * @param first the first element
     * @param count the number of elements to copy and length of new vector
     */
    public Vector(Vector other, int first, int count)
    {
        this(other.values, first, count);
    }

    /**
     * Returns the number of elements in this vector.
     * @return the size
     */
    public int size()
    {
        return values.length;
    }

    /**
     * Returns value in this vector.
     * @param index the index
     * @return the value
     */
    public float get(int index)
    {
        return values[index];
    }

    /**
     * Changes value in this vector.
     * @param index the index
     * @param value the new value
     */
    public void set(int index, float value)
    {
        values[index] = value;
    }

    /**
     * Returns values from this vector.
     * @param values the array for returned values
     */
    public void get(float[] values)
    {
        System.arraycopy(this.values, 0, values, 0, values.length);
    }

    /**
     * Returns values from this vector.
     * @param first the first element to return
     * @param values the array for returned values
     * @param offset the offset to array
     * @param count the number of elements to return
     */
    public void get(int first, float[] values, int offset, int count)
    {
        System.arraycopy(this.values, first, values, offset, count);
    }

    /**
     * Changes values in this vector.
     * @param values the array with new values
     */
    public void set(float[] values)
    {
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    /**
     * Changes values in this vector.
     * @param first the first element to change
     * @param values the array with new values
     * @param offset the offset to array
     * @param count the number of elements to change
     */
    public void set(int first, float[] values, int offset, int count)
    {
        System.arraycopy(values, first, this.values, offset, count);
    }

    /**
     * Normalizes this vector.
     */
    public void normalize()
    {
        normalize(values);
    }

    /**
     * Calculates the lenght of this vector.
     * @return the length
     */
    public float length()
    {
        return length(values);
    }

    @Override
    public String toString()
    {
        int count = values.length;
        if (count == 0) return "";

        StringBuilder builder = new StringBuilder();
        builder.append(values[0]);

        for (int i = 1; i < count; i++)
        {
            builder.append(' ');
            builder.append(values[i]);
        }

        return builder.toString();
    }




    /* ************************************************
     * ************    STATIC METHODS   ***************
     * ************************************************
     */

    /**
     * Calculates dot product of two vectors.
     * @param first the first vector
     * @param second the second vector
     * @return the calculated dot product
     */
    public static float dot(Vector first, Vector second)
    {
        return dot(first.values, second.values);
    }

    /**
     * Calculates dot product of two vector.
     * @param first the first vector
     * @param second the second vector
     * @return the calculated dot product
     */
    public static float dot(float[] first, float[] second)
    {
        float result = 0.0f;

        int count = first.length;
        if (count != second.length)
            throw new IllegalArgumentException("Incompatible arrays");

        for (int i = 0; i < count; i++)
        {
            result += first[i] * second[i];
        }

        return result;
    }

    /**
     * Calculates cross product of two vectors.
     * @param first the first vector
     * @param second the second vector
     * @return the calculated cross product
     */
    public static Vector cross(Vector first, Vector second)
    {
        return cross(first, second, null);
    }

    /**
     * Calculates cross products of two vectors.
     * If {@code result} is {@code null}, new vector will be created
     * and returned. Otherwise, {@code result} will be used to store
     * the result and returned by this method.
     *
     * @param first the first vector
     * @param second the second vector
     * @param result the vector to store result in
     * @return the calculated cross product
     */
    public static Vector cross(Vector first, Vector second, Vector result)
    {
        if (result == null) result = new Vector(3);

        cross(first.values, second.values, result.values);

        return result;
    }

    /**
     * Calculates cross product of two vectors.
     * @param first first vector
     * @param second second vector
     * @param result the result of calculations
     */
    public static void cross(float[] first, float[] second, float[] result)
    {
        result[0] = first[1] * second[2] - first[2] * second[1];
        result[1] = first[2] * second[0] - first[0] * second[2];
        result[2] = first[0] * second[1] - first[1] * second[0];
    }

    /**
     * Normalizes the vector.
     * @param values vector to normalize
     */
    public static void normalize(float[] values)
    {
        normalize(values, 0, values.length);
    }

    /**
     * Normalizes the vector.
     * @param values the vector to normalize
     * @param count the number of elements
     */
    public static void normalize(float[] values, int count)
    {
        normalize(values, 0, count);
    }

    /**
     * Normalizes the vector.
     * @param values the vector
     * @param first the first element
     * @param count the number of elements
     */
    public static void normalize(float[] values, int first, int count)
    {
        float sum = 0.0f;
        int last = first + count;

        for (int i = first; i < last; i++)
        {
            sum += values[i] * values[i];
        }

        float scale = 1.0f / (float) Math.sqrt(sum);

        for (int i = first; i < last; i++)
        {
            values[i] *= scale;
        }
    }

    /**
     * Calculates length of the vector.
     * @param values the vector
     * @return the calculated length
     */
    public static float length(float[] values)
    {
        return length(values, 0, values.length);
    }

    /**
     * Calculates lenght of the vector.
     * @param values the vector
     * @param count the number of elements
     * @return the calculated length
     */
    public static float length(float[] values, int count)
    {
        return length(values, 0, count);
    }

    /**
     * Calculates length of the vector.
     * @param values the vector
     * @param first the first element
     * @param count the number of elements
     * @return the calculated length
     */
    public static float length(float[] values, int first, int count)
    {
        float sum = 0.0f;
        int last = first + count;

        for (int i = first; i < last; i++)
        {
            sum += values[i] * values[i];
        }

        return (float) Math.sqrt(sum);
    }

    /**
     * Copies values from one vector to another.
     * The number of elements copied is determined using the destination vector.
     * @param from the source vector
     * @param to the destination vector
     */
    public static void copy(Vector from, Vector to)
    {
        copy(from, 0, to, 0, to.size());
    }

    /**
     * Copies values from one vector to another.
     * @param from the source vector
     * @param firstFrom the first element in source vector
     * @param to the destination vector
     * @param firstTo the first element in destination vector
     * @param count the number of elements to copy
     */
    public static void copy(Vector from, int firstFrom,
            Vector to, int firstTo, int count)
    {
        for (int i = 0; i < count; i++)
        {
            to.values[firstFrom + i] = from.values[firstFrom + i];
        }
    }
}
