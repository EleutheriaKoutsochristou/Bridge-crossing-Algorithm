public class Person {
    public int time;
    public String name;
    Person(int time,String name){
        this.time=time;
        this.name=name;
    }

    String getName(){
        return name;
    }
    int getTime(){
        return time;
    }
}
