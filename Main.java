// University Assigment-Project Members:Eleutheria Koutsochristou,Alexandra Konomi, Vasileios Stagakhs

import java.util.*;
public class Main {
    public static void main(String[] args)
    {
        int N;
        List<Person> people=new ArrayList<Person>();
        Scanner myObj = new Scanner(System.in);
        Scanner obj = new Scanner(System.in);
        System.out.println("Give me the number of people waiting to cross the bridge: ");
        N = myObj.nextInt();
        for(int i=1; i<=N; i++){
            System.out.println("Give me the name of the "+i+" person: ");
            String name= obj.nextLine();
            System.out.println("Give me the time to cross the bridge for the person: "+i);
            int time=myObj.nextInt();
            Person person = new Person(time,name);
            people.add(person);
        }
        State initialState = new State(N,people);   //Initialize the first state
        long start = System.currentTimeMillis();    //Start the counter to check how long the algorithm will take to find the solution(or not find one)
        AStarAlgorithm a = new AStarAlgorithm();
        State terminalState = a.Algorithm(initialState);    //Start the algorithm and return and save the last state
        long end = System.currentTimeMillis();      //Stop the timer
        if(terminalState == null) System.out.println("Could not find a solution.");
        else
        {
            // print the path from beginning to start.
            State temp = terminalState; // begin from the end.
            ArrayList<State> path = new ArrayList<>();
            path.add(terminalState);
            while(temp.getFather() != null) // if father is null, then we are at the root.
            {
                path.add(temp.getFather());
                temp = temp.getFather();
            }
            // reverse the path and print.
            Collections.reverse(path);
            if(N==0) {  // print a proper message in case there were no family members given
                System.out.println("There was no family waiting to cross the bridge...");
            }else{
                System.out.println("Path");
                for(State item: path)
                {
                    item.printState();
                }
                System.out.println();
                System.out.println("The family needed " + terminalState.get_g() + " minutes to cross the bridge to the other side"); //Print the time it took the family to cross the bridge in minutes
                System.out.println("Search time:" + (double)(end - start) / 1000 + " sec.");  // total time of searching in seconds. //Print the time it took the program to find a solution(or not find one)
            }
        }

    }
}
