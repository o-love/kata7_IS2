package histogram.persistence;

public interface Loader<T> {
    Iterable<T> items();
}
