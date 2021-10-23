package util.datastructure.function;

@FunctionalInterface
public interface ToIntQuaFunction<P1, P2, P3, P4> {

    int applyAsInt(P1 p1, P2 p2, P3 p3, P4 p4);
}
