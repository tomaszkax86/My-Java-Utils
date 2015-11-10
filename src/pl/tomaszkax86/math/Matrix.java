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
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Class implementing matrix storage and operations.
 *
 * Contains various methods for creating matrices for OpenGL usage.
 *
 * @author Tomasz Kapuściński
 */
public final class Matrix implements Serializable
{
    // matrix dimensions and content
    private final int rows, columns;
    private float[][] values;

    // auxiliary matrices to speed up some operations
    private transient Matrix transformation = null;
    private transient Matrix result = null;


    /**
     * Creates new square matrix.
     * @param size the number of rows and columns
     */
    public Matrix(int size)
    {
        this(size, size);
    }

    /**
     * Creates new matrix.
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public Matrix(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        this.values = new float[rows][columns];
    }

    /**
     * Creates new matrix as a copy of another matrix.
     * @param other the matrix to copy
     */
    public Matrix(Matrix other)
    {
        this(other.rows, other.columns);

        for (int i = 0; i < rows; i++)
        {
            System.arraycopy(other.values[i], 0, this.values[i], 0, columns);
        }
    }

    /**
     * Returns the number of rows.
     * @return the number of rows
     */
    public int getRows()
    {
        return rows;
    }

    /**
     * Returns the number of columns.
     * @return the number of columns
     */
    public int getColumns()
    {
        return columns;
    }

    /**
     * Returns the value under given row and column.
     * @param row the row index
     * @param column the column index
     * @return the value
     */
    public float get(int row, int column)
    {
        return values[row][column];
    }

    /**
     * Changes the value under given row and column
     * @param row the row index
     * @param column the column index
     * @param value the new value
     */
    public void set(int row, int column, float value)
    {
        values[row][column] = value;
    }

    /**
     * Returns content of row.
     * @param row the row index
     * @param values the array for returned values
     */
    public void getRow(int row, float[] values)
    {
        System.arraycopy(this.values[row], 0, values, 0, columns);
    }

    /**
     * Changes content of row.
     * @param row the row index
     * @param values the array with new values
     */
    public void setRow(int row, float[] values)
    {
        System.arraycopy(values, 0, this.values[row], 0, columns);
    }

    /**
     * Returns content of column.
     * @param column the column index
     * @param values the array for returned values
     */
    public void getColumn(int column, float[] values)
    {
        for (int i = 0; i < rows; i++)
        {
            values[i] = this.values[i][column];
        }
    }

    /**
     * Changes content of column.
     * @param column the column index
     * @param values the array with new values
     */
    public void setColumn(int column, float[] values)
    {
        for (int i = 0; i < rows; i++)
        {
            this.values[i][column] = values[i];
        }
    }

    /**
     * Adds one row with another.
     * The result of this operation can be summaries as:
     * {@code row += other * multiplier}.
     *
     * @param row the row index
     * @param other the other row index
     * @param multiplier the multiplier
     */
    public void addRow(int row, int other, float multiplier)
    {
        for (int i = 0; i < columns; i++)
        {
            values[row][i] += multiplier * values[other][i];
        }
    }

    /**
     * Adds one column with another.
     * The result of this operation can be summarized as:
     * {@code column += other * multiplier}.
     * @param column the column index
     * @param other the other column index
     * @param multiplier the multiplier
     */
    public void addColumn(int column, int other, float multiplier)
    {
        for (int i = 0; i < rows; i++)
        {
            values[i][column] += multiplier * values[i][other];
        }
    }

    /**
     * Multiplies every element in the row by value.
     * @param row the row index
     * @param multiplier the multiplier
     */
    public void scaleRow(int row, float multiplier)
    {
        for (int i = 0; i < columns; i++)
        {
            values[row][i] *= multiplier;
        }
    }

