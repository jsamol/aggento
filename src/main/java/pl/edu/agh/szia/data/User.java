package pl.edu.agh.szia.data;

import jade.core.AID;
import pl.edu.agh.szia.agent.SellerAgent;
import pl.edu.agh.szia.auction.Auction;
import pl.edu.agh.szia.utils.AuctionManager;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
