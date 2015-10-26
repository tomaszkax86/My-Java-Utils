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
 * This is a simple 3D model implementation.
 * Vertices are contained in {@link VertexGroup} objects.
 * @author Tomasz Kapuściński
 */
public final class Model implements Iterable<VertexGroup>
{
    private final ArrayList<VertexGroup> groups = new ArrayList<>();


    /**
     * Returns number of vertices in this model.
     * @return the number of vertices
     */
    public int size()
    {
        return groups.size();
    }

    /**
     * Returns vertex under given index.
     * @param index index
     * @return the vertex
     */
    public VertexGroup get(int index)
    {
        return groups.get(index);
    }

    /**
     * Adds vertex to this model.
     * Vertex will be added to a vertex group with compatible material
     * or new vertex group if no such vertex group existed.
     * @param vertex new vertex
     */
    public void add(Vertex vertex)
    {
        // search for compatible vertex group
        for (VertexGroup group : groups)
        {
            if (group.getMaterial() == vertex.getMaterial())
            {
                group.add(vertex);
                return;
            }
        }

        // create new vertex group
        VertexGroup group = new VertexGroup(vertex.getMaterial());
        group.add(vertex);
        groups.add(group);
    }

    @Override
    public Iterator<VertexGroup> iterator()
    {
        return groups.iterator();
    }
}
