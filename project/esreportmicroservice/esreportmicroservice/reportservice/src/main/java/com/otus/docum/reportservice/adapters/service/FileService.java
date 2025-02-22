package com.otus.docum.reportservice.adapters.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Lazy
@Service
public class FileService {

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
            log.info("FILE PATH  :" + Paths.get(new String(getClass().getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .toURI()
            .getPath() + fileName).substring(1)).toString());
 
            
            return Paths.get(stream2file(getFileFromResourceAsStream(fileName)).getPath());

        } catch (Exception e)
        {
            log.error("getFileURIFromResources: " + e.getMessage().toString());
            throw new RuntimeException(e);
        }
	}
    public InputStream getResourceAsStream(String fileName) throws IOException {
        return new ClassPathResource(fileName).getInputStream();        
    }

    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile("employees-details",".jrxml");
        tempFile.deleteOnExit();
        try(FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

}
