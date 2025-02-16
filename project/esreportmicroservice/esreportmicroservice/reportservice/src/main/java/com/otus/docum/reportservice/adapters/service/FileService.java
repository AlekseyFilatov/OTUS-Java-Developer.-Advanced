package com.otus.docum.reportservice.adapters.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Lazy
@Service
public class FileService {

        public Path getFileURIFromResources(String fileName) {
        try {
        
            return Paths.get(new String(getClass().getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath() + fileName).substring(1));

        } catch (Exception e)
        {
            log.error("getFileURIFromResources: " + e.getMessage().toString());
            throw new RuntimeException(e);
        }
	}

}
