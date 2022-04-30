package util.datastructure.function;

@FunctionalInterface
public interface TriIntFunction<R> {

    R apply(int i1, int i2, int i3);
}
