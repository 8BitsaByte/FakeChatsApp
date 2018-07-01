package com.eL.FakeChats;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 7/27/2017.
 */
public class FakeChatClass implements Serializable {
    int id; // 0 for user 1 for friend
    String message;
    ArrayList<Integer> smileyPositions,smileyTypes;

    public FakeChatClass(int id, String message, ArrayList<Integer> smileyPositions, ArrayList<Integer> smileyTypes) {
        this.id = id;
        this.message = message;
        this.smileyPositions = new ArrayList<>(smileyPositions);
        this.smileyTypes = new ArrayList<>(smileyTypes);
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Integer> getSmileyPositions() {
        return smileyPositions;
    }

    public ArrayList<Integer> getSmileyTypes() {
        return smileyTypes;
    }
}
