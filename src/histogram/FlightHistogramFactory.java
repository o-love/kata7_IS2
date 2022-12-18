package histogram;

import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.stream.StreamSupport;

public class FlightHistogramFactory {

    public static Histogram<Integer> createFromDepartureTime(Iterable<Flight> flights) {
        return create(flights, flight -> flight.departureTime().getHour());
    }

    private static Histogram<Integer> create(Iterable<Flight> flights, ToIntFunction<Flight> intMapper) {
        Objects.requireNonNull(flights);
        Objects.requireNonNull(intMapper);

        Histogram<Integer> histogram = new Histogram<>();
        StreamSupport.stream(flights.spliterator(), false)
                .mapToInt(intMapper)
                .forEach(histogram::increment);
        return histogram;
    }
}
