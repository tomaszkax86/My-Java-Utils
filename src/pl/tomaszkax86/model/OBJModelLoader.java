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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements loading models from Wavefront OBJ files.
 * @author Tomasz Kapuściński
 */
public final class OBJModelLoader extends ModelLoader
{
    @Override
    public String getName()
    {
        return "Wavefront OBJ";
    }

    @Override
    public String getDescription()
    {
        return "Simple Wavefront OBJ model loader";
    }

    @Override
    public String getExtension()
    {
        return "obj";
    }

    @Override
    public Model load(InputStream input) throws IOException
    {
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(input)))
        {
            ArrayList<VertexCoord> vertexcoords = new ArrayList<>();
            ArrayList<Normal> normals = new ArrayList<>();
            ArrayList<TextureCoord> texcoords = new ArrayList<>();

            Model model = new Model();

            HashMap<String, Material> materials = new HashMap<>();
            Material material = null;

            while(true)
            {
                String line = reader.readLine();
                if(line == null) break;

                String[] parts = line.split(" ");

                switch(parts[0])
                {
                case "v":
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    vertexcoords.add(new VertexCoord(x, y, z));
                    break;

                case "vt":
                    float u = Float.parseFloat(parts[1]);
                    float v = Float.parseFloat(parts[2]);
                    texcoords.add(new TextureCoord(u, v));
                    break;

                case "vn":
                    float nx = Float.parseFloat(parts[1]);
                    float ny = Float.parseFloat(parts[2]);
                    float nz = Float.parseFloat(parts[3]);
                    normals.add(new Normal(nx, ny, nz));
                    break;

                case "mtllib":
                    loadMaterials(materials, new File(parts[1]));
                    break;

                case "usemtl":
                    material = materials.get(parts[1]);
                    break;

                case "f":
                    Vertex[] verts = new Vertex[parts.length - 1];

                    // attribute parsing
                    for(int i=0; i<verts.length; i++)
                    {
                        String[] temp = parts[i+1].split("/");

                        int vc = Integer.parseInt(temp[0]) - 1;
                        int tc = Integer.parseInt(temp[1]) - 1;
                        int n = Integer.parseInt(temp[2]) - 1;

                        verts[i] = new Vertex(vertexcoords.get(vc),
                                texcoords.get(tc),
                                normals.get(n),
                                material);
                    }

                    // triangulation
                    int count = verts.length - 2;

                    for(int i=0; i<count; i++)
                    {
                        model.add(verts[0]);
                        model.add(verts[1+i]);
                        model.add(verts[2+i]);
                    }

                    break;
                }
            }

            return model;
        }
    }

    private void loadMaterials(HashMap<String, Material> materials, File file)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            Material current = null;

            while(true)
            {
                String line = reader.readLine();
                if(line == null) break;

                String[] parts = line.split(" ");

                switch(parts[0])
                {
                case "newmtl":
                    current = new Material(parts[1], null);
                    materials.put(parts[1], current);
                    break;

                case "map_Kd":
                    current = new Material(current.getName(), parts[1]);
                    materials.put(current.getName(), current);
                    break;
                }
            }
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
