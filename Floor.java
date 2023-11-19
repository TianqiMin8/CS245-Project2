import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.io.*;
import java.util.List;
import java.util.LinkedList;

class Floor{
    //store the current floor
    private int curFloor;
    //store the generated passenger on this floor
    private Deque<Passenger> PassengerUp;
    private Deque<Passenger> PassengerDown;
    //necessary?
    private Deque<Elevator> ElevatorUp;
    private Deque<Elevator> ElevatorDown;

    public Floor(Deque<Passenger> PassengerUp, Deque<Passenger> PassengerDown, 
    int curFloor, Deque<Elevator> ElevatorUp, Deque<Elevator> ElevatorDown){
        this.curFloor = curFloor;
        this.PassengerUp = PassengerUp;
        this.PassengerDown = PassengerDown;
        this.ElevatorUp = ElevatorUp;
        this.ElevatorDown = ElevatorDown;
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

            //testing
            //System.out.println(tick +" "+Floor.curFloor());
            //store the new generated passenger either in a up queue or in a down queue
            if((floorToGo - curFloor)>0){PassengerUp.add(p);}
            else{PassengerDown.add(p);}
        }
    }

    //check passenger can get on the elevator. if, use elevator upload
    public void floorUpload(Floor f){
        //check if FloorupQueue is not empty, check every elevator on this floor
        if(!f.PassengerUp.isEmpty()){
            if(!f.ElevatorUp.isEmpty()){
                (f.ElevatorUp.peek()).elevatorUpload(f.ElevatorUp.peek(), f.PassengerUp.pop());
            }
        }
        //check if FloordownQueue is not empty, check every elevator on this floor
        if(!f.PassengerDown.isEmpty()){
            if(!f.ElevatorDown.isEmpty()){
                //need to add a judge to make sure the elevator is not overloded
                (f.ElevatorDown.peek()).elevatorUpload(f.ElevatorDown.peek(), f.PassengerDown.pop());
            }
        }
    
    } 

    //check which floor this elevator is in, store that elevator to this floor's queue
    public void storeElevatorInThisFloor(Elevator e){
        int elevatorCurFloor = e.elevatorCurFloor();
        //check every elevator, check their floor, store the elevator to the floor's queue  
        if(elevatorCurFloor == curFloor){
            ElevatorDown.add(e);
        }
    }

    

    public void floorTick(int floorNum, int tick, double passengers, Floor f, List<Elevator> ListElevator){
        generatePassenger(f, floorNum, passengers, tick);
        for(Elevator e : ListElevator){
            storeElevatorInThisFloor(e);
        }
        floorUpload(f);
    }
}