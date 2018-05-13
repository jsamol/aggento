package pl.edu.agh.szia.server;

import jade.core.Profile;
import jade.core.ProfileImpl;
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

    private ContainerController mainContainer;
    private User currentUser;
    private Map<String, User> users = new HashMap<>();
    private Auction currentAuction;
    private Map<Integer, Auction> auctions = new HashMap<>();
    private Integer newAuctionID = 1;
    private Integer currentAuctionId = 1;
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

    public void createAuctionInteractive(){
        System.out.println("Please enter name of the item to sell: ");
        Scanner reader = new Scanner(System.in);
        String itemName = reader.nextLine();

        System.out.println("Please enter date when auction should end [yyyy-MM-dd HH:mm:ss]: ");
        String endDateStr = reader.nextLine();

        try{
            Date endDate = dateFormat.parse(endDateStr);
            createSellerAgent(itemName, endDate.getTime());
            auctions.put(newAuctionID, currentAuction);
            currentUser.addOwnedAuction(currentAuction);
            newAuctionID += 1;

        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    public void bidInteractive(){
        System.out.println("Please enter limit: ");
        Scanner reader = new Scanner(System.in);
        String limit = reader.nextLine();

        createBuyerAgent(new BigDecimal(limit));
    }

    public void listAuctions(){
        for(Auction auction: auctions.values()){
            auction.printAuction();
        }
    }

    public void setCurrentAuction(){
        System.out.println("Please enter auction id: ");
        Scanner reader = new Scanner(System.in);
        String auctionId = reader.nextLine();

        currentAuctionId = Integer.parseInt(auctionId);
        currentAuction = auctions.get(currentAuctionId);
    }

    private void createSellerAgent(String itemName, Long endTime){
        try {
            currentAuction = new Auction(null, new Product(itemName), new BigDecimal(1), newAuctionID, endTime);
            AgentController ag = mainContainer.createNewAgent("sellerAgent" + newAuctionID,
                    "pl.edu.agh.szia.server.agent.user.SellerAgent", new Object[] {currentUser, itemName, currentAuction, endTime});
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void createBuyerAgent(BigDecimal limit){
        try {
            AgentController ag = mainContainer.createNewAgent(currentUser.getUsername() + currentAuctionId.toString(),
                    "pl.edu.agh.szia.server.agent.user.BuyerAgent", new Object[] {currentAuction, limit});
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
