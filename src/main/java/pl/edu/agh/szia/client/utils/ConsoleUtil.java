package pl.edu.agh.szia.client.utils;

public class ConsoleUtil {
    private static final String WELCOME_MESSAGE = "Hello! Please enter your username:";
    private static final String MENU = "Choose an option: \n" +
                                        "c - create auction \n" +
                                        "b - bid \n" +
                                        "la - list auctions \n" +
                                        "sa - set current auction \n" +
                                        "q - quit";
    private static final String EXIT_MESSAGE = "Bye!";
    private static final String UNRECOGNIZED_COMMAND_MESSAGE = "CommandMessage unrecognized.";

    public static void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public static void printMenu() {
        System.out.println(MENU);
    }

    public static void printExitMessage() {
        System.out.println(EXIT_MESSAGE);
    }

    public static void printUnrecognizedCommandMessage() {
        System.out.println(UNRECOGNIZED_COMMAND_MESSAGE);
    }
}
