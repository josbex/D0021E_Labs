package Sim;

// Class used to implement link

public abstract class Link extends SimEnt {

    private static int counter;

    protected SimEnt _connectorA=null;
    protected SimEnt _connectorB=null;


    protected Link() {
        super();
        this.identifierString = "LINK " + Link.counter;
        Link.counter++;
    }

    // Connects the link to some simulation entity like
    // a node, switch, router etc.

    public void setConnector(SimEnt connectTo)
    {
        if (_connectorA == null)
            _connectorA=connectTo;
        else
            _connectorB=connectTo;
    }
}
