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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents vertex groups with the same material properties.
 * @author Tomasz Kapuściński
 */
public final class VertexGroup implements Iterable<Vertex>
{
    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final Material material;

    /**
     * Creates new vertex group for given material.
     * @param material vertex group material
     */
    public VertexGroup(Material material)
    {
        this.material = material;
    }

    /**
     * Returns number of vertices in this group.
     * @return the number of vertices
     */
    public int size()
    {
        return vertices.size();
    }

    /**
     * Returns vertex in given index.
     * @param index index
     * @return the vertex
     */
    public Vertex get(int index)
    {
        return vertices.get(index);
    }

    /**
     * Returns material assigned to this vertex group.
     * @return this vertex group's material
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * Adds vertex to this group.
     * @param vertex the vertex to add
     */
    public void add(Vertex vertex)
    {
        if (vertex.getMaterial() != material)
            throw new IllegalArgumentException("Incompatible material");

        vertices.add(vertex);
    }

    @Override
    public Iterator<Vertex> iterator()
    {
        return vertices.iterator();
    }
}
