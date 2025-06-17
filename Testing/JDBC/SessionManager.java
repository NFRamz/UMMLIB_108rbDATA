package Testing.JDBC;

import java.util.UUID;

public class SessionManager {
    private static String sessionId = generateNewSessionId();

    public static String getSessionId() {
        return sessionId;
    }

    public static void resetSession() {
        sessionId = generateNewSessionId();
    }

    private static String generateNewSessionId() {
        return UUID.randomUUID().toString();
    }
}

