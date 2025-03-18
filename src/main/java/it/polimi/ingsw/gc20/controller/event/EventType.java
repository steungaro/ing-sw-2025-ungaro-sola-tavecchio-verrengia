package it.polimi.ingsw.gc20.controller.event;

import jdk.jfr.Event;

import java.io.Serializable;

public class EventType<T extends Event> implements Serializable {}
