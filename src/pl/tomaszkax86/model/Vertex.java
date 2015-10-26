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
 * This class represents a vertex with 3D coordinates, UV texture coordinates,
 * normal and assigned material.
 * @author Tomasz Kapuściński
 */
public final class Vertex
{
    private final float x, y, z;
    private final float nx, ny, nz;
    private final float u, v;
    private final Material material;

    /**
     * Creates new vertex.
     * @param x X vertex coordinate
     * @param y Y vertex coordinate
     * @param z Z vertex coordinate
     * @param u U texture coordinate
     * @param v V texture coordinate
     * @param nx X normal coordinate
     * @param ny Y normal coordinate
     * @param nz Z normal coordinate
     * @param material material
     */
    public Vertex(float x, float y, float z,
            float u, float v,
            float nx, float ny, float nz,
            Material material)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.material = material;
    }


    /**
     * Creates new vertex.
     * @param vc vertex coordinate
     * @param tc texture coordinate
     * @param n normal
     * @param material material
     */
    public Vertex(VertexCoord vc, TextureCoord tc, Normal n, Material material)
    {
        if(vc == null) throw new NullPointerException();

        this.x = vc.getX();
        this.y = vc.getY();
        this.z = vc.getZ();

        if(tc != null)
        {
            this.u = tc.getU();
            this.v = tc.getV();
        }
        else
        {
            this.u = 0.0f;
            this.v = 0.0f;
        }

        if(n != null)
        {
            this.nx = n.getX();
            this.ny = n.getY();
            this.nz = n.getZ();
        }
        else
        {
            this.nx = 0.0f;
            this.ny = 1.0f;
            this.nz = 0.0f;
        }

        this.material = material;
    }

    /**
     * Returns X coordinate.
     * @return the X coordinate
     */
    public float getX()
    {
        return x;
    }

    /**
     * Returns Y coordinate.
     * @return the Y coordinate
     */
    public float getY()
    {
        return y;
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
     * Returns X normal coordinate.
     * @return the X normal coordinate
     */
    public float getNX()
    {
        return nx;
    }

    /**
     * Returns Y normal coordinate.
     * @return the Y normal coordinate
     */
    public float getNY()
    {
        return ny;
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
     * Returns U texture coordinate.
     * @return the U texture coordinate
     */
    public float getU()
    {
        return u;
    }

    /**
     * Returns V texture coordinate.
     * @return the V texture coordinate
     */
    public float getV()
    {
        return v;
    }

    /**
     * Returns material.
     * @return the material
     */
    public Material getMaterial()
    {
        return material;
    }
}
