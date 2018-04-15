package pl.edu.agh.szia.auction;

import jade.core.AID;
import pl.edu.agh.szia.data.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Auction {

    private AID ownerAID;
    private List<AID> participants;

    private Product product;
    private BigDecimal currentPrice;

    public Auction(AID ownerAID, Product product, BigDecimal currentPrice) {
        this.ownerAID = ownerAID;
        this.product = product;
        this.currentPrice = currentPrice;

        participants = new ArrayList<AID>();
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
}