    /**
     * Multiplies every element in the column by value.
     * @param column the column index
     * @param multiplier the multiplier
     */
    public void scaleColumn(int column, float multiplier)
    {
        for (int i = 0; i < rows; i++)
        {
            values[i][column] *= multiplier;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < rows; row++)
        {
            if (row > 0) builder.append('\n');

            for (int column = 0; column < columns; column++)
            {
                if (column > 0) builder.append('\t');

                builder.append(values[row][column]);
            }
        }

        return builder.toString();
    }

    /**
     * Loads this matrix with identity values.
     * @return this matrix
     */
    public Matrix loadIdentity()
    {
        loadIdentity(this);

        return this;
    }

    /**
     * Transforms this matrix by other matrix.
     * @param transform the transformation matrix
     * @return this matrix
     */
    public Matrix transform(Matrix transform)
    {
        requestResult();

        multiply(this, transform, result);
        swap(this, result);

        return this;
    }

    /**
     * Transforms this matrix by translation matrix.
     * @param dx the translation of X axis
     * @param dy the translation of Y axis
     * @param dz the translation of Z axis
     * @return this matrix
     */
    public Matrix translate(float dx, float dy, float dz)
    {
        requestTransform();

        loadTranslation(transformation, dx, dy, dz);
        transform(transformation);

        return this;
    }

    /**
     * Transforms this matrix by scale matrix.
     * @param sx the scale on X axis
     * @param sy the scale on Y axis
     * @param sz the scale on Z axis
     * @return this matrix
     */
    public Matrix scale(float sx, float sy, float sz)
    {
        requestTransform();

        loadScale(transformation, sx, sy, sz);
        transform(transformation);

        return this;
    }

    /**
     * Transforms this matrix by perspective projection matrix.
     * @param fov the field of view in degrees
     * @param aspect the aspect ratio (width / height)
     * @param near the near plane
     * @param far the far plane
     * @return this matrix
     */
    public Matrix perspective(float fov, float aspect, float near, float far)
    {
        requestTransform();

        loadPerspective(transformation, fov, aspect, near, far);
        transform(transformation);

        return this;
    }

    /**
     * Transforms this matrix by orthographic projection matrix.
     * @param left the left plane (-X)
     * @param right the right plane (+X)
     * @param bottom the bottom plane (-Y)
     * @param top the top plane (+Y)
     * @param near the near plane
     * @param far the far plane
     * @return this matrix
     */
    public Matrix ortho(float left, float right, float bottom, float top, float near, float far)
    {
        requestTransform();

        loadOrtho(transformation, left, right, bottom, top, near, far);
        transform(transformation);

        return this;
    }

    /**
     * Transforms this matrix by 2D orthographic projection matrix.
     * @param left the left plane (-X)
     * @param right the right plane (+X)
     * @param bottom the bottom plane (-Y)
     * @param top the top plane (+Y)
     * @return this matrix
     */
    public Matrix ortho2D(float left, float right, float bottom, float top)
    {
        requestTransform();

        loadOrtho2D(transformation, left, right, bottom, top);
        transform(transformation);

        return this;
    }

    /**
     * Loads this matrix from {@code ByteBuffer} object.
     * @param buffer the {@code ByteBuffer} to load matrix from
     */
    public void load(ByteBuffer buffer)
    {
        load(this, buffer);
    }

    /**
     * Loads this matrix from {@code FloatBuffer} object.
     * @param buffer the {@code FloatBuffer} to load matrix from
     */
    public void load(FloatBuffer buffer)
    {
        load(this, buffer);
    }

    /**
     * Stores this matrix in {@code ByteBuffer} object.
     * @param buffer the {@code ByteBuffer} to store matrix to
     */
    public void store(ByteBuffer buffer)
    {
        store(this, buffer);
    }

    /**
     * Stores this matrix in {@code FloatBuffer} object.
     * @param buffer the {@code FloatBuffer} to store matrix to
     */
    public void store(FloatBuffer buffer)
    {
        store(this, buffer);
    }

    /**
     * Loads this matrix with translation matrix for given values.
     * @param dx the X translation
     * @param dy the Y translation
     * @param dz the Z translation
     */
    public void loadTranslation(float dx, float dy, float dz)
    {
        loadTranslation(this, dx, dy, dz);
    }

