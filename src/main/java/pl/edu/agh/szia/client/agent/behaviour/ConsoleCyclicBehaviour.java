package pl.edu.agh.szia.client.agent.behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.szia.client.utils.ConsoleUtil;
import pl.edu.agh.szia.utils.Configuration;
import pl.edu.agh.szia.utils.command.CommandMessage;
import pl.edu.agh.szia.utils.command.CommandType;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleCyclicBehaviour extends CyclicBehaviour {

    private final AID serverAid;
    private String clientUsername;

    public ConsoleCyclicBehaviour(String clientUsername) {
        this.clientUsername = clientUsername;
        this.serverAid = new AID(Configuration.getServerAgentGuid(), AID.ISGUID);
    }

    @Override
    public void onStart() {
        super.onStart();
        signInWithUsername();
    }

    @Override
    public void action() {
        boolean interactive = false;

        ConsoleUtil.printMenu();

        Scanner reader = new Scanner(System.in);
        String[] input = reader.nextLine().split(" ");

        if (input.length == 1) {
            interactive = true;
        }

        switch (input[0].toLowerCase()) {
            case "c":
                if (interactive)
//                        createAuctionInteractive();
                    break;
            case "b":
                if (interactive)
//                        bidInteractive();
                    break;
            case "la":
                    listAuctions();
                break;
            case "sa":
//                    setCurrentAuction();
                break;
            case "q":
                ConsoleUtil.printExitMessage();
                killContainer(); // TODO: improve system termination
            default:
                ConsoleUtil.printUnrecognizedCommandMessage();
        }
    }

    private void signInWithUsername() {
        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

        CommandMessage commandMessage = new CommandMessage(CommandType.SIGN_IN, clientUsername);

        try {
            message.setContentObject(commandMessage);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listAuctions() {
        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

        CommandMessage commandMessage = new CommandMessage(CommandType.LIST_AUCTIONS);

        try {
            message.setContentObject(commandMessage);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(ACLMessage message) {
        message.addReceiver(serverAid);
        myAgent.send(message);
    }

    private void killContainer() {
        try {
            myAgent.getContainerController().kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
