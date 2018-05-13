package pl.edu.agh.szia.data;

import jade.core.AID;
import pl.edu.agh.szia.server.auction.Auction;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private Auction activeAuction;
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

    public Auction getActiveAuction() {
        return activeAuction;
    }

    public void setActiveAuction(Auction activeAuction) {
        this.activeAuction = activeAuction;
    }

    public void addOwnedAuction(Auction auction){
        ownedAuctions.put(auction, auction.getOwnerAID());
    }

    public void addParticipatedAuction(Auction auction, AID AID){
        participatedAuctions.put(auction, AID);
    }
}
