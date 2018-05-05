package pl.edu.agh.szia;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.szia.auction.Auction;
import pl.edu.agh.szia.data.Product;
import pl.edu.agh.szia.data.User;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AuctionSystem {
    private static ContainerController mainContainer;
    private static User currentUser;
    private static Map<String, User> users = new HashMap<>();
    private static Auction currentAuction;
    private static Map<Integer, Auction> auctions = new HashMap<>();
    private static Integer newAuctionID = 1;
    private static Integer currentAuctionId = 1;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String [] args){
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        mainContainer = runtime.createMainContainer(profile);

        boolean quit = false;

        while (!quit) {
            boolean interactive = false;

            printMenu();

            Scanner reader = new Scanner(System.in);
            String[] input = reader.nextLine().split(" ");

            if (input.length == 1) {
                interactive = true;
            }

            switch (input[0].toLowerCase()) {
                case "c":
                    if(currentUser == null){
                        System.out.println("You have to login first");
                        break;
                    }

                    if(interactive)
                        createAuctionInteractive();
                    break;
                case "b":
                    if(interactive)
                        bidInteractive();
                    break;
                case "l":
                    if(interactive)
                        currentUser = loginInteractive();
                    break;
                case "la":
                    listAuctions();
                    break;
                case "sa":
                    setCurrentAuction();
                    break;
                case "q":
                    quit = true;
                    System.out.println("Bye");
                    break;
                default:
                    System.out.println("Unrecognized option");
            }
        }
    }

    public static void printMenu(){
        System.out.println("Choose option: \n" +
                        "c - create auction \n" +
                        "b - bid \n" +
                        "l - login \n" +
                        "la - list auctions \n" +
                        "sa - set current auction \n" +
                        "q - quit");
    }

    public static void createAuctionInteractive(){
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

    public static void createSellerAgent(String itemName, Long endTime){
        try {
            currentAuction = new Auction(null, new Product(itemName), new BigDecimal(1), newAuctionID, endTime);
            AgentController ag = mainContainer.createNewAgent("sellerAgent" + newAuctionID,
                    "pl.edu.agh.szia.agent.SellerAgent", new Object[] {currentUser, itemName, currentAuction, endTime});
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static User loginInteractive(){
        System.out.println("Enter username: ");
        Scanner reader = new Scanner(System.in);
        String username = reader.nextLine();

        if(users.get(username) != null) {
            System.out.println("Welcome back " + username);
            return users.get(username);
        }

        System.out.println("Welcome new user: " + username);
        User newUser = new User(username);
        users.put(username, newUser);
        return newUser;
    }

    public static void bidInteractive(){
        System.out.println("Please enter limit: ");
        Scanner reader = new Scanner(System.in);
        String limit = reader.nextLine();

        createBuyerAgent(new BigDecimal(limit));
    }

    public static void createBuyerAgent(BigDecimal limit){
        try {
            AgentController ag = mainContainer.createNewAgent(currentUser.getUsername() + currentAuctionId.toString(),
                    "pl.edu.agh.szia.agent.BuyerAgent", new Object[] {currentAuction, limit});
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static void listAuctions(){
        for(Auction auction: auctions.values()){
            auction.printAuction();
        }
    }

    public static void setCurrentAuction(){
        System.out.println("Please enter auction id: ");
        Scanner reader = new Scanner(System.in);
        String auctionId = reader.nextLine();

        currentAuctionId = Integer.parseInt(auctionId);
        currentAuction = auctions.get(currentAuctionId);
    }
}
