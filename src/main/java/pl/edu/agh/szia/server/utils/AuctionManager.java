package pl.edu.agh.szia.server.utils;

import pl.edu.agh.szia.server.auction.Auction;

import java.util.ArrayList;
import java.util.List;

public class AuctionManager {

    private static AuctionManager instance;
    private final List<Auction> auctions;

    private AuctionManager() {
        auctions = new ArrayList<Auction>();
    }

    public static AuctionManager instanceOf() {
        if (instance == null) {
            instance = new AuctionManager();
        }

        return instance;
    }

    public void addAuction(Auction auction) {
        auctions.add(auction);
    }

    public List<Auction> getAuctions() {
        return auctions;
    }
}
