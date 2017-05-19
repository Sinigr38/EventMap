package com.sinigr.eventmap;

public class SimpleNode {
    boolean visible = true;
    int value;

    int visited = 0;
    SimpleNode in1, in2;
    SimpleNode out1, out2;
    double baseProb, tempProb, resultProb, variantProb;
    String type;

    public SimpleNode(Node node) {
        type = node.type;
        if(node instanceof Event) {
            Event event = (Event)node;
            baseProb = event.probability;
        } else {
            if (type.equals("or")) baseProb = 2D/3D*100;
            else if(type.equals("xor")) baseProb = 50;
            else baseProb = 100;
        }
    }
}