    /**
     * Loads this matrix with rotation around X axis.
     * @param angle the angle in degrees
     */
    public void loadRotationX(float angle)
    {
        loadRotationX(this, angle);
    }

    /**
     * Loads this matrix with rotation around Y axis.
     * @param angle the angle in degrees
     */
    public void loadRotationY(float angle)
    {
        loadRotationY(this, angle);
    }

    /**
     * Loads this matrix with rotation around Z axis.
     * @param angle the angle in degrees
     */
    public void loadRotationZ(float angle)
    {
        loadRotationZ(this, angle);
    }

    /**
     * Loads this matrix with scale values.
     * @param sx the X scale
     * @param sy the Y scale
     * @param sz the Z scale
     */
    public void loadScale(float sx, float sy, float sz)
    {
        loadScale(this, sx, sy, sz);
    }

    /**
     * Loads this matrix with perspective projection values.
     * @param fov the field of view in degrees
     * @param aspect the aspect ratio (width divided by height)
     * @param near the near plane
     * @param far the far plane
     */
    public void loadPerspective(float fov, float aspect, float near, float far)
    {
        loadPerspective(this, fov, aspect, near, far);
    }

    /**
     * Loads this matrix with orthographic projection values.
     * @param left the left plane
     * @param right the right plane
     * @param bottom the bottom plane
     * @param top the top plane
     * @param near the near plane
     * @param far the far plane
     */
    public void loadOrtho(float left, float right,
            float bottom, float top, float near, float far)
    {
        loadOrtho(this, left, right, bottom, top, near, far);
    }

    /**
     * Loads this matrix with 2D orthographics projection values;
     * @param left the left plane
     * @param right the right plane
     * @param bottom the bottom plane
     * @param top the top plane
     */
    public void loadOrtho2D(float left, float right, float bottom, float top)
    {
        loadOrtho2D(this, left, right, bottom, top);
    }

    /**
     * Loads this matrix with camera view specified as look at vectors.
     * @param eyeX the eye X coordinate
     * @param eyeY the eye Y coordinate
     * @param eyeZ the eye Z coordinate
     * @param centerX the look at X coordinate
     * @param centerY the look at Y coordinate
     * @param centerZ the look at Z coordinate
     * @param upX the up X coordinate
     * @param upY the up Y coordinate
     * @param upZ the up Z coordinate
     */
    public void loadLookAt(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ)
    {
        loadLookAt(this, eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                upX, upY, upZ);
    }

    /**
     * Loads this matrix with camera view transformation.
     * @param x the X camera coordinate
     * @param y the Y camera coordinate
     * @param z the Z camera coordinate
     * @param pitch the angle of rotation around X axis in degrees
     * @param yaw the angle of rotation around Y axis in degrees
     * @param roll the angle of rotation around Z axis in degrees
     */
    public void loadCameraView(float x, float y, float z,
            float pitch, float yaw, float roll)
    {
        loadCameraView(this, x, y, z, pitch, yaw, roll);
    }

    /**
     * Computes inverse of this matrix.
     * @return this matrix
     */
    public Matrix inverse()
    {
        if (rows != columns)
            throw new IllegalStateException("Cannot invert non-square matrix");

        requestTransform();

        inverse(this, transformation);
        swap(this, transformation);

        return this;
    }

    /**
     * Transposes this matrix.
     * @return this matrix
     */
    public Matrix transpose()
    {
        requestResult();

        transpose(this, result);
        swap(this, result);

        return this;
    }

    /**
     * Requests the transformation matrix.
     */
    private void requestTransform()
    {
        if (transformation == null) transformation = new Matrix(rows, columns);
    }

    /**
     * Requests the result matrix.
     */
    private void requestResult()
    {
        if (result == null) result = new Matrix(rows, columns);
    }


