package com.otus.docum.reportservice.adapters.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.otus.docum.reportservice.application.exception.ResourceLoadException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Lazy
@Service
public class FileService implements AutoCloseable {

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    
    public Path getFileURIFromResources(String fileName) {
        try {
            
            return Paths.get(stream2file(getFileFromResourceAsStream(fileName)).getPath());

        } catch (Exception ex)
        {
            log.error("getFileURIFromResources: " + ex.getMessage());
            throw new ResourceLoadException(fileName, ex);
        }
    }

    public InputStream getResourceAsStream(String fileName) throws IOException {
        return new ClassPathResource(fileName).getInputStream();        
    }

    public static File stream2file (InputStream in) throws IOException {
        File tempFile = Files.createTempFile("employees-details",".jrxml").toFile();
        try(FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(in, out);
                return tempFile;
            } catch (Exception ex) {
                tempFile.delete();
                log.error("Create temp file error ", ex);
                throw new ResourceLoadException(tempFile.getName(), ex);
            }
            finally {
                tempFile.deleteOnExit();
            }
    }

    @Override
    public void close() throws Exception {

        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

}
