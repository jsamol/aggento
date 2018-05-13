package pl.edu.agh.szia.server.agent.server;

import jade.core.Agent;
import pl.edu.agh.szia.server.AuctionSystem;
import pl.edu.agh.szia.server.agent.server.behaviour.ListenCyclicBehaviour;

public class ServerAgent extends Agent {

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args.length > 0 && args[0] instanceof AuctionSystem) {
            AuctionSystem auctionSystem = (AuctionSystem) args[0];
            addBehaviour(new ListenCyclicBehaviour(auctionSystem));
        }
    }
}
