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
import java.math.BigDecimal;
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
        subscribe();
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
                if (interactive) {
                    createAuctionInteractive();
                }
                break;
            case "b":
                if (interactive) {
                        bidInteractive();
                }
                break;
            case "la":
                    listAuctions();
                break;
            case "sa":
                    setActiveAuction();
                break;
            case "q":
                ConsoleUtil.printExitMessage();
                unsubscribe();
                killContainer(); // TODO: improve system termination
            default:
                ConsoleUtil.printUnrecognizedCommandMessage();
        }
    }

    private void signInWithUsername() {
        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        final CommandMessage commandMessage = new CommandMessage(CommandType.SIGN_IN, clientUsername);
        appendMessageWithCommandMessageAndSend(message, commandMessage);
    }

    private void createAuctionInteractive() {
        System.out.println("Please enter name of the item to sell: ");
        Scanner reader = new Scanner(System.in);
        String itemName = reader.nextLine();

        System.out.println("Please enter date when auction should end [yyyy-MM-dd HH:mm:ss]: ");
        String endDateStr = reader.nextLine();

        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        final CommandMessage commandMessage = new CommandMessage(CommandType.CREATE_AUCTION, clientUsername, itemName, endDateStr);
        appendMessageWithCommandMessageAndSend(message, commandMessage);
    }

    private void bidInteractive(){
        System.out.println("Please enter limit: ");
        Scanner reader = new Scanner(System.in);
        String limit = reader.nextLine();

        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        final CommandMessage commandMessage = new CommandMessage(CommandType.BID, clientUsername, limit);
        appendMessageWithCommandMessageAndSend(message, commandMessage);
    }

    private void listAuctions() {
        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        final CommandMessage commandMessage = new CommandMessage(CommandType.LIST_AUCTIONS);
        appendMessageWithCommandMessageAndSend(message, commandMessage);
    }

    private void setActiveAuction() {
        System.out.println("Please enter auction id: ");
        Scanner reader = new Scanner(System.in);
        String auctionId = reader.nextLine();

        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        final CommandMessage commandMessage = new CommandMessage(CommandType.SET_ACTIVE_AUCTION, clientUsername, auctionId);
        appendMessageWithCommandMessageAndSend(message, commandMessage);
    }

    private void appendMessageWithCommandMessageAndSend(ACLMessage message, CommandMessage commandMessage) {
        try {
            message.setContentObject(commandMessage);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void unsubscribe() {
        try {
            final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            final CommandMessage commandMessage = new CommandMessage(CommandType.UNSUBSCRIBE, clientUsername);
            message.setContentObject(commandMessage);
            message.addReceiver(serverAid);
            myAgent.send(message);
            waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo implement properly
    private void subscribe() {
        try {
            final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            final CommandMessage commandMessage = new CommandMessage(CommandType.SUBSCRIBE, clientUsername);
            message.setContentObject(commandMessage);
            message.addReceiver(serverAid);
            myAgent.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(ACLMessage message) {
        message.addReceiver(serverAid);
        myAgent.send(message);
        waitForResponse();
    }

    private void waitForResponse() {
        ACLMessage response = myAgent.blockingReceive();
        if (response != null) {
            String responseContent = response.getContent();
            System.out.println(responseContent);
        }
    }

    private void killContainer() {
        try {
            myAgent.getContainerController().kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
