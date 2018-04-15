package pl.edu.agh.szia.data;

import jade.core.AID;
import pl.edu.agh.szia.agent.SellerAgent;
import pl.edu.agh.szia.auction.Auction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private final Map<Auction, AID> ownedAuctions;
    private final Map<Auction, AID> participatedAuctions;

    public User(String username) {
        this.username = username;

        ownedAuctions = new HashMap<Auction, AID>();
        participatedAuctions = new HashMap<Auction, AID>();
    }

    public void startNewAuction(Product product, BigDecimal initialPrice) {
        final SellerAgent agent = new SellerAgent(this);
        final Auction auction = new Auction(agent.getAID(), product, initialPrice);

        agent.setTargetAuction(auction);
        ownedAuctions.put(auction, agent.getAID());
    }
}
