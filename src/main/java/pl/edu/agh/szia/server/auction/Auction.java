package pl.edu.agh.szia.server.auction;

import jade.core.AID;
import pl.edu.agh.szia.data.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Auction {

    private AID ownerAID;
    private String winningBidder;
    private List<AID> participants;
    private Integer id;
    private Long endTime;

    private Product product;
    private BigDecimal currentPrice;

    public Auction(AID ownerAID, Product product, BigDecimal currentPrice, Integer id, Long endTime) {
        this.ownerAID = ownerAID;
        this.product = product;
        this.currentPrice = currentPrice;
        this.id = id;
        this.endTime = endTime;
        this.winningBidder = "";

        participants = new ArrayList<AID>();
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "\n" +
                "ItemName: " + this.product.getName() + "\n" +
                "CurrentPrice: " + this.currentPrice + "\n" +
                "Currently winning bidder: " + this.winningBidder + "\n" +
                "Auction ends: " + new Date(this.endTime) + "\n";
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void raisePrice(BigDecimal bidValue) {
        if (bidValue.compareTo(currentPrice) <= 0) {
            throw new IllegalArgumentException();
        }

        currentPrice = bidValue;
    }

    public AID getOwnerAID() {
        return ownerAID;
    }

    public void setOwnerAID(AID ownerAID) {
        this.ownerAID = ownerAID;
    }

    public String getWinningBidder() {
        return winningBidder;
    }

    public void setWinningBidder(String winningBidder) {
        this.winningBidder = winningBidder;
    }

    public List<AID> getParticipants() {
        return participants;
    }

    public void setParticipants(List<AID> participants) {
        this.participants = participants;
    }

    public void addPartticipant(AID participant){
        this.participants.add(participant);
    }

    public void printAuction(){
        System.out.println("ID: " + this.id);
        System.out.println("ItemName: " + this.product.getName());
        System.out.println("CurrentPrice: " + this.currentPrice);
        System.out.println("Currently winning bidder: " + this.winningBidder);
        System.out.println("Auction ends: " + new Date(this.endTime));
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }
}
