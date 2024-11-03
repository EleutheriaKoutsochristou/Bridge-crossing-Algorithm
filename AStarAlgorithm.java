import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.*;
public class AStarAlgorithm {

    private ArrayList<State> frontier;  //Create a list to store our frontier
    private ArrayList<State> closedSet; //Create a list to store our closedSet


    AStarAlgorithm()
    {
        this.frontier = new ArrayList<>();
        this.closedSet = new ArrayList<>();
    }

    State Algorithm(State initialState){
        if(initialState.isFinal()){   //If the first state the user gave is final return it
            return initialState;
        }
        // step 1: put initial state in the frontier.
        this.frontier.add(initialState);
        // step 2: check for empty frontier.
        while(this.frontier.size() > 0){
            State currentState = this.frontier.remove(0);   //Take the first element from frontier and set it as our current state
            if(currentState.isFinal()) return currentState;     //If our current state is final we found a solution so we return it
            if(!this.closedSet.contains(currentState))
            {
                this.closedSet.add(currentState);       //Add the current state at the closedSet so we know we checked its children
                this.frontier.addAll(currentState.getChildren());  //Get the children of the current state by calling the function getChildren()
                Collections.sort(this.frontier); // sort the frontier based on the final score(f) to get the best child to set as our current state
            }
        }
        return null;
    }
}


