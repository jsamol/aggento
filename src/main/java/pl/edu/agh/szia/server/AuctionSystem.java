package pl.edu.agh.szia.server;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.szia.server.auction.Auction;
import pl.edu.agh.szia.data.Product;
import pl.edu.agh.szia.data.User;
import pl.edu.agh.szia.utils.Configuration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AuctionSystem {
    private static final String SERVER_AGENT_PATH = "pl.edu.agh.szia.server.agent.server.ServerAgent";

    private static final String USER_DOES_NOT_EXIST = "User does not exist.";
    private static final String USERNAME_ALREADY_TAKEN_ERROR_PATTERN = "Username %s is already taken.";
    private static final String USERNAME_REGISTERED_PATTERN = "Welcome, %s!";

    private static final String AUCTION_CREATED = "A new auction has been created.";
    private static final String AUCTION_BID_SUCCESSFUL = "Bid successful.";
    private static final String AUCTION_DOES_NOT_EXIST = "Auction with a given id does not exist.";
    private static final String AUCTION_SET_ACTIVE_SUCCESSFUL_PATTERN = "Active auction set to %s";

    private ContainerController mainContainer;
    private Map<String, User> users = new HashMap<>();
    private Map<Integer, Auction> auctions = new HashMap<>();
    private Map<String, String> subscribers = new HashMap<>();
    private Integer newAuctionID = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String [] args){
        AuctionSystem auctionSystem = new AuctionSystem();
        auctionSystem.run();
    }

    private void run() {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        mainContainer = runtime.createMainContainer(profile);
        try {
            AgentController agentController = mainContainer.createNewAgent(Configuration.SERVER_AGENT_NAME, SERVER_AGENT_PATH, new Object[] { this });
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public ACLMessage registerUser(String username) {
        ACLMessage response;
        if (users.containsKey(username)) {
            response = new ACLMessage(ACLMessage.REFUSE);
            response.setContent(String.format(USERNAME_ALREADY_TAKEN_ERROR_PATTERN, username));
        } else {
            User user = new User(username);
            users.put(username, user);

            response = new ACLMessage(ACLMessage.AGREE);
            response.setContent(String.format(USERNAME_REGISTERED_PATTERN, username));
        }

        return response;
    }

    public ACLMessage createAuction(String username, String itemName, String endDateStr){
        if (users.containsKey(username)) {
            try {
                User currentUser = users.get(username);

                Date endDate = dateFormat.parse(endDateStr);
                Long endTime = endDate.getTime();

                Auction currentAuction = new Auction(null, new Product(itemName), new BigDecimal(1), newAuctionID, endTime);
                auctions.put(newAuctionID, currentAuction);
                currentUser.addOwnedAuction(currentAuction);
                currentUser.setActiveAuction(currentAuction);

                createSellerAgent(currentUser, itemName, endTime);
                newAuctionID += 1;

                final ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
                response.setContent(AUCTION_CREATED);
                return response;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final ACLMessage response = new ACLMessage(ACLMessage.CANCEL);
        response.setContent(USER_DOES_NOT_EXIST);
        return response;
    }

    public ACLMessage bid(String username, String limit){
        final ACLMessage response;
        if (!users.containsKey(username)) {
            response = new ACLMessage(ACLMessage.CANCEL);
            response.setContent(USER_DOES_NOT_EXIST);
        }
        else {
            User currentUser = users.get(username);
            createBuyerAgent(currentUser, new BigDecimal(limit));

            response = new ACLMessage(ACLMessage.CONFIRM);
            response.setContent(AUCTION_BID_SUCCESSFUL);
        }

        return response;
    }

    public ACLMessage listAuctions() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Auction auction: auctions.values()){
            stringBuilder.append(auction);
        }

        final ACLMessage response = new ACLMessage(ACLMessage.INFORM);
        response.setContent(stringBuilder.toString());

        return response;
    }

    public ACLMessage setActiveAuction(String username, String auctionId){
        final ACLMessage response;

        int currentAuctionId = Integer.parseInt(auctionId);
        if (!auctions.containsKey(currentAuctionId)) {
            response = new ACLMessage(ACLMessage.CANCEL);
            response.setContent(AUCTION_DOES_NOT_EXIST);
        } else {
            Auction auction = auctions.get(currentAuctionId);
            if (!users.containsKey(username)) {
                response = new ACLMessage(ACLMessage.CANCEL);
                response.setContent(USER_DOES_NOT_EXIST);
            } else {
                User currentUser = users.get(username);
                currentUser.setActiveAuction(auction);

                response = new ACLMessage(ACLMessage.CONFIRM);
                response.setContent(String.format(AUCTION_SET_ACTIVE_SUCCESSFUL_PATTERN, auctionId));
            }
        }

        return response;
    }

    //todo implement properly
    public ACLMessage addSubscriber(String username) {
        final ACLMessage response;

        response = new ACLMessage(ACLMessage.CONFIRM);
        response.setContent("OK");
        subscribers.put(username, username);
        System.out.println("Currently subscribing:");
        System.out.println(subscribers);
        return response;
    }

    //todo implement properly
    public ACLMessage removeSubscriber(String username) {
        final ACLMessage response;

        response = new ACLMessage(ACLMessage.CONFIRM);
        response.setContent("OK");
        subscribers.remove(username);
        System.out.println("Currently subscribing:");
        System.out.println(subscribers);
        return response;
    }



    private void createSellerAgent(User user, String itemName, Long endTime){
        try {
            AgentController ag = mainContainer.createNewAgent("sellerAgent" + newAuctionID,
                    "pl.edu.agh.szia.server.agent.user.SellerAgent", new Object[] { user, itemName, endTime });
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void createBuyerAgent(User user, BigDecimal limit){
        try {
            AgentController ag = mainContainer.createNewAgent(user.getUsername() + user.getActiveAuction().getId().toString(),
                    "pl.edu.agh.szia.server.agent.user.BuyerAgent", new Object[] { user.getActiveAuction(), limit });
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
