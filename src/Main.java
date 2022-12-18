import histogram.*;
import histogram.persistence.files.SQLiteFlightLoader;
import spark.Spark;
import com.google.gson.Gson;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        Spark.get("/hello", (request, response) -> "Hello world");
        Spark.get("/flights", (request, response) -> getFlightHistogramJSON());
    }

    private static String getFlightHistogramJSON() {
        return new Gson().toJson(getFlightHistogram());
    }

    private static Histogram<Integer> getFlightHistogram() {
        return FlightHistogramFactory.createFromDepartureTime(getFlights());
    }

    private static Iterable<Flight> getFlights() {
        return new SQLiteFlightLoader(Main::flightsResultSetFactory).items();
    }

    private static ResultSet flightsResultSetFactory() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:flights.db")
                    .createStatement()
                    .executeQuery("SELECT * FROM flights");
        } catch (SQLException e) {
            throw new RuntimeException("Unable to execute query.", e);
        }
    }
}