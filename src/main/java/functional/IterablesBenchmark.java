package functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.transform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import clojure.java.api.Clojure;
import clojure.lang.AFn;

public class IterablesBenchmark {

    public static void main(String... args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(CollectListToList.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(options).run();
    }

    private static long counter;

    private static void doSomething() {
        counter++;
    }

    private static final com.google.common.base.Function<String, String> guavaIdentity = s -> {
        doSomething();
        return s;
    };

    private static final java.util.function.Function<String, String> streamsIdentity = s -> {
        doSomething();
        return s;
    };

    /**
       There is an identity function in Clojure but
       we do stuff inside here
    */
    private static final class CljIdentity extends AFn {
        @Override
        public Object invoke(Object s) {
            doSomething();
            return s;
        }
    };

    private static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList() {
        return Collector.of(
            ImmutableList.Builder::new,
            ImmutableList.Builder::add,
            (l, r) -> l.addAll(r.build()),
            ImmutableList.Builder<T>::build);
    }


    private static final String[] data = new String[100];
    static {
        Arrays.fill(data, "hello");
    }

    private static final List<String> list = Arrays.asList(data);

    @BenchmarkMode(value = Mode.Throughput)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public static class CollectListToList {

        @Benchmark
        public void iterate() {
            List<String> result = new ArrayList<>(list.size());
            for (String each : list) {
                doSomething();
                result.add(each);
            }
            doSomethingAfter(result);
        }

        @Benchmark
        public void iterate_immutable() {
            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (String each : list) {
                doSomething();
                builder.add(each);
            }
            List<String> result = builder.build();
            doSomethingAfter(result);
        }

        @Benchmark
        public void guava() {
            List<String> result = Lists.newArrayList(transform(list, guavaIdentity));
            doSomethingAfter(result);
        }

        @Benchmark
        public void guava_immutable() {
            List<String> result = ImmutableList.copyOf(transform(list, guavaIdentity));
            doSomethingAfter(result);
        }

        @Benchmark
        public void streams() {
            List<String> result = list
                .stream()
                .map(streamsIdentity)
                .collect(Collectors.toList());
            doSomethingAfter(result);

        }

        @Benchmark
        public void streams_immutable() {
            List<String> result = list
                .stream()
                .map(streamsIdentity)
                .collect(toImmutableList());
            doSomethingAfter(result);
        }

        @Benchmark
        public void parallel_streams() {
            List<String> result = list
                .parallelStream()
                .map(streamsIdentity)
                .collect(Collectors.toList());
            doSomethingAfter(result);

        }

        @Benchmark
        public void parallel_streams_immutable() {
            List<String> result = list
                .parallelStream()
                .map(streamsIdentity)
                .collect(toImmutableList());
            doSomethingAfter(result);
        }

        @Benchmark
        public void clojure_mapv_in_java() {
            // Using mapv in order to avoid lazyness
            IFn mapv = Clojure.var("clojure.core", "mapv");
            List<String> result = (List<String>) mapv.invoke(new CljIdentity(), list);
            doSomethingAfter(result);
        }
    }

    private static void doSomethingAfter(Collection<String> result) {
        if (result.size() != counter) {
            counter = 1;
        }
        counter = 0;
    }
}