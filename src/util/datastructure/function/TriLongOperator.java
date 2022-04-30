package util.datastructure.function;

@FunctionalInterface
public interface TriLongOperator {

    long applyAsLong(long l1, long l2, long l3);
}