    /**
     * Loads matrix from {@code ByteBuffer} object.
     * @param matrix the matrix to be loaded with values
     * @param buffer the {@code ByteBuffer} to load matrix from
     */
    public static void load(Matrix matrix, ByteBuffer buffer)
    {
        int rows = matrix.getRows();
        int columns = matrix.getColumns();
        int index = 0;

        for (int column = 0; column < columns; column++)
        {
            for (int row = 0; row < rows; row++)
            {
                matrix.values[row][column] = buffer.getFloat(index);
                index += 4;
            }
        }
    }

    /**
     * Loads matrix from {@code FloatBuffer} object.
     * @param matrix the matrix to be loaded with values
     * @param buffer the {@code FloatBuffer} to load matrix from
     */
    public static void load(Matrix matrix, FloatBuffer buffer)
    {
        int rows = matrix.getRows();
        int columns = matrix.getColumns();
        int index = 0;

        for (int column = 0; column < columns; column++)
        {
            for (int row = 0; row < rows; row++)
            {
                matrix.values[row][column] = buffer.get(index);
                index++;
            }
        }
    }

    /**
     * Stores matrix to {@code ByteBuffer} object.
     * @param matrix the matrix with values to be stored
     * @param buffer the {@code ByteBuffer} to store matrix to
     */
    public static void store(Matrix matrix, ByteBuffer buffer)
    {
        int rows = matrix.getRows();
        int columns = matrix.getColumns();

        buffer.clear();

        for (int column = 0; column < columns; column++)
        {
            for (int row = 0; row < rows; row++)
            {
                buffer.putFloat(matrix.values[row][column]);
            }
        }

        buffer.flip();
    }

    /**
     * Stores matrix to {@code FloatBuffer} object.
     * @param matrix the matrix with values to be stored
     * @param buffer the {@code FloatBuffer} to store matrix to
     */
    public static void store(Matrix matrix, FloatBuffer buffer)
    {
        int rows = matrix.getRows();
        int columns = matrix.getColumns();

        buffer.clear();

        for (int column = 0; column < columns; column++)
        {
            for (int row = 0; row < rows; row++)
            {
                buffer.put(matrix.values[row][column]);
            }
        }

        buffer.flip();
    }

