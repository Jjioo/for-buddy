package test1;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class VowelCountServer extends Agent {
    private static final String SERVICE_TYPE = "vowel-count-serve";

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
                // Process the request and send the vowel count back
                Object[] args = null;
                try {
                    args = (Object[]) msg.getContentObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (args != null && args.length == 1) {
                    String word = (String) args[0];
                    int vowelCount = countVowels(word);
                    sendResultToClient(msg.getSender(), Integer.toString(vowelCount));
                } else {
                    System.err.println("Invalid number of arguments. Please provide exactly one value.");
                }
            } else {
                block();
            }
        }

        private int countVowels(String word) {
            // Implement vowel count logic
            int count = 0;
            for (char ch : word.toLowerCase().toCharArray()) {
                if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'|| ch == 'y') {
                    count++;
                }
            }
            return count;
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
