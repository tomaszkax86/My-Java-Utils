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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ServiceLoader;

/**
 * This class represents model loaders.
 * @author Tomasz Kapuściński
 */
public abstract class ModelLoader
{
    /**
     * Creates new model loader.
     */
    protected ModelLoader() { }

    /**
     * Returns format name of this model loader.
     * @return the name
     */
    public abstract String getName();

    /**
     * Returns description of this model loader.
     * @return the description
     */
    public abstract String getDescription();

    /**
     * Returns file name extension of this model loader.
     * @return the extension
     */
    public abstract String getExtension();

    /**
     * Loads model from {@link java.io.InputStream}.
     * @param input input stream
     * @return the model
     * @throws IOException when I/O error occurs
     */
    public abstract Model load(InputStream input) throws IOException;

    /**
     * Loads model from {@link java.io.File}.
     * @param file file
     * @return the model
     * @throws IOException when I/O error occurs
     */
    public Model load(File file) throws IOException
    {
        try (FileInputStream input = new FileInputStream(file))
        {
            return load(input);
        }
    }

    /**
     * Loads model from {@link java.net.URL}.
     * @param url URL
     * @return the model
     * @throws IOException when I/O error occurs
     */
    public Model load(URL url) throws IOException
    {
        try (InputStream input = url.openStream())
        {
            return load(input);
        }
    }

    /**
     * Loads model from {@link java.io.InputStream}.
     * @param input input
     * @param name format name
     * @return
     * @throws IOException
     */
    public static Model load(InputStream input, String name)
            throws IOException
    {
        ModelLoader loader = getInstance(name);

        return loader.load(input);
    }

    /**
     * Loads model from {@link java.net.URL}.
     * @param url URL
     * @param name format name
     * @return the model
     * @throws IOException when I/O error occurs
     */
    public static Model load(URL url, String name)
            throws IOException
    {
        ModelLoader loader = getInstance(name);

        return loader.load(url);
    }

    /**
     * Loads model from {@link java.io.File}.
     * @param file file
     * @param name format name
     * @return the model
     * @throws IOException when I/O error occurs
     */
    public static Model load(File file, String name) throws IOException
    {
        if (name == null)
        {
            String filename = file.getName();
            int index = filename.lastIndexOf('.');
            if (index == -1)
            {
                throw new IllegalArgumentException("No exception");
            }

            String extension = filename.substring(index + 1);

            ModelLoader loader = getInstanceByExtension(extension);

            return loader.load(file);
        }
        else
        {
            ModelLoader loader = getInstance(name);

            return loader.load(file);
        }
    }

    /**
     * Returns instance of model loader for given format name.
     * @param name format name
     * @return the model loader
     */
    public static ModelLoader getInstance(String name)
    {
        ServiceLoader<ModelLoader> service
                = ServiceLoader.load(ModelLoader.class);

        for (ModelLoader loader : service)
        {
            if (loader.getName().equalsIgnoreCase(name))
            {
                return loader;
            }
        }

        throw new IllegalArgumentException("No such loader: " + name);
    }

    /**
     * Returns instance of model loader for given file name extension.
     * @param extension file name extension
     * @return the model loader
     */
    public static ModelLoader getInstanceByExtension(String extension)
    {
        ServiceLoader<ModelLoader> service
                = ServiceLoader.load(ModelLoader.class);

        for (ModelLoader loader : service)
        {
            if (loader.getExtension().equalsIgnoreCase(extension))
            {
                return loader;
            }
        }

        throw new IllegalArgumentException("Unknown file name extension: "
                + extension);
    }
}
