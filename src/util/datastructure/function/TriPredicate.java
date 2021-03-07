package util.datastructure.function;

@FunctionalInterface
public interface TriPredicate<T, U, P> {

    boolean test(T t, U u, P p);
}
