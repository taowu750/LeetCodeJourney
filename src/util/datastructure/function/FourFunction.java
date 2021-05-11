package util.datastructure.function;

@FunctionalInterface
public interface FourFunction<P1, P2, P3, P4, R> {

    R apply(P1 param1, P2 param2, P3 param3, P4 param4);
}
