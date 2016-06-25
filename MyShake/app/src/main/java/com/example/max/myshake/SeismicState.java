package com.example.max.myshake;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by max on 13.06.16.
 */
public class SeismicState {

    private int numberOfStates = 10;
    private int numberTrueStartes = numberOfStates/2;
    private Queue<Boolean> states = new LinkedList<>();


    public SeismicState(){
        for(int i = 0; i<numberOfStates; ++i){
            states.add(Boolean.FALSE);
        }
    }

    public void resetStates(){
        for(int i = 0; i<numberOfStates; ++i){
            states.add(Boolean.FALSE);
        }
    }

    public boolean isEarthquake(){
        int i = 0;
        for (boolean b : states){
            if(b){++i;}
        }
        return i>=numberTrueStartes;
    }

    public void addState(boolean b){
        states.add(b);
        states.remove();
    }
}
