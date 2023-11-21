import java.util.*;

class Floor{
    //store the current floor
    private int curFloor;
    //store the generated passenger on this floor
    private Deque<Passenger> PassengerUp;
    private Deque<Passenger> PassengerDown;

    public Floor(Deque<Passenger> PassengerUp, Deque<Passenger> PassengerDown, 
    int curFloor){
        this.curFloor = curFloor;
        this.PassengerUp = PassengerUp;
        this.PassengerDown = PassengerDown;

    }

    public int curFloor(){return curFloor;}
    public Deque<Passenger> PassengerUp(){return PassengerUp;}
    public Deque<Passenger> PassengerDown(){return PassengerDown;}



    //generate passenger in this floor
    public void generatePassenger(Floor Floor, int floorNum, double passengers, int tick){
        Random r = new Random();
        //generate passenger in this floor
        double a = Math.random();  


        if(a <= passengers){
            int floorToGo = r.nextInt(floorNum);
            while(Floor.curFloor == floorToGo){floorToGo = r.nextInt(floorNum);}
            Passenger p = new Passenger(Floor.curFloor,floorToGo, tick, 0);
            //store the new generated passenger either in a up queue or in a down queue
            if((floorToGo - curFloor)>0){Floor.PassengerUp.add(p);}
            else{Floor.PassengerDown.add(p);}
        }
    }
}