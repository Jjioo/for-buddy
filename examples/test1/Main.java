package test1;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main extends Agent {

    public static void main(String[] args) throws StaleProxyException {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        AgentContainer ac = rt.createMainContainer(p);

        AgentController rmaAgentController = null;
        AgentController snifferAgentController = null;
        AgentController wordServerController = null;    
        AgentController client1AgentController = null;

        AgentController reverseServerController = null;
        AgentController palindromeServerController = null;
        AgentController vowelCountServerController = null;

        try {
            rmaAgentController = ac.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rmaAgentController.start();

            snifferAgentController = ac.createNewAgent("snif", "jade.tools.sniffer.Sniffer", new Object[0]);
            snifferAgentController.start();

            wordServerController = ac.createNewAgent("wordServer", "test1.WordServer", new Object[0]);
            wordServerController.start();

            reverseServerController = ac.createNewAgent("reverseServer", "test1.ReverseServer", new Object[0]);
            reverseServerController.start();

            palindromeServerController = ac.createNewAgent("palindromeServer", "test1.PalindromeServer", new Object[0]);
            palindromeServerController.start();

            vowelCountServerController = ac.createNewAgent("vowelCountServer", "test1.VowelCountServer", new Object[0]);
            vowelCountServerController.start();

            // Sleep to allow agents to initialize
            Thread.sleep(2000);

            client1AgentController = ac.createNewAgent("c1", "test1.Client", new Object[]{"hey"});
            client1AgentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