    /**
     * Loads matrix with identity values.
     * @param matrix the matrix to be loaded with values
     */
    public static void loadIdentity(Matrix matrix)
    {
        int rows = matrix.getRows();
        int columns = matrix.getColumns();

        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                matrix.values[row][column] = (row == column ? 1.0f : 0.0f);
            }
        }
    }

    /**
     * Loads matrix with translation values.
     * @param matrix the matrix to load values with
     * @param dx the translation on X axis
     * @param dy the translation on Y axis
     * @param dz the translation on Z axis
     */
    public static void loadTranslation(Matrix matrix,
            float dx, float dy, float dz)
    {
        loadIdentity(matrix);

        matrix.values[0][3] += dx;
        matrix.values[1][3] += dy;
        matrix.values[2][3] += dz;
    }

    /**
     * Loads matrix with scale values.
     * @param matrix the matrix to be loaded with values
     * @param sx the X scale
     * @param sy the Y scale
     * @param sz the Z scale
     */
    public static void loadScale(Matrix matrix, float sx, float sy, float sz)
    {
        loadIdentity(matrix);

        matrix.values[0][0] = sx;
        matrix.values[1][1] = sy;
        matrix.values[2][2] = sz;
    }

    /**
     * Loads matrix with perspective projection values.
     * @param matrix the matrix to be loaded with values
     * @param fov the field of view in degrees
     * @param aspect the aspect ratio (width divided by height)
     * @param near the near plane
     * @param far the far plane
     */
    public static void loadPerspective(Matrix matrix,
            float fov, float aspect, float near, float far)
    {
        loadIdentity(matrix);

        float focal = (float) (1.0f / Math.tan(0.5f * Math.toRadians(fov)));

        matrix.values[0][0] = focal / aspect;
        matrix.values[1][1] = focal;
        matrix.values[2][2] = -(far + near) / (far - near);
        matrix.values[2][3] = -2.0f * far * near / (far - near);
        matrix.values[3][2] = -1.0f;
        matrix.values[3][3] = 0.0f;
    }

    /**
     * Loads matrix with orthographic projection values.
     * @param matrix the matrix to be loaded with values
     * @param left the left plane
     * @param right the right plane
     * @param bottom the bottom plane
     * @param top the top plane
     * @param near the near plane
     * @param far the far plane
     */
    public static void loadOrtho(Matrix matrix, float left, float right,
            float bottom, float top, float near, float far)
    {
        loadIdentity(matrix);

        matrix.values[0][0] = 2.0f / (right - left);
        matrix.values[1][1] = 2.0f / (top - bottom);
        matrix.values[2][2] = -2.0f / (far - near);

        matrix.values[0][3] = -(right + left) / (right - left);
        matrix.values[1][3] = -(top + bottom) / (top - bottom);
        matrix.values[2][3] = -(far + near) / (far - near);
    }

    /**
     * Loads matrix with 2D orthographics projection values.
     * @param matrix the matrix to be loaded with values
     * @param left the left plane
     * @param right the right plane
     * @param bottom the bottom plane
     * @param top the top plane
     */
    public static void loadOrtho2D(Matrix matrix, float left, float right,
            float bottom, float top)
    {
        loadOrtho(matrix, left, right, bottom, top, -1.0f, 1.0f);
    }

    /**
     * Loads matrix with camera view using glLookAt parameters.
     * @param matrix the matrix to load with values
     * @param eyeX the eye X coordinate
     * @param eyeY the eye Y coordinate
     * @param eyeZ the eye Z coordinate
     * @param centerX the look at X coordinate
     * @param centerY the look at Y coordinate
     * @param centerZ the look at Z coordinate
     * @param upX the up vector X coordinate
     * @param upY the up vector Y coordinate
     * @param upZ the up vector Z coordinate
     */
    public static void loadLookAt(Matrix matrix,
            float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ)
    {
        // calculate forward vector
        // normalized vector from Eye position to At position
        float forwardX = centerX - eyeX;
        float forwardY = centerY - eyeY;
        float forwardZ = centerZ - eyeZ;

        float inv = 1.0f / length(forwardX, forwardY, forwardZ);

        forwardX *= inv;
        forwardY *= inv;
        forwardZ *= inv;

        // calculate right vector
        // normalized cross product of Up and Forward vectors
        float sideX = (upY * forwardZ - upZ * forwardY);
        float sideY = (upZ * forwardX - upX * forwardZ);
        float sideZ = (upX * forwardY - upY * forwardX);

        inv = 1.0f / length(sideX, sideY, sideZ);

        sideX *= inv;
        sideY *= inv;
        sideZ *= inv;

        // recalculate up vector
        upX = (forwardY * sideZ - forwardZ * sideY);
        upY = (forwardZ * sideX - forwardX * sideZ);
        upZ = (forwardX * sideY - forwardY * sideZ);

        // set matrix values
        matrix.set(0, 0, sideX);
        matrix.set(1, 0, sideY);
        matrix.set(2, 0, sideZ);
        matrix.set(3, 0, 0.0f);

        matrix.set(0, 1, upX);
        matrix.set(1, 1, upY);
        matrix.set(2, 1, upZ);
        matrix.set(3, 1, 0.0f);

        matrix.set(0, 2, forwardX);
        matrix.set(1, 2, forwardY);
        matrix.set(2, 2, forwardZ);
        matrix.set(3, 2, 0.0f);

        matrix.set(0, 3, 0.0f);
        matrix.set(1, 3, 0.0f);
        matrix.set(2, 3, 0.0f);
        matrix.set(3, 3, 1.0f);

        matrix.translate(-eyeX, -eyeY, -eyeZ);
    }

    private static float dot(float x1, float y1, float z1,
            float x2, float y2, float z2)
    {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    private static float length(float dx, float dy, float dz)
    {
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Loads matrix with camera view transformation.
     * @param matrix the matrix to be loaded with values
     * @param x the X camera coordinate
     * @param y the Y camera coordinate
     * @param z the Z camera coordinate
     * @param pitch the angle of rotation around X axis in degrees
     * @param yaw the angle of rotation around Y axis in degrees
     * @param roll the angle of rotation around Z axis in degrees
     */
    public static void loadCameraView(Matrix matrix, float x, float y, float z,
            float pitch, float yaw, float roll)
    {
        Matrix transformation = new Matrix(4);

        matrix.loadIdentity();

        transformation.loadRotationZ(roll);
        matrix.transform(transformation);

        transformation.loadRotationX(pitch);
        matrix.transform(transformation);

        transformation.loadRotationY(yaw);
        matrix.transform(transformation);

        transformation.loadTranslation(-x, -y, -z);
        matrix.transform(transformation);
    }

    /**
     * Loads rotation matrix around X axis.
     * @param matrix the matrix to be loaded with rotation values
     * @param angle the rotation angle in degrees
     */
    public static void loadRotationX(Matrix matrix, float angle)
    {
        double radians = Math.toRadians(angle);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        loadIdentity(matrix);

        matrix.values[1][1] = cos;
        matrix.values[1][2] = -sin;
        matrix.values[2][1] = sin;
        matrix.values[2][2] = cos;
    }

    /**
     * Loads rotation matrix around Y axis.
     * @param matrix the matrix to be loaded with rotation values
     * @param angle the rotation angle in degrees
     */
    public static void loadRotationY(Matrix matrix, float angle)
    {
        double radians = Math.toRadians(angle);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        loadIdentity(matrix);

        matrix.values[0][0] = cos;
        matrix.values[0][2] = sin;
        matrix.values[2][0] = -sin;
        matrix.values[2][2] = cos;
    }

    /**
     * Loads rotation matrix around Z axis.
     * @param matrix the matrix to be loaded with rotation values
     * @param angle the rotation angle in degrees
     */
    public static void loadRotationZ(Matrix matrix, float angle)
    {
        double radians = Math.toRadians(angle);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        loadIdentity(matrix);

        matrix.values[0][0] = cos;
        matrix.values[0][1] = -sin;
        matrix.values[1][0] = sin;
        matrix.values[1][1] = cos;
    }

    /**
     * Adds two matrices and stores result in third one.
     * @param first the first matrix to add
     * @param second the second matrix to add
     * @param result the matrix for result
     */
    public static void add(Matrix first, Matrix second, Matrix result)
    {
        checkCompatibility(first, second);
        checkCompatibility(first, result);

        int rows = first.getRows();
        int columns = first.getColumns();

        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                float x = first.values[row][column];
                float y = second.values[row][column];

                result.values[row][column] = x + y;
            }
        }
    }

    /**
     * Multiplies two matrices and stores result in other matrix.
     * @param first the first matrix to multiply
     * @param second the second matrix to multiply
     * @param result the matrix where multiplication result is to be stored
     */
    public static void multiply(Matrix first, Matrix second, Matrix result)
    {
        if(first.getColumns() != result.getColumns())
            throw new IllegalArgumentException("Incompatible matrices");

        if(first.getColumns() != second.getRows())
            throw new IllegalArgumentException("Incompatible matrices");

        int rows = first.getRows();
        int columns = second.getColumns();
        int depth = first.getColumns();

        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                float sum = 0.0f;

                for (int k = 0; k < depth; k++)
                    sum += first.values[row][k] * second.values[k][column];

                result.values[row][column] = sum;
            }
        }
    }

    /**
     * Multiplies vector by a matrix.
     * @param matrix the matrix to multiply
     * @param vector the vector to multiply
     * @return the result of multiplication
     */
    public static Vector multiply(Matrix matrix, Vector vector)
    {
        Vector result = new Vector(matrix.getRows());

        multiply(matrix, vector, result);

        return result;
    }

    /**
     * Multiplies vector by a matrix.
     * @param matrix the matrix to multiply
     * @param vector the vector to multiply
     * @param result the result of multiplication
     */
    public static void multiply(Matrix matrix, Vector vector, Vector result)
    {
        multiply(matrix, vector.values, result.values);
    }

    /**
     * Multiplies vector by a matrix.
     * @param matrix the matrix to multiply
     * @param vector the vector to multiply
     * @param result the result of multiplication
     */
    public static void multiply(Matrix matrix, float[] vector, float[] result)
    {
        int rows = matrix.getRows();
        int columns = matrix.getColumns();

        if (vector.length < columns)
            throw new IllegalArgumentException("Source vector too short");

        if (result.length < rows)
            throw new IllegalArgumentException("Destination vector too short");

        for (int row = 0; row < rows; row++)
        {
            float sum = 0.0f;

            for (int column = 0; column < columns; column++)
            {
                sum += matrix.get(row, column) * vector[column];
            }

            result[row] = sum;
        }
    }

    /**
     * Copies one matrix to another.
     * @param src the source matrix
     * @param dest the destination matrix
     */
    public static void copy(Matrix src, Matrix dest)
    {
        checkCompatibility(src, dest);

        int rows = src.getRows();
        int columns = src.getColumns();

        for (int row = 0; row < rows; row++)
        {
            System.arraycopy(src.values[row], 0, dest.values[row], 0, columns);
        }
    }

    /**
     * Copies available region from one matrix to another.
     * @param src the source matrix
     * @param dest the destination matrix
     */
    public static void subCopy(Matrix src, Matrix dest)
    {
        if (dest.rows > src.rows) throw new IllegalArgumentException("Incompatible matrices");
        if (dest.columns > src.columns) throw new IllegalArgumentException("Incompatible matrices");

        int rows = src.getRows();
        int columns = src.getColumns();

        for (int row = 0; row < rows; row++)
        {
            System.arraycopy(src.values[row], 0, dest.values[row], 0, columns);
        }
    }

    /**
     * Swaps content of two matrices.
     * @param first the first matrix
     * @param second the second matrix
     */
    public static void swap(Matrix first, Matrix second)
    {
        checkCompatibility(first, second);

        float[][] temp = first.values;
        first.values = second.values;
        second.values = temp;
    }

    /**
     * Computes transpose of given matrix and stores it in other matrix.
     * @param src the source matrix
     * @param dest the destination matrix
     */
    public static void transpose(Matrix src, Matrix dest)
    {
        if (src.rows != dest.columns)
            throw new IllegalArgumentException("Incompatible matrices");

        if (src.columns != dest.rows)
            throw new IllegalArgumentException("Incompatible matrices");

        int rows = src.rows;
        int columns = src.columns;

        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                dest.values[column][row] = src.values[row][column];
            }
        }
    }

    /**
     * Computes inverse of given matrix and stores it in other matrix.
     * @param src the source matrix
     * @param dest the destination matrix
     */
    public static void inverse(Matrix src, Matrix dest)
    {
        checkCompatibility(src, dest);

        // create a working copy
        src.requestResult();
        copy(src, src.result);

        // load destination with identity matrix
        dest.loadIdentity();

        // perform Gaussian elimination
        gaussianElimination(src.result, dest);
    }

    /**
     * Computes inverse of 3x3 matrix.
     * This code is left in case it is needed.
     * @param src the source matrix
     * @param dest the destination matrix
     */
    private static void inverse3(Matrix src, Matrix dest)
    {
        // shortcut for source values
        final float m[][] = src.values;

        // determinant
        float det = m[0][0] * m[1][1] * m[2][2]
                + m[1][0] * m[2][1] * m[0][2]
                + m[2][0] * m[0][1] * m[1][2]
                - m[0][0] * m[2][1] * m[1][2]
                - m[1][0] * m[0][1] * m[2][2]
                - m[2][0] * m[1][1] * m[0][2];

        if(Math.abs(det) < 1e-6f)
            throw new IllegalArgumentException(
                    "Matrix cannot be inverted: determinant is 0");

        float detInv = 1.0f / det;

        float m00 =  (m[1][1] * m[2][2] - m[2][1] * m[1][2]);
        float m01 = -(m[1][0] * m[2][2] - m[2][0] * m[1][2]);
        float m02 =  (m[1][0] * m[2][1] - m[2][0] * m[1][1]);
        float m10 = -(m[0][1] * m[2][2] - m[2][1] * m[0][2]);
        float m11 =  (m[0][0] * m[2][2] - m[2][0] * m[0][2]);
        float m12 = -(m[0][0] * m[2][1] - m[2][0] * m[0][1]);
        float m20 =  (m[0][1] * m[1][2] - m[1][1] * m[0][2]);
        float m21 = -(m[0][0] * m[1][2] - m[1][0] * m[0][2]);
        float m22 =  (m[0][0] * m[1][1] - m[1][0] * m[0][1]);

        dest.values[0][0] = m00 * detInv;
        dest.values[0][1] = m01 * detInv;
        dest.values[0][2] = m02 * detInv;
        dest.values[1][0] = m10 * detInv;
        dest.values[1][1] = m11 * detInv;
        dest.values[1][2] = m12 * detInv;
        dest.values[2][0] = m20 * detInv;
        dest.values[2][1] = m21 * detInv;
        dest.values[2][2] = m22 * detInv;
    }

    /**
     * Performs Gaussian Elimination on two matrices.
     * This method can be used to calculate matrix inverse. Simply load
     * {@code second} with identity matrix.
     * @param first the first matrix
     * @param second the second matrix
     */
    public static void gaussianElimination(Matrix first, Matrix second)
    {
        checkCompatibility(first, second);

        int rows = first.getRows();

        // converting first matrix to row echelon form
        for (int diagonal = 0; diagonal < rows; diagonal++)
        {
            float value = first.get(diagonal, diagonal);

            // if first element is zero, find non-zero row
            // and add to this one while scaling to 1
            if (Math.abs(value) < 1e-6f)
            {
                int row = diagonal + 1;

                for (; row < rows; row++)
                {
                    if (Math.abs(first.get(row, diagonal)) > 1e-6f)
                        break;
                }

                if (row == rows)
                {
                    throw new RuntimeException(
                            "Matrix seems to be invalid or non-invertible");
                }

                // this will rescale first value to 1
                value = 1.0f / first.get(row, diagonal);

                first.addRow(diagonal, row, value);
                second.addRow(diagonal, row, value);
            }
            // else scale rows by element on diagonal to get 1
            else
            {
                // this will rescale first value to 1
                float scale = 1.0f / first.get(diagonal, diagonal);

                first.scaleRow(diagonal, scale);
                second.scaleRow(diagonal, scale);
            }

            // clear columns below the diagonal by adding other rows
            for (int row = diagonal + 1; row < rows; row++)
            {
                // this will result in first value being 0
                value = first.get(row, diagonal);

                first.addRow(row, diagonal, -value);
                second.addRow(row, diagonal, -value);
            }
        }

        // clearing upper part of matrix
        for (int diagonal = rows - 1; diagonal >= 0; diagonal--)
        {
            // going through upper rows
            for (int row = 0; row < diagonal; row++)
            {
                float value = first.get(row, diagonal);

                first.addRow(row, diagonal, -value);
                second.addRow(row, diagonal, -value);
            }
        }
    }

    /**
     * Checks matrix compatibility. Throws {@code IllegalArgumentException}
     * if matrices don't have equal number of rows and columns.
     * @param first the first matrix to check
     * @param second the second matrix to check
     */
    private static void checkCompatibility(Matrix first, Matrix second)
    {
        if (first.getRows() != second.getRows())
            throw new IllegalArgumentException(
                    "Incompatible matrices: different row count");

        if (first.getColumns() != second.getColumns())
            throw new IllegalArgumentException(
                    "Incompatible matrices: different column count");
    }
}
