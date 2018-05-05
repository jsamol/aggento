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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AuctionSystem {
    private static ContainerController mainContainer;
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser;
    private static Auction auction;

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
        System.out.println("Choose option: \n " +
                        "c - create auction \n" +
                        "b - bid \n" +
                        "l - login \n " +
                        "q - quit");
    }

    public static void createAuctionInteractive(){
        System.out.println("Please enter name of item to sell: ");
        Scanner reader = new Scanner(System.in);
        String[] input = reader.nextLine().split(" ");

        createSellerAgent(input[0]);
    }

    public static void createSellerAgent(String itemName){
        try {
            auction = new Auction(null, new Product(itemName), new BigDecimal(1));
            AgentController ag = mainContainer.createNewAgent("agentnick",
                    "pl.edu.agh.szia.agent.SellerAgent", new Object[] {currentUser, itemName, auction});
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static User loginInteractive(){
        System.out.println("Enter username: ");
        Scanner reader = new Scanner(System.in);
        String[] input = reader.nextLine().split(" ");

        String username = input[0];

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
        String[] input = reader.nextLine().split(" ");

        createBuyerAgent(new BigDecimal(input[0]));
    }

    public static void createBuyerAgent(BigDecimal limit){
        try {
            AgentController ag = mainContainer.createNewAgent(currentUser.getUsername(),
                    "pl.edu.agh.szia.agent.BuyerAgent", new Object[] {auction, limit});
            ag.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
