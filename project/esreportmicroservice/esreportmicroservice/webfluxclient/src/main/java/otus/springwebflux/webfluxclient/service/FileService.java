package otus.springwebflux.webfluxclient.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.exceptions.LoadFromResourceException;
import otus.springwebflux.webfluxclient.exceptions.ResourceLoadException;
import otus.springwebflux.webfluxclient.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
@Lazy
@Service
public class FileService {
    @Value("${files.base.dir:/}")
    private String filesBaseDir;

    public Mono<String> getFileContentAsString(String fileName) {
        return DataBufferUtils.read(Paths.get(this.getFileURIFromResources(fileName)),
         DefaultDataBufferFactory.sharedInstance,
          DefaultDataBufferFactory.DEFAULT_INITIAL_CAPACITY)
          .map(dataBuffer -> dataBuffer.toString(StandardCharsets.UTF_8))
          .reduceWith(StringBuilder::new, StringBuilder::append)
          .map(StringBuilder::toString);
    }
  
    
    public <T> Flux<T> loadObjectList(Class<T> type, String fileName) throws ResourceLoadException {
            File file = Paths.get(getFileURIFromResources(fileName)).toFile();
            try {
                CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
                CsvMapper mapper = new CsvMapper();
                
                MappingIterator<T> readValues = 
                mapper.reader(type).with(bootstrapSchema).readValues(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
                return Flux.fromIterable(readValues.readAll());
            } catch (Exception ex) {
                log.error("Error occurred while loading object list from file :%s".formatted(fileName));
                throw new ResourceLoadException(file.getName(),ex);
            } finally {
                if(file.exists()) file.delete();
            }
        }

    public URI getFileURIFromResources(String fileName) {
        try {
            
            return stream2file(getResourceAsStream(fileName), fileName).toURI();

        } catch (Exception ex)
        {
            log.error("getFileURIFromResources: " + ex.getMessage());
            throw new ResourceLoadException(fileName, ex);
        }
    }

    public InputStream getResourceAsStream(String fileName) throws IOException {
        return new ClassPathResource(fileName).getInputStream();        
    }

    public static File stream2file (InputStream in, String fileName) throws IOException {
        File tempFile = Files.createTempFile(fileName.split(".")[0],fileName.split(".")[1]).toFile();
        try(FileOutputStream out = new FileOutputStream(tempFile)) {
                FileUtils.copyInputStreamToFile(in, tempFile);
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

}
