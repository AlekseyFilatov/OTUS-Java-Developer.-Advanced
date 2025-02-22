package otus.springwebflux.webfluxclient.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.exceptions.LoadFromResourceException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;


@Slf4j
@Lazy
@Service
public class FileService {
    @Value("${files.base.dir:/}")
    private String filesBaseDir;

    public Mono<String> getFileContentAsString(String fileName) {
        return DataBufferUtils.read(Paths.get(filesBaseDir + "/" + fileName),
         DefaultDataBufferFactory.sharedInstance,
          DefaultDataBufferFactory.DEFAULT_INITIAL_CAPACITY)
          .map(dataBuffer -> dataBuffer.toString(StandardCharsets.UTF_8))
          .reduceWith(StringBuilder::new, StringBuilder::append)
          .map(StringBuilder::toString);
    }
  
    
    public <T> Flux<T> loadObjectList(Class<T> type, String fileName) {
            try {
                CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
                CsvMapper mapper = new CsvMapper();
                File file = getFileURIFromResources(fileName).toFile();
                MappingIterator<T> readValues = 
                mapper.reader(type).with(bootstrapSchema).readValues(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
                return Flux.fromIterable(readValues.readAll());
            } catch (Exception e) {
                log.error("Error occurred while loading object list from file " + fileName, e);
                return Flux.empty();
            }
        }

    public Path getFileURIFromResources(String fileName) {
            try {
            
                return Paths.get(new String(getClass().getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
                            .getPath() + fileName).substring(1));
    
            } catch (Exception e)
            {
                log.error("getFileURIFromResources: " + e.getMessage());
                throw new LoadFromResourceException(e.getMessage());
            }
        }

}
