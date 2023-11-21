import java.util.*;
import java.util.List;
import java.util.LinkedList;

class Elevator{
    private boolean up;
    //private int elevatorIndex;
    private int elevatorCurFloor;

    private PriorityQueue<Passenger> PassengerElevatorUp = new PriorityQueue<>(); //minHeap
    private PriorityQueue<Passenger> PassengerElevatorDown = new PriorityQueue<>(Collections.reverseOrder()); //maxHeap
    
    //store the target floors
    // private PriorityQueue<Integer> targetFloorsUp = new PriorityQueue<>();
    // private PriorityQueue<Integer> targetFloorsDown = new PriorityQueue<>(Collections.reverseOrder());

    //load and unload only elevator 
    //in the floor down

    public Elevator(boolean up, int elevatorIndex, int elevatorCurFloor, 
    PriorityQueue<Passenger> PassengerElevatorUp, PriorityQueue<Passenger> PassengerElevatorDown,
    PriorityQueue<Integer> targetFloorsUp, PriorityQueue<Integer> targetFloorsDown){
        this.up = up;
        //this.elevatorIndex = elevatorIndex;   
        this.elevatorCurFloor = elevatorCurFloor;    
        this.PassengerElevatorUp = PassengerElevatorUp;
        this.PassengerElevatorDown = PassengerElevatorDown;
        // this.targetFloorsUp = targetFloorsUp;
        // this.targetFloorsDown = targetFloorsDown;
    }

    public boolean up(){
        return up;
    }
    public int elevatorCurFloor(){
        return elevatorCurFloor;
    }

    public PriorityQueue<Passenger> PassengerElevatorUp(){
        return PassengerElevatorUp;
    }

    public PriorityQueue<Passenger> PassengerElevatorDown(){
        return PassengerElevatorDown;
    }


    
    //off load passenger on the elevator first, return it's time
    public List<Integer> offLoad(Elevator e, int tick, String structures){
        List<Integer> leavingPassengers;
        if(structures.equals("array")){leavingPassengers = new ArrayList<Integer>();}
        else{leavingPassengers = new LinkedList<>();}
        
        //firstElement is the minTime, second is the maxTime, third is the sumTime
        int minTime = Integer.MAX_VALUE;
        int maxTime = 0;
        int sumTime = 0;
        int offloadPassengerNum = 0;
        if(!PassengerElevatorUp.isEmpty()){
            if(up){
                if(elevatorCurFloor == PassengerElevatorUp.peek().endFloor()){
                    //record and change these passengers' leave tick 
                    int tempDuration = tick - PassengerElevatorUp.peek().startTick();
                    Passenger t = PassengerElevatorUp.peek();
                    System.out.println("passenger floor up: "+ t.startFloor() +"-"
                    + t.endFloor() + " "+ tempDuration);
                    PassengerElevatorUp.poll();
                    minTime = Math.min(minTime, tempDuration);
                    maxTime = Math.max(maxTime, tempDuration);
                    sumTime += tempDuration;
                    offloadPassengerNum ++;
                }
                
            }
            else{
                if(elevatorCurFloor == PassengerElevatorDown.peek().endFloor()){
                    //record these passengers' leave tick
                    Passenger t = PassengerElevatorDown.peek();
                    int tempDuration = tick - PassengerElevatorDown.peek().startTick();
                    System.out.println("passenger floor down: "+ t.startFloor() +"-"
                    + t.endFloor() + " "+ tempDuration);
                    PassengerElevatorDown.poll();
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

    //upload passengers to the elevator, use that function to Floor
    //public void elevatorUpload(Elevator e, Passenger p){
    public void elevatorUpload(Elevator e, Floor f, int elevatorCapacity, List<Floor> ListFloor){
        if(e.PassengerElevatorUp.isEmpty() && e.PassengerElevatorDown.isEmpty()){
            //choose passenger waiting the longest time, chose it as it's up or down
            if(!f.PassengerUp().isEmpty() && !f.PassengerDown().isEmpty()){
                if(f.PassengerUp().peek().startTick() < f.PassengerDown().peek().startTick()){
                    e.up = true;}
                else{e.up = false;}

            }
            else if(!f.PassengerUp().isEmpty()){
                while(!f.PassengerUp().isEmpty()&& e.PassengerElevatorUp.size()<elevatorCapacity){
                    e.PassengerElevatorUp.add(f.PassengerUp().poll());
                }
                e.up = true;
            }
            else if(!f.PassengerDown().isEmpty()){
                while(!f.PassengerDown().isEmpty()&& e.PassengerElevatorDown.size()<elevatorCapacity){
                    e.PassengerElevatorDown.add(f.PassengerDown().poll());
                }
                e.up = false;
            }
            //this floor is empty, not upload any passengers
            else{
                return;
            }            
        }

        if(e.up){
            while(!f.PassengerUp().isEmpty() && e.PassengerElevatorUp.size()<elevatorCapacity){
                e.PassengerElevatorUp.add(f.PassengerUp().pop());
            }
        }

        else{
            while(!f.PassengerDown().isEmpty() && e.PassengerElevatorDown.size()<elevatorCapacity){
                e.PassengerElevatorDown.add(f.PassengerDown().pop());
            }
        }

    }

    //judge the elevator's target floor and move the elevator to it
    //after uploading all passengers in this floor to this elevator
    public void move(Elevator e, List<Floor> ListFloor){
        int targetFloor = e.elevatorCurFloor();

        //if there are passenger in the elevator
        //check if it's larger than 5 floors' travel
        if(e.up && !e.PassengerElevatorUp.isEmpty()){
            targetFloor = Math.min(targetFloor+5,e.PassengerElevatorUp.peek().endFloor());
            //System.out.println("Up "+ targetFloor);
        }
        ///////////////////////////////////////////////////////////////////////////////////
        else if(!e.up && !e.PassengerElevatorDown.isEmpty()){
            System.out.println("Down "+ (targetFloor-5));
            targetFloor = Math.max(targetFloor-5,e.PassengerElevatorDown.peek().endFloor());
            //System.out.println("Down "+ targetFloor);
        }  
        //if the elevator is empty now
        else{
            //find a nearest floor with passengers 
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

    //in odd tick, elevator upload or download passengers
    //in even tick, elevator move
    public List<Integer> elevatorTick(Elevator e, String structures, int tick, List<Floor> ListFloor, int elevatorCapacity){
        List<Integer> result;
        if(structures.equals("array")){result = new ArrayList<>();}
        else{result = new LinkedList<>();}
        result = offLoad(e, tick, structures);
        
        //should be changed?
        for(Floor f : ListFloor){
            e.elevatorUpload(e, f, elevatorCapacity,ListFloor);
        }

        //move elevator to its target floor
        move(e, ListFloor);

        return result;
    }

}
