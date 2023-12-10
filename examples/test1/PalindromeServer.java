package test1;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class PalindromeServer extends Agent {
    private static final String SERVICE_TYPE = "palindrome-serve";

    protected void setup() {
        // Register the service in the DF
        try {
            registerService(SERVICE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Listen for incoming messages
        addBehaviour(new ReceiveRequestBehaviour());
    }

    private void registerService(String serviceType) throws Exception {
        // Register the service in the DF
        jade.domain.DFService.register(this, createDFAgentDescription(serviceType));
    }

    private jade.domain.FIPAAgentManagement.DFAgentDescription createDFAgentDescription(String serviceType) {
        jade.domain.FIPAAgentManagement.DFAgentDescription dfd = new jade.domain.FIPAAgentManagement.DFAgentDescription();
        dfd.setName(getAID());
        jade.domain.FIPAAgentManagement.ServiceDescription sd = new jade.domain.FIPAAgentManagement.ServiceDescription();
        sd.setType(serviceType);
        sd.setName(getLocalName() + "-" + serviceType);
        dfd.addServices(sd);
        return dfd;
    }

    private class ReceiveRequestBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            // Listen for REQUEST messages
            ACLMessage msg = receive();
            if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                // Process the request and send whether the word is a palindrome
                Object[] args = null;
                try {
                    args = (Object[]) msg.getContentObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (args != null && args.length == 1) {
                    String word = (String) args[0];
                    boolean isPalindrome = checkPalindrome(word);
                    sendResultToClient(msg.getSender(), Boolean.toString(isPalindrome));
                } else {
                    System.err.println("Invalid number of arguments. Please provide exactly one value.");
                }
            } else {
                block();
            }
        }

        private boolean checkPalindrome(String word) {
            // Implement palindrome check logic
            String reversedWord = new StringBuilder(word).reverse().toString();
            return word.equals(reversedWord);
        }

        private void sendResultToClient(jade.core.AID clientAID, String result) {
            // Send the result back to the ClientAgent
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(clientAID);
            message.setContent(result);
            myAgent.send(message);
        }
    }
}
