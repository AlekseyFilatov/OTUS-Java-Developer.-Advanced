package otus.springwebflux.webfluxclient;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import otus.springwebflux.webfluxclient.model.Employee;

import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class SendingAppTest {

    private static final Logger log = LoggerFactory.getLogger(SendingAppTest.class);

	private static int ix = 0;
	private static MockWebServer mockBackEnd;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Random r = new Random();

    @Autowired
    TestRestTemplate template;

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();


    @BeforeClass
    public static void setUp() throws IOException {
        log.info("Dispatcher create!");
		final Dispatcher dispatcher = new Dispatcher() {

			@NotNull
			@Override
			public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
				String pathParam = recordedRequest.getPath().replaceAll("/slow/", "");
				List<Employee> employeesPart2 = List.of(
                        new Employee(Long.valueOf(r.nextInt(100000)), "Name" + pathParam, "Surname" + pathParam,
                         String.valueOf(r.nextInt(100)), String.valueOf(r.nextInt(100))),
                        new Employee(Long.valueOf(r.nextInt(100000)), "Name" + pathParam, "Surname" + pathParam,
                         String.valueOf(r.nextInt(100)), String.valueOf(r.nextInt(100))),
                        new Employee(Long.valueOf(r.nextInt(100000)), "Name" + pathParam, "Surname" + pathParam,
                         String.valueOf(r.nextInt(100)), String.valueOf(r.nextInt(100))));
				try {
					return new MockResponse()
							.setResponseCode(200)
							.setBody(mapper.writeValueAsString(employeesPart2))
							.setHeader("Content-Type", "application/json")
							.setBodyDelay(200, TimeUnit.MILLISECONDS);
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		mockBackEnd = new MockWebServer();
		mockBackEnd.setDispatcher(dispatcher);
		mockBackEnd.start();
		System.setProperty("target.uri", "http://localhost:" + mockBackEnd.getPort());

	}

    @Test
    @BenchmarkOptions(warmupRounds = 3, concurrency = 50, benchmarkRounds = 100)
    public void testSend() throws InterruptedException {
        ResponseEntity<Employee[]> r = template.exchange("http://localhost:" + mockBackEnd.getPort() + "/employees/integration/{param}", HttpMethod.GET, null, Employee[].class, ++ix);
        Assert.assertEquals(200, r.getStatusCodeValue());
        Assert.assertNotNull(r.getBody());
    }

    @AfterClass
    public static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }
}