import java.time.LocalDateTime;
import java.time.Duration;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

       if (minTime == null || entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }

       if (maxTime == null || entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }
    }


    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }

       Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toSeconds() / 3600.0;

        if (hours <= 0) {
            return totalTraffic;
        }

        return totalTraffic / hours;
    }

    public int getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public long getTimeRangeInHours() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        return Duration.between(minTime, maxTime).toHours();
    }
}