package otus.springwebflux.webfluxclient.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import otus.springwebflux.webfluxclient.model.Employee;
import otus.springwebflux.webfluxclient.service.EncryptType;
import otus.springwebflux.webfluxclient.service.Encryptor;

@State(Scope.Thread)
public class JmhBenchmarkEncrypt {

    public static void main(String[] args) throws Exception {

        java.net.URLClassLoader classLoader = (java.net.URLClassLoader) JmhBenchmark.class.getClassLoader();
        StringBuilder classpath = new StringBuilder();
        for (java.net.URL url : classLoader.getURLs()) {
            classpath.append(url.getPath()).append(java.io.File.pathSeparator);
        }
        System.setProperty("java.class.path", classpath.toString());

        org.openjdk.jmh.Main.main(args);
    
    }

    Encryptor utils = new Encryptor();
    private final int operationCount = 1;

    @Param({EncryptType.MD5, EncryptType.SHA256, EncryptType.SHA512})
    private String type;

    @Benchmark
    @BenchmarkMode(Mode.All)
    //@Fork(value = 1, warmups = 1)
    //@Fork(jvmArgsAppend = "-Dfile.encoding=utf-8")
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(jvmArgsAppend = "-Dfile.encoding=UTF-8")
    @Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(operationCount)
    public void testEncrypt() {
        IntStream.range(0, operationCount)
                .mapToObj(i -> new Employee(Long.valueOf(i),"user" + i, "pass" + i, "email@email" + i, "addres 1 2 "+ i))
                .forEach(user -> utils.encrypt(type, user.getPassword()));
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    //@Fork(value = 1, warmups = 1)
    //@Fork(jvmArgsAppend = "-Dfile.encoding=utf-8")
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(jvmArgsAppend = "-Dfile.encoding=UTF-8")
    @Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(operationCount)
    public void testEncryptWithBh(Blackhole bh) {
        IntStream.range(0, operationCount)
                .mapToObj(i -> new Employee(Long.valueOf(i),"user" + i, "pass" + i, "email@email" + i, "addres 1 2 "+ i))
                .map(user -> utils.encrypt(type, user.getPassword())).forEach(bh::consume);
    }

}