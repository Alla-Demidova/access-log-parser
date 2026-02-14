import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public LogEntry(String logLine) {
        try {

            int firstSpace = logLine.indexOf(' ');
            this.ipAddr = logLine.substring(0, firstSpace);


            int openBracket = logLine.indexOf('[');
            int closeBracket = logLine.indexOf(']', openBracket);

            if (openBracket >= 0 && closeBracket > openBracket) {
                String dateStr = logLine.substring(openBracket + 1, closeBracket);
                this.time = LocalDateTime.parse(dateStr, DATE_FORMATTER);
            } else {
                this.time = null;
            }


            int firstQuote = logLine.indexOf('"', closeBracket + 1);
            int secondQuote = logLine.indexOf('"', firstQuote + 1);

            HttpMethod parsedMethod = null;
            String parsedPath = "";

            if (firstQuote >= 0 && secondQuote > firstQuote) {
                String request = logLine.substring(firstQuote + 1, secondQuote);


                String[] requestParts = request.split(" ");
                if (requestParts.length >= 2) {
                    try {
                        parsedMethod = HttpMethod.valueOf(requestParts[0]);
                    } catch (IllegalArgumentException e) {
                        parsedMethod = null;
                    }
                    parsedPath = requestParts[1];
                }
            }

            this.method = parsedMethod;
            this.path = parsedPath;


            int responseCode = 0;
            int responseSize = 0;

            if (secondQuote >= 0) {
                String afterRequest = logLine.substring(secondQuote + 1).trim();
                String[] parts = afterRequest.split(" ");


                for (String part : parts) {
                    if (!part.isEmpty()) {
                        try {
                            responseCode = Integer.parseInt(part);
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                }

                boolean foundCode = false;
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        if (!foundCode) {
                            foundCode = true;
                        } else {
                            try {
                                responseSize = part.equals("-") ? 0 : Integer.parseInt(part);
                                break;
                            } catch (NumberFormatException e) {

                                break;
                            }
                        }
                    }
                }
            }

            this.responseCode = responseCode;
            this.responseSize = responseSize;


            String parsedReferer = "";
            int lastQuote = logLine.lastIndexOf('"');
            int secondLastQuote = logLine.lastIndexOf('"', lastQuote - 1);

            if (secondLastQuote >= 0 && lastQuote > secondLastQuote) {
               String possibleReferer = logLine.substring(secondLastQuote + 1, lastQuote);
                if (!possibleReferer.contains("Mozilla") && !possibleReferer.contains("Googlebot") &&
                        !possibleReferer.contains("Yandex")) {
                    parsedReferer = possibleReferer;
                } else {
                   int thirdLastQuote = logLine.lastIndexOf('"', secondLastQuote - 1);
                    if (thirdLastQuote >= 0) {
                        parsedReferer = logLine.substring(thirdLastQuote + 1, secondLastQuote);
                    }
                }
            }

            this.referer = parsedReferer;

            UserAgent parsedUserAgent;
            int lastQuoteIndex = logLine.lastIndexOf('"');
            int prevQuoteIndex = logLine.lastIndexOf('"', lastQuoteIndex - 1);

            if (prevQuoteIndex >= 0 && lastQuoteIndex > prevQuoteIndex) {
                String userAgentStr = logLine.substring(prevQuoteIndex + 1, lastQuoteIndex);
                parsedUserAgent = new UserAgent(userAgentStr);
            } else {
                parsedUserAgent = new UserAgent("");
            }

            this.userAgent = parsedUserAgent;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга строки: " + logLine, e);
        }
    }


    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddr='" + ipAddr + '\'' +
                ", time=" + time +
                ", method=" + method +
                ", path='" + path + '\'' +
                ", responseCode=" + responseCode +
                ", responseSize=" + responseSize +
                ", referer='" + referer + '\'' +
                ", userAgent=" + userAgent +
                '}';
    }
}