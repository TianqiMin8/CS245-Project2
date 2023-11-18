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

    public boolean up(){
        return up;
    }
    public int elevatorCurFloor(){
        return elevatorCurFloor;
    }


    
    //off load passenger on the elevaotr first, return it's time
    public List<Integer> offLoad(Elevator e, int tick, String structures){
        List<Integer> leavingPassengers;
        if(structures.equals("array")){leavingPassengers = new ArrayList<Integer>();}
        else{leavingPassengers = new LinkedList();}
        
        //firstElement is the minTime, second is the maxTime, third is the sumTime
        int minTime = Integer.MAX_VALUE;
        int maxTime = 0;
        int sumTime = 0;
        int offloadPassengerNum = 0;
        if(!PassengerElevatorUp.isEmpty()){
            if(up){
                while(elevatorCurFloor == PassengerElevatorUp.peek().endFloor()){
                    //record and change these passengers' leave tick 
                    
                    int tempDuration = tick - PassengerElevatorUp.peek().startTick();
                    minTime = Math.min(minTime, tempDuration);
                    maxTime = Math.max(maxTime, tempDuration);
                    sumTime += tempDuration;
                    offloadPassengerNum ++;
                }
                
            }
            else{
                while(elevatorCurFloor == PassengerElevatorDown.peek().endFloor()){
                    //record these passengers' leave tick
                    int tempDuration = tick - PassengerElevatorDown.peek().startTick();
                    minTime = Math.min(minTime, tempDuration);
                    maxTime = Math.max(maxTime, tempDuration);
                    sumTime += tempDuration;
                    offloadPassengerNum++;
                }
            }
        }
    
        //store all value to the return list
        leavingPassengers.add(minTime);
        leavingPassengers.add(maxTime);
        leavingPassengers.add(sumTime);
        leavingPassengers.add(offloadPassengerNum);
        return leavingPassengers;
    }

    //upload passengers to the elevator
    public void elevatorUpload(Elevator e, Passenger p){
        if(e.up){e.PassengerElevatorUp.add(p);}
        else{e.PassengerElevatorDown.add(p);}
    }

    //judge the elevator's target floor and move the elevator to it
    //after uploading all passengers in this floor to this elevator
    public void move(Elevator e, List<Floor> ListFloor){
        int targetFloor = e.elevatorCurFloor();
        //if there are passenger in the elevator
        if(!e.PassengerElevatorUp.isEmpty() || !e.PassengerElevatorDown.isEmpty()){
            //check if it's larger than 5 floors' travel
            if(e.up){
                targetFloor = Math.min(targetFloor+5,e.PassengerElevatorUp.peek().endFloor());
            }
            else{
                targetFloor = Math.max(targetFloor-5,e.PassengerElevatorUp.peek().endFloor());
            } 
        }  
        //if the elevator is empty now
        else{
            //find a nearest floor with passengers 
            ////////////////////////////////////////////////////later to consider how to flag this passenger or queue in this floor
            //check all floors, from 0 to maxFloor
            for(Floor f : ListFloor){
                if(targetFloor != e.elevatorCurFloor() && f.curFloor()== e.elevatorCurFloor()){
                    break;
                }
                //when the floor is higher than the target floor, once find a floor with passengers, end the loop
                if(targetFloor == e.elevatorCurFloor() && f.curFloor() > e.elevatorCurFloor()){
                    if(!f.PassengerUp().isEmpty() || !f.PassengerDown().isEmpty()){
                        targetFloor = f.curFloor();
                        break;
                    }
                }
                if(!f.PassengerUp().isEmpty() || !f.PassengerDown().isEmpty()){
                    targetFloor = f.curFloor();
                }
            }
            //if no passengers in all floor, stay at the current floor
        }    

        //move the elevator to its targetFloor
        e.elevatorCurFloor = targetFloor;
        return;
    }

    public List<Integer> elevatorTick(Elevator e, String structures, int tick, List<Floor> ListFloor){
        List<Integer> result;
        if(structures.equals("array")){result = new ArrayList<>();}
        else{result = new LinkedList<>();}
        result = offLoad(e, tick, structures);
        
        //should be changed?
        //elevatorUpload(e, e.elevatorCurFloor());

        //move elevator to its target floor
        move(e, ListFloor);

        return result;
    }

}
