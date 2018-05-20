package pl.edu.agh.szia.utils;

public class Configuration {
    public static final String PLATFORM_IP = System.getProperty("server_address");
    public static final int PLATFORM_PORT = 1099;
    public static final String SERVER_AGENT_NAME = "serverAgent";
    public static final String CLIENT_AGENT_NAME_PATTERN = "client-%s";

    public static String getServerAgentGuid() {
        return String.format("%s@%s:%d/JADE", SERVER_AGENT_NAME, PLATFORM_IP, PLATFORM_PORT);
    }

    public static String getClientAgentName(String username) {
        return String.format(CLIENT_AGENT_NAME_PATTERN, username);
    }
}
