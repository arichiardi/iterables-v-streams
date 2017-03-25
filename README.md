# Compile

Use:

    mvn -Dclojure.compiler.disable-locals-clearing=true  -Dclojure.compiler.direct-linking=true clean clojure:compile && mvn package
   
# Usage

From the command line then you can do:

    java -cp target/iterables-v-streams-$VERSION-jar-with-dependencies.jar functional.IterablesBenchmark
    
# Benchmark results on my machine:

    # Run complete. Total time: 00:01:43
    
    Benchmark                                                         Mode  Cnt        Score        Error  Units
    IterablesBenchmark.CollectListToList.clojure_mapv_aot            thrpt    5   522866.456 ±  53771.542  ops/s
    IterablesBenchmark.CollectListToList.clojure_mapv_static_class   thrpt    5   735186.111 ±  30622.703  ops/s
    IterablesBenchmark.CollectListToList.guava                       thrpt    5  1179430.025 ±  18223.699  ops/s
    IterablesBenchmark.CollectListToList.guava_immutable             thrpt    5  1373184.339 ±  17016.481  ops/s
    IterablesBenchmark.CollectListToList.iterate                     thrpt    5  1371781.644 ±  15863.586  ops/s
    IterablesBenchmark.CollectListToList.iterate_immutable           thrpt    5  1163678.906 ± 122094.615  ops/s
    IterablesBenchmark.CollectListToList.parallel_streams            thrpt    5    93815.983 ±  21460.162  ops/s
    IterablesBenchmark.CollectListToList.parallel_streams_immutable  thrpt    5    83329.537 ±   2212.073  ops/s
    IterablesBenchmark.CollectListToList.streams                     thrpt    5  1283322.612 ±  34207.117  ops/s
    IterablesBenchmark.CollectListToList.streams_immutable           thrpt    5  1303504.086 ±  22961.295  ops/s

