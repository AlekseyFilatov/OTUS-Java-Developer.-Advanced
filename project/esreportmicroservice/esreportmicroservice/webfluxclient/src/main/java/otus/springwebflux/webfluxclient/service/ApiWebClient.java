package otus.springwebflux.webfluxclient.service;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Consumer;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import org.springframework.core.io.buffer.DataBuffer;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import jakarta.annotation.PreDestroy;

import lombok.Getter;

import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeClientRequestDTO;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeRequestDTO;
import otus.springwebflux.webfluxclient.dto.EmployeeAggreegateID;
import otus.springwebflux.webfluxclient.exceptions.ApiWebClientException;
import otus.springwebflux.webfluxclient.exceptions.ApiWebClientInEventSourcingRuntimeException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

/*
 * https://www.baeldung.com/webclient-stream-large-byte-array-to-file
 * https://javarush.com/quests/lectures/questspring.level01.lecture40
 * https://www.cyberforum.ru/java-spring/thread2277712.html
 */

@Configuration
@Slf4j
@Lazy
@Service
public class ApiWebClient {


    @Getter
    @Value(value = "${webclient.reportservice.uri}")
    private String reportserviceUri;

    @Getter
    @Value(value = "${webclient.grpcservice.uri}")
    private String grpcserviceUri;

    @Getter
    @Value(value = "${webclient.esservice.uri}")
    private String esserviceserviceUri;

    @Value("${target.uri}")
    private String targetUri;

    @Bean("webClientES")
    public WebClient webClientES() {
        return WebClient.builder()
            .baseUrl(this.getEsserviceserviceUri())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient())))  // timeout
            .build();
    }

    @Bean("webClientReport")
    public WebClient webClientReport() {
        return WebClient.builder()
            .baseUrl(this.getReportserviceUri())
            .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs()
                    .maxInMemorySize((int) Runtime.getRuntime().maxMemory())
                )
                .build())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_PDF_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient())))  // timeout
            .build();
    }


    @Bean("webClientGRPC")
    public WebClient webClientGRPC() {
        return WebClient.builder()
            .baseUrl(this.getGrpcserviceUri())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient())))  // timeout
            .build();
    }


    @Bean("webClientEmpty")
    public WebClient webClient() {
        return WebClient.builder().baseUrl(targetUri).build();
    }

    @Bean
    public TcpClient tcpClient() {
        return TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .doOnConnected(connection ->
            connection.addHandlerLast(new ReadTimeoutHandler(10))
            .addHandlerLast(new WriteTimeoutHandler(10)));
    };


    public Mono<DataBuffer> getReport () {

            Consumer<HttpHeaders> headers = httpHeaders -> {
                httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
                httpHeaders.setContentDispositionFormData("filename", "employees-details.pdf");
                httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                httpHeaders.add("Pragma", "no-cache");
                httpHeaders.add("Expires", "0");
            };
            
           return  webClientReport().get()
                    .uri("/employee/records/report")
                    .accept(MediaType.APPLICATION_PDF)
                    .headers(headers)
                    .retrieve()
                    // handle status
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                        log.error("Error endpoint with status code {}", clientResponse.statusCode());
                        throw new ApiWebClientException("HTTP Status 500 error - " + clientResponse.toString());  // throw custom exception
                    })
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                        log.error("Error endpoint with status code {}",  clientResponse.statusCode());
                        throw new ApiWebClientException("HTTP Status 400 error - " + clientResponse.toString());  // throw custom exception
                    })
                    .bodyToMono(DataBuffer.class)
                    .onErrorMap(e -> {
                        log.error("GET Report Error: %s%n".formatted(e.getMessage()));
                        return new ApiWebClientInEventSourcingRuntimeException("GET Report Error: %s%n".formatted(e.getMessage()), e);
                     });                   
        }        

    public Mono<EmployeeAggreegateID> createClient(CreateEmployeeRequestDTO dto) {
        log.info("%s -> ApiWebClient.createClient -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
        return Mono.just(dto)
                   .then(webClientES().post()
                .uri("/api/employee")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Authorization createClient")
                .body(Mono.just(dto), CreateEmployeeRequestDTO.class)
                .retrieve()

                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("Error endpoint with status code {}", clientResponse.statusCode());
                    throw new ApiWebClientException("HTTP Status 500 error - " + clientResponse.toString());  // throw custom exception
                })
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("Error endpoint with status code {}",  clientResponse.statusCode());
                    throw new ApiWebClientException("HTTP Status 400 error - " + clientResponse.toString());  // throw custom exception
                })
                .bodyToMono(String.class))
                .doOnNext(str -> log.info("body: %s".formatted(str)))
                .flatMap(emp -> Mono.just(new EmployeeAggreegateID(emp)))
                .onErrorResume(e -> {
                    log.error("ES Client Error: " + e.getMessage());
                    return Mono.empty();
                 });              
                
    }

    public Mono<EmployeeAggreegateID> createClientGRPC(CreateEmployeeClientRequestDTO dto) {
        log.info("%s -> ApiWebClient.createClientGRPC -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
        return Mono.just(dto)
                .then(webClientGRPC().post()
                .uri("/api/employee")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Authorization createClient")
                .body(Mono.just(dto), CreateEmployeeClientRequestDTO.class)
                .retrieve()

                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("Error endpoint with status code {}", clientResponse.statusCode());
                    throw new ApiWebClientException("HTTP Status 500 error - " + clientResponse.toString());  // throw custom exception
                })
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("Error endpoint with status code {}",  clientResponse.statusCode());
                    throw new ApiWebClientException("HTTP Status 400 error - " + clientResponse.toString());  // throw custom exception
                })
                .bodyToMono(String.class))
                .flatMap(emp -> Mono.just(new EmployeeAggreegateID(emp)))
                .onErrorMap(e -> {
                    log.error("GRPC Client Error: %s%n".formatted(e.getMessage()));
                    return new ApiWebClientInEventSourcingRuntimeException("GRPC Client Error: %s%n".formatted(e.getMessage()), e);
                    //return Mono.empty();
                 });              
                
    }

    public ClientResponse createClientWithDuration(ClientRequest request) {

        //ClientResponse response = 
        return webClientES().post()
                .uri("/api/employee")
                .body(Mono.just(request), ClientRequest.class)
                .retrieve()

                // handle status
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("Error endpoint with status code {}", clientResponse.statusCode());
                    throw new ApiWebClientException("HTTP Status 500 error - " + clientResponse.toString());  // throw custom exception
                })
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("Error endpoint with status code {}",  clientResponse.statusCode());
                    throw new ApiWebClientException("HTTP Status 400 error - " + clientResponse.toString());  // throw custom exception
                })

                .bodyToMono(ClientResponse.class)
                .onErrorResume(e -> {
                    log.error("error: " + e.getMessage());
                    return Mono.empty();
                 })
                .timeout(Duration.ofSeconds(3))  // timeout
                .block();

    }

   @PreDestroy
    public void preDestroy() {
        webClientES().delete();
        webClientGRPC().delete();
        webClientReport().delete();
        webClient().delete();
    } 

}