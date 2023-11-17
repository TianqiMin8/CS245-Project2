import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.io.*;
import java.util.List;
import java.util.LinkedList;

public class Project2Final {
    public static void main(String[] args) throws Exception{
        //Requirement 1

        //judge if the properties file exist
        Properties p1 = new Properties();
        String structures;
        int floors, elevators, elevatorCapacity, duration;
        double passengers;
        try (FileInputStream input = new FileInputStream(args[0])) {
            // Load properties from the file
            p1.load(input);
            structures = p1.getProperty("structures");
            floors = Integer.parseInt(p1.getProperty("floors"));
            passengers = Double.parseDouble(p1.getProperty("passengers"));
            elevators = Integer.parseInt(p1.getProperty("elevators"));
            elevatorCapacity = Integer.parseInt(p1.getProperty("elevatorCapacity"));
            duration = Integer.parseInt(p1.getProperty("duration"));
        }
        catch(Exception e) {
            // Handle the case where the file is not found or an error occurs
            System.out.println("Property file not found, use default one.");
            structures = "linked";
            floors = 32;
            passengers = 0.03;
            elevators = 1;
            elevatorCapacity = 10;
            duration = 500;
        }


        //Requirement 2

        //create a list of floors, elevators
        List<Floor> ListFloor;
        List<Elevator> ListElevator;
        if(structures.equals("array")){      
            ListFloor= new ArrayList<>();
            ListElevator = new ArrayList<>();
        }
        else{      
            ListFloor= new LinkedList<>();
            ListElevator = new LinkedList<>();
        }

        //store all floor object to a list
        //i is the current Floor
        for(int i=0; i<floors; i++){
            //store an empty queue in that floor
            Deque<Passenger> PassengerUp;
            Deque<Passenger> PassengerDown;
            Deque<Elevator> ElevatorUp;
            Deque<Elevator> ElevatorDown;
            if(structures.equals("array")){
                PassengerUp = new ArrayDeque<Passenger>();
                PassengerDown = new ArrayDeque<Passenger>();
                ElevatorUp = new ArrayDeque<Elevator>();
                ElevatorDown = new ArrayDeque<Elevator>();
            }
            else{
                PassengerUp = new ConcurrentLinkedDeque<Passenger>();
                PassengerDown = new ConcurrentLinkedDeque<Passenger>();
                ElevatorUp = new ConcurrentLinkedDeque<Elevator>();
                ElevatorDown = new ConcurrentLinkedDeque<Elevator>();
            }

            Floor tempFloor = new Floor(PassengerUp, PassengerDown, i, ElevatorUp, ElevatorDown);
            ListFloor.add(tempFloor);
        }

        //store all elevator objects to a list
        for(int i=0; i<elevators; i++){
            PriorityQueue<Passenger> PassengerElevatorUp= new PriorityQueue<>();
            PriorityQueue<Passenger> PassengerElevatorDown= new PriorityQueue<>(Collections.reverseOrder());
            PriorityQueue<Integer> targetFloorsUp= new PriorityQueue<>();
            PriorityQueue<Integer> targetFloorsDown= new PriorityQueue<>(Collections.reverseOrder());
            Elevator tempElevator = new Elevator(true, i, 0, 
            PassengerElevatorUp, PassengerElevatorDown, targetFloorsUp, targetFloorsDown);
            ListElevator.add(tempElevator);
        }

        

    }
}

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

    public void floorTick(){

    }

    
}

class Elevator{
    private boolean up;
    private int elevatorIndex;
    private int elevatorCurFloor;
    private PriorityQueue<Passenger> PassengerElevatorUp = new PriorityQueue<>(); //minHeap
    
    private PriorityQueue<Passenger> PassengerElevatorDown = new PriorityQueue<>(Collections.reverseOrder()); //maxHeap
    
    //store the target floors
    private PriorityQueue<Integer> targetFloorsUp = new PriorityQueue<>();
    private PriorityQueue<Integer> targetFloorsDown = new PriorityQueue<>(Collections.reverseOrder());

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

    //off load passenger on the elevaotr first
    public Deque offLoad(elevator e, int tick, String structures){
        Deque leavingPassengers;
        if(structures.equals("array")){leavingPassengers = new ArrayDeque<Passenger>();}
        else{leavingPassengers = new ConcurrentLinkedDeque();}

        if(up){
            while(elevatorCurFloor == PassengerElevatorUp.peek().endFloor){
                //PassengerElevatorUp.poll();
                //record these passengers' leave tick 
                PassengerElevatorUp.peek().endTick = tick;
                leavingPassengers.add(PassengerElevatorUp.poll());
            }
            
        }
        else{
            while(elevatorCurFloor == PassengerElevatorDown.peek().endFloor){
                //record these passengers' leave tick
                PassengerElevatorDown.peek().endTick = tick;
                leavingPassengers.add(PassengerElevatorUp.poll());
            }
        }
        return leavingPassengers;
    }

    //upload passengers on the elevator
    public void elevatorUpload(Elevator e, Passenger p){
        if(e.up){e.PassengerElevatorUp.add(p);}
        else{e.PassengerElevatorDown.add(p);}
    }



}


class Passenger{
    private int startFloor;
    private int endFloor; 
    private int startTick;
    private int endTick;

    public Passenger(int startFloor, int endFloor, int startTick, int endTick){
        this.startFloor = startFloor;
        this.endFloor = endFloor;
        this.startTick = startTick;
        this.endTick = endTick;
    }

    public int endTick(){
        return endTick;
    }
}