package pl.edu.agh.szia.client;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.szia.client.utils.ConsoleUtil;
import pl.edu.agh.szia.utils.Configuration;

import java.util.Scanner;

public class Client {
    private static final String CLIENT_AGENT_PATH = "pl.edu.agh.szia.client.agent.ClientAgent";

    private final ContainerController containerController;
    private String username;

    public Client(ContainerController containerController) {
        this.containerController = containerController;
    }

    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        ContainerController containerController = runtime.createAgentContainer(new ProfileImpl());

        Client client = new Client(containerController);
        client.run();
    }

    private void run() {
        signUp();
        createClientAgent(Configuration.getClientAgentName(username));
    }

    private void signUp() {
        ConsoleUtil.printWelcomeMessage();

        Scanner reader = new Scanner(System.in);
        username = reader.nextLine();
    }

    private void createClientAgent(String agentName) {
        try {
            AgentController agentController = containerController.createNewAgent(agentName, CLIENT_AGENT_PATH, new Object[] { username });
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
