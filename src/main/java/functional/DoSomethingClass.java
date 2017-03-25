package functional;

import java.util.Collection;

public class DoSomethingClass {

    private static long counter;

    public static void doSomething () {
        counter++;
    }

    public static void doSomethingAfter(Collection<String> result) {
        if (result.size() != counter) {
            counter = 1;
        }
        counter = 0;
    }
}
