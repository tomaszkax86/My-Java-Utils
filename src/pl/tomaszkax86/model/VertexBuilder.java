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
package pl.tomaszkax86.model;

/**
 * This class represents vertex builder.
 * @author Tomasz Kapuściński
 */
public final class VertexBuilder
{
    private float x, y, z;
    private float nx, ny, nz;
    private float u, v;
    private Material material;

    /**
     * Creates new vertex builder.
     */
    public VertexBuilder() { }

    /**
     * Returns X coordinate.
     * @return the X coordinate
     */
    public float getX()
    {
        return x;
    }

    /**
     * Sets X coordinate.
     * @param x the X coordinate to set
     * @return this builder
     */
    public VertexBuilder setX(float x)
    {
        this.x = x;

        return this;
    }

    /**
     * Returns Y coordinate.
     * @return the y
     */
    public float getY()
    {
        return y;
    }

    /**
     * Sets Y coordinate.
     * @param y the Y coordinate to set
     * @return this builder
     */
    public VertexBuilder setY(float y)
    {
        this.y = y;

        return this;
    }

    /**
     * Returns Z coordinate.
     * @return the Z coordinate
     */
    public float getZ()
    {
        return z;
    }

    /**
     * Sets Z coordinate.
     * @param z the Z coordinate to set
     * @return this builder
     */
    public VertexBuilder setZ(float z)
    {
        this.z = z;

        return this;
    }

    /**
     * Returns X normal coordinate.
     * @return the X normal coordinate
     */
    public float getNX()
    {
        return nx;
    }

    /**
     * Sets X normal coordinate.
     * @param nx the X normal coordinate to set
     * @return this builder
     */
    public VertexBuilder setNX(float nx)
    {
        this.nx = nx;

        return this;
    }

    /**
     * Returns Y normal coordinate.
     * @return the ny
     */
    public float getNY()
    {
        return ny;
    }

    /**
     * Sets Y normal coordinate.
     * @param ny the Y normal coordinate to set
     * @return this builder
     */
    public VertexBuilder setNY(float ny)
    {
        this.ny = ny;

        return this;
    }

    /**
     * Returns Z normal coordinate.
     * @return the Z normal coordinate
     */
    public float getNZ()
    {
        return nz;
    }

    /**
     * Sets Z normal coordinate.
     * @param nz the Z normal coordinate to set
     * @return this builder
     */
    public VertexBuilder setNZ(float nz)
    {
        this.nz = nz;

        return this;
    }

    /**
     * Returns U texture coordinate.
     * @return the U texture coordinate
     */
    public float getU()
    {
        return u;
    }

    /**
     * Sets U texture coordinate.
     * @param u the U coordinate to set
     * @return this builder
     */
    public VertexBuilder setU(float u)
    {
        this.u = u;

        return this;
    }

    /**
     * Returns U texture coordinate.
     * @return the U coordinate
     */
    public float getV()
    {
        return v;
    }

    /**
     * Sets U texture coordinate.
     * @param v the U coordinate to set
     * @return this builder
     */
    public VertexBuilder setV(float v)
    {
        this.v = v;

        return this;
    }

    /**
     * Returns material.
     * @return the material
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * Sets material.
     * @param material the material to set
     * @return this builder
     */
    public VertexBuilder setMaterial(Material material)
    {
        this.material = material;

        return this;
    }

    /**
     * Sets vertex coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @return this builder
     */
    public VertexBuilder setVertexCoord(float x, float y)
    {
        setVertexCoord(x, y, 0.0f);

        return this;
    }

    /**
     * Sets vertex coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return this builder
     */
    public VertexBuilder setVertexCoord(float x, float y, float z)
    {
        setX(x);
        setY(y);
        setZ(z);

        return this;
    }

    /**
     * Sets vertex coordinates.
     * @param vertex vertex coordinates
     * @return this builder
     */
    public VertexBuilder setVertexCoord(VertexCoord vertex)
    {
        setVertexCoord(vertex.getX(), vertex.getY(), vertex.getZ());

        return this;
    }

    /**
     * Sets texture coordinates.
     * @param u U coordinate
     * @param v V coordinate
     * @return this builder
     */
    public VertexBuilder setTextureCoord(float u, float v)
    {
        setU(u);
        setV(v);

        return this;
    }

    /**
     * Sets texture coordinates.
     * @param texture texture coordinates
     * @return this builder
     */
    public VertexBuilder setTextureCoord(TextureCoord texture)
    {
        setTextureCoord(texture.getU(), texture.getV());

        return this;
    }

    /**
     * Sets normal coordinates.
     * @param nx X coordinate
     * @param ny Y coordinate
     * @param nz Z coordinate
     * @return this builder
     */
    public VertexBuilder setNormal(float nx, float ny, float nz)
    {
        setNX(nx);
        setNY(ny);
        setNZ(nz);

        return this;
    }

    /**
     * Sets normal coordinates.
     * @param normal normal coordinates to use
     * @return this builder
     */
    public VertexBuilder setNormal(Normal normal)
    {
        setNormal(normal.getX(), normal.getY(), normal.getZ());

        return this;
    }

    /**
     * Creates new vertex from this builder.
     * @return new vertex
     */
    public Vertex toVertex()
    {
        return new Vertex(x, y, z, u, v, nx, ny, nz, material);
    }
}
