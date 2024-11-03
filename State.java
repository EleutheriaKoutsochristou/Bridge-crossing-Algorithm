import java.util.*;
import java.lang.*;

public class State implements Comparable<State> {

    private int N = 0;
    public State father = null;
    private String lamp = "right";  //variable to know on which side the lamp is on default:right side since thats where our family is located at first
    private int g;  //total score from root-state
    private int h;  //heuristic value
    private int f;  //total score for each state
    private List<Person> left_side=new ArrayList<Person>();     //a list containing the people at the left side of the bridge for each state
    private List<Person> right_side=new ArrayList<Person>();    //a list containing the people at the right side of the bridge for each state

    State(int N,List<Person> people){  //constructor for the initial state, we place everyone on the right_side list since everyone is on the right side of the bridge at first
        for(int i=0;i<N;i++){
            this.right_side.add(people.get(i));
        }
        this.N = N;
    }

    //Setters and getters
    State getFather(){return this.father;}

    void setFather(State father){this.father=father;}

    int get_g(){return g;}

    int get_h(){return h;}

    public int get_f(){return f;}

    List<Person> get_right_side(){return right_side;}

    List<Person> get_left_side(){return left_side;}

    State (List<Person> chL, List<Person> chR,String lamp){  //constructor for each child state we create keeping track of the people on the left/right side and the position of the lamp
        this.left_side = chL;
        this.right_side = chR;
        this.lamp = lamp;
    }

    void printState(){      //print each state so the user can see visually how it looks like
        String names=" ";
        for(int i =0; i < this.left_side.size(); i++){
            names = names + this.left_side.get(i).name + " ";
        }
        names = names + "___ ";
        for(int i =0; i < this.right_side.size(); i++){
            names = names + this.right_side.get(i).name + " ";
        }
        System.out.println(names);
    }

    public boolean isFinal(){       //check if the state is final( if everyone passed so no one is on the right side)
        if (right_side.size() == 0){
            return true;
        }
        return false;
    }

    ArrayList<State> getChildren(){        //function to generate children for each current state and calculate their f score
        ArrayList<State> children= new ArrayList<>();  //initialize a list to add the children we create
        //We use these 2 variables to calculate the g score(distance from the root)When one person passes the g=firstperson
        // and when 2 people pass g is equal to the max time between firstperson and secondperson
        int firstperson;        //variable to keep track of the time the first person that passed the bridge took.
        int secondperson;       //variable to keep track of the time the second person that passed the bridge took
        if (lamp=="left"){      //If the lamp is on the left side we move from the left to the right side
            for(int i=0; i< left_side.size(); i++){         //we iterate between the people on the left side to create the children
                ArrayList<Person> chL= new ArrayList<>(left_side);  //We make a new list for the people on the left/right side to use to create the child
                ArrayList<Person> chR= new ArrayList<>(right_side);
                Person p =left_side.get(i);   //we take the i-th person from the left side
                firstperson=p.getTime();
                chL.remove(p);   //we remove the i-th person from the left side
                chR.add(p);         // and move him to the right side
                State child = new State(chL,chR, "right");  //We create the first child with 1 person passing the bridge
                child.setFather(this);   //Set the father of the child we created
                child.g=child.CalculateG(firstperson,child.getFather());  //call the cost functions to get the f score
                child.h=child.CalculateH();
                child.f=child.g+child.h;
                children.add(child);    //add child to the children list
                for(int j=chL.size(); j>i ; j--) {      //we iterate the remaining people on the right side backwards so we can get all the pairs without duplicates
                    //We do the same procedure for the second child
                    ArrayList<Person> chL2= new ArrayList<>(chL);
                    ArrayList<Person> chR2= new ArrayList<>(chR);
                    Person P2 =chL2.get(j-1);
                    secondperson=P2.getTime();
                    chL2.remove(P2);
                    chR2.add(P2);
                    State child2 = new State(chL2, chR2, "right");
                    child2.setFather(this);
                    child2.g=child2.CalculateG(Math.max(firstperson,secondperson),child.getFather());
                    child2.h=child2.CalculateH();
                    child2.f=child2.g+child2.h;
                    children.add(child2);
                }
            }
        }else{ //If the lamp is on the right side we move from the right to the left side
            //Similarly with the lamp being on the left side we find the children of the current state and add them in a list
            for(int i=0; i< right_side.size(); i++){
                ArrayList<Person> chL= new ArrayList<>(this.left_side);
                ArrayList<Person> chR= new ArrayList<>(this.right_side);
                Person p =right_side.get(i);
                firstperson=p.getTime();
                chR.remove(p);
                chL.add(p);
                State child = new State(chL, chR, "left");
                child.setFather(this);
                child.g=child.CalculateG(firstperson,child.getFather());
                child.h=child.CalculateH();
                child.f=child.g+child.h;
                children.add(child);
                for(int j=chR.size(); j>i ; j--) {
                    ArrayList<Person> chL2= new ArrayList<>(chL);
                    ArrayList<Person> chR2= new ArrayList<>(chR);
                    Person P2 = chR.get(j-1);
                    secondperson=P2.getTime();
                    chR2.remove(P2);
                    chL2.add(P2);
                    State child2 = new State(chL2, chR2, "left");
                    child2.setFather(this);
                    child2.g=child2.CalculateG(Math.max(firstperson,secondperson),child.getFather());
                    child2.h=child2.CalculateH();
                    child2.f=child2.g+child2.h;
                    children.add(child2);
                }
            }
        }
        return children;  //return all the children of the current state as a list
    }

    int CalculateG(int time, State father){  //calculate the distance from the root by adding the g score of the father
                                            // and the time the slowest person(or the only person) passing took
        if(this.father!=null) {
            g=this.father.g+time;
        } else {
           g=0;
        }
        return g;


    }
    //calculating heuristic value by adding up the time of each person on the right side
    int CalculateH(){
        for(int i =0; i < right_side.size(); i++){
            h= h + right_side.get(i).time;
        }
        return h;
    }
    @Override  //compare the total score of each state to sort it later at the frontier
    public int compareTo(State s) {
        return Double.compare(this.f, s.f); // compare based on the heuristic score.
    }
}

