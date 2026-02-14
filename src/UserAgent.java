import java.util.Locale;

public class UserAgent {
    private final String browser;
    private final String operatingSystem;

    public UserAgent(String userAgentString) {
        if (userAgentString == null || userAgentString.isEmpty()) {
            this.browser = "Unknown";
            this.operatingSystem = "Unknown";
            return;
        }

        this.browser = parseBrowser(userAgentString);
        this.operatingSystem = parseOperatingSystem(userAgentString);
    }

    private String parseBrowser(String ua) {
        String lowerUa = ua.toLowerCase(Locale.ROOT);

        if (lowerUa.contains("edg/") || lowerUa.contains("edga/") || lowerUa.contains("edge/")) {
            return "Edge";
        } else if (lowerUa.contains("opr/") || lowerUa.contains("opera")) {
            return "Opera";
        } else if (lowerUa.contains("chrome/") && !lowerUa.contains("edg/") && !lowerUa.contains("opr/")) {
            return "Chrome";
        } else if (lowerUa.contains("firefox/")) {
            return "Firefox";
        } else if (lowerUa.contains("safari/") && !lowerUa.contains("chrome/")) {
            return "Safari";
        } else if (lowerUa.contains("yandexbot")) {
            return "YandexBot";
        } else if (lowerUa.contains("googlebot")) {
            return "Googlebot";
        } else if (lowerUa.contains("curl/")) {
            return "curl";
        } else if (lowerUa.contains("postman")) {
            return "Postman";
        }

        return "Other";
    }

    private String parseOperatingSystem(String ua) {
        String lowerUa = ua.toLowerCase(Locale.ROOT);

        if (lowerUa.contains("windows")) {
            return "Windows";
        } else if (lowerUa.contains("mac os")) {
            return "macOS";
        } else if (lowerUa.contains("linux")) {
            if (lowerUa.contains("android")) {
                return "Android";
            }
            return "Linux";
        } else if (lowerUa.contains("iphone") || lowerUa.contains("ipad")) {
            return "iOS";
        } else if (lowerUa.contains("android")) {
            return "Android";
        }

        return "Unknown";
    }

    public String getBrowser() {
        return browser;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "browser='" + browser + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                '}';
    }
}