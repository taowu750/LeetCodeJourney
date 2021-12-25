package util.datastructure.function;

@FunctionalInterface
public interface ToIntTriFunction<P1, P2, P3> {

    int applyAsInt(P1 param1, P2 param2, P3 param3);
}
