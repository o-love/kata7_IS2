package histogram;

import java.time.LocalTime;

public record Flight (
        int day_of_week,
        LocalTime departureTime,
        int departureDelay,
        LocalTime arrivalTime,
        int arrivalDelay,
        boolean cancelled,
        boolean diverted,
        LocalTime airTime,
        int distance
) {}
