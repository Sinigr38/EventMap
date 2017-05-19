package com.sinigr.eventmap;

class Event extends Node {

    public String name;
    String description;
    int probability;

    Event(Node tmp) {
        super(tmp);
        name = "name";
        description = "description";
        probability = 100;
    }
}
