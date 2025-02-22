package otus.springwebflux.webfluxclient.benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.stream.Collectors;

import java.util.stream.LongStream;

import org.openjdk.jmh.infra.Blackhole;

import otus.springwebflux.webfluxclient.model.Employee;
import reactor.core.publisher.Flux;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;



@State(Scope.Thread)
public class JmhBenchmark {

    public static void main(String[] args) throws Exception {

        java.net.URLClassLoader classLoader = (java.net.URLClassLoader) JmhBenchmark.class.getClassLoader();
        StringBuilder classpath = new StringBuilder();
        for (java.net.URL url : classLoader.getURLs()) {
            classpath.append(url.getPath()).append(java.io.File.pathSeparator);
        }
        System.setProperty("java.class.path", classpath.toString());

        org.openjdk.jmh.Main.main(args);
    
    }

    

    @Param({"10", "100", "1000"})
    private int size;

    private ConcurrentMap<Long, Employee> repositoryMap = new ConcurrentHashMap<>();

    public long putStore(long id, Employee empl) {
       repositoryMap.put(id, empl);
       return id;
    }

    @Setup
    public void setup() {
        LongStream
                .range(0, size)
                .boxed()
                .collect(Collectors.toList())
                .forEach(ln -> putStore(ln, new Employee(ln, String.valueOf(ln),"123","email@email.com","Address 123 and 123")))
                ;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(jvmArgsAppend = "-Dfile.encoding=UTF-8 -XX:+UseParallelGC")
    @Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    public void getRepo() {
        Flux.fromIterable(repositoryMap.values().stream().toList());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(jvmArgsAppend = "-Dfile.encoding=UTF-8 -XX:+UseParallelGC")
    @Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    public void getRepoWithBh(Blackhole bh) {
        Flux.fromIterable(repositoryMap.values().stream().toList()).toIterable().forEach(bh::consume);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(jvmArgsAppend = "-Dfile.encoding=UTF-8 -XX:+UseParallelGC")
    @Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    public void getRepoCollect() {

        Flux.fromIterable(repositoryMap.values().stream().collect(Collectors.toList()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(jvmArgsAppend = "-Dfile.encoding=UTF-8 -XX:+UseParallelGC")
    @Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    public void getRepoCollectWithBh(Blackhole bh) {

        Flux.fromIterable(repositoryMap.values().stream().collect(Collectors.toList())).toIterable().forEach(bh::consume);
    }

}



