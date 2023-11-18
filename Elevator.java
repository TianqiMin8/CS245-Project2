import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.io.*;
import java.util.List;
import java.util.LinkedList;

class Elevator{
    private boolean up;
    private int elevatorIndex;
    private int elevatorCurFloor;

    private PriorityQueue<Passenger> PassengerElevatorUp = new PriorityQueue<>(); //minHeap
    private PriorityQueue<Passenger> PassengerElevatorDown = new PriorityQueue<>(Collections.reverseOrder()); //maxHeap
    
    //store the target floors
    private PriorityQueue<Integer> targetFloorsUp = new PriorityQueue<>();
    private PriorityQueue<Integer> targetFloorsDown = new PriorityQueue<>(Collections.reverseOrder());

    //load and unload only elevator 
    //in the floor down

    //need the new function to find the target floor when the elevator is empty
    public Elevator(boolean up, int elevatorIndex, int elevatorCurFloor, 
    PriorityQueue<Passenger> PassengerElevatorUp, PriorityQueue<Passenger> PassengerElevatorDown,
    PriorityQueue<Integer> targetFloorsUp, PriorityQueue<Integer> targetFloorsDown){
        this.up = up;
        this.elevatorIndex = elevatorIndex;   
        this.elevatorCurFloor = elevatorCurFloor;    
        //this.elevatorPassenger = elevatorPassenger; 
        this.PassengerElevatorUp = PassengerElevatorUp;
        this.PassengerElevatorDown = PassengerElevatorDown;
        this.targetFloorsUp = targetFloorsUp;
        this.targetFloorsDown = targetFloorsDown;
    }

    public int elevatorCurFloor(){
        return elevatorCurFloor;
    }

    
    //off load passenger on the elevaotr first, return it's time
    public List<Integer> offLoad(elevator e, int tick, String structures, List<Integer> Time){
        List<Integer> leavingPassengers;
        if(structures.equals("array")){leavingPassengers = new ArrayList<Integer>();}
        else{leavingPassengers = new LinkedList();}
        
        //firstElement is the minTime, second is the maxTime, third is the sumTime
        int minTime = Time.get(0);
        int maxTime = Time.get(1);
        int sumTime = Time.get(2);
        
        if(up){
            while(elevatorCurFloor == PassengerElevatorUp.peek().endFloor()){
                //PassengerElevatorUp.poll();
                //record and change these passengers' leave tick 
                //PassengerElevatorUp.peek().endTick = tick;
                int tempDuration = tick - PassengerElevatorUp.peek().startTick();
                minTime = Math.min(minTime, tempDuration);
                maxTime = Math.max(maxTime, tempDuration);
                sumTime += tempDuration;
                //leavingPassengers.add(PassengerElevatorUp.poll());
            }
            
        }
        else{
            while(elevatorCurFloor == PassengerElevatorDown.peek().endFloor()){
                //record these passengers' leave tick
                int tempDuration = tick - PassengerElevatorDown.peek().startTick();
                minTime = Math.min(minTime, tempDuration);
                maxTime = Math.max(maxTime, tempDuration);
                sumTime += tempDuration;
                //leavingPassengers.add(PassengerElevatorDown.poll());
            }
        }
        //store all value to the return list
        leavingPassengers.add(minTime);
        leavingPassengers.add(maxTime);
        leavingPassengers.add(sumTime);
        return leavingPassengers;
    }

    //upload passengers on the elevator
    public void elevatorUpload(Elevator e, Passenger p){
        if(e.up){e.PassengerElevatorUp.add(p);}
        else{e.PassengerElevatorDown.add(p);}
    }

}
