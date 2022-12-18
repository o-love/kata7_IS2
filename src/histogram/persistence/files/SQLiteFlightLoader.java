package histogram.persistence.files;

import histogram.Flight;
import histogram.persistence.Loader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

public class SQLiteFlightLoader implements Loader<Flight> {

    private final Supplier<ResultSet> rsSupplier;

    public SQLiteFlightLoader(Supplier<ResultSet> rsSupplier) {
        Objects.requireNonNull(rsSupplier);

        this.rsSupplier = rsSupplier;
    }
    
    @Override
    public Iterable<Flight> items() {
        return this::flightIterator;
    }
    
    private Iterator<Flight> flightIterator() {
        return new Iterator<>() {
            final ResultSet rs;
            private Flight nextFlight;

            {
                rs = rsSupplier.get();

                try {
                    nextFlight = produceNextFlight();
                } catch (SQLException e) {
                    nextFlight = null;
                }
            }

            @Override
            public boolean hasNext() {
                return nextFlight != null;
            }

            @Override
            public Flight next() {
                try {
                    return getNextFlight();
                } catch (SQLException e) {
                    return null;
                }
            }

            private Flight getNextFlight() throws SQLException {
                Flight toReturn = nextFlight;
                this.nextFlight = produceNextFlight();
                return toReturn;
            }

            private Flight produceNextFlight() throws SQLException {
                if (rs.next()) {
                    return produceFlight();
                }
                return null;
            }

            private Flight produceFlight() throws SQLException {
                return new Flight(
                        rs.getInt("DAY_OF_WEEK"),
                        localTimeOfColumn("DEP_TIME"),
                        rs.getInt("DEP_DELAY"),
                        localTimeOfColumn("ARR_TIME"),
                        rs.getInt("ARR_DELAY"),
                        rs.getBoolean("CANCELLED"),
                        rs.getBoolean("DIVERTED"),
                        localTimeOfColumn("AIR_TIME"),
                        rs.getInt("DISTANCE")
                );
            }

            private LocalTime localTimeOfColumn(String columnName) throws SQLException {
                return LocalTime.of(rs.getInt(columnName) / 100 % 24, rs.getInt(columnName) % 100 % 60);
            }
        };
    }
}
