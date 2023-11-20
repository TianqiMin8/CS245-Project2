import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.io.*;
import java.util.List;
import java.util.LinkedList;


//simulate the elevator runninng
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
        List<Integer> allTime;
        if(structures.equals("array")){      
            ListFloor= new ArrayList<>();
            ListElevator = new ArrayList<>();
            allTime = new ArrayList<>();
        }
        else{      
            ListFloor= new LinkedList<>();
            ListElevator = new LinkedList<>();
            allTime = new LinkedList<>();
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

        //initialize time storer
        //firstElement is the minTime, second is the maxTime, 
        //third is the sumTime, forth is the passenger number
        allTime.add(Integer.MAX_VALUE);
        allTime.add(0);
        allTime.add(0);
        allTime.add(0);
        //System.out.println("floors: "+floors);

        //simulate in the duration time, what the floors and elevators would do
        Project2Final project2 = new Project2Final();
        for(int t=0; t<duration; t++){
            //////////////////////////////////////////////////////testing
            //System.out.println("tick: "+t);
            allTime = project2.oneTick(allTime, ListFloor, ListElevator, structures, floors, t, passengers, elevatorCapacity);
        }


        //Requirement 3
        if(allTime.get(3) == 0){allTime.set(3,1);}
        System.out.println("Average length of time: "+allTime.get(2)/allTime.get(3));
        System.out.println("Longest time: "+allTime.get(1));
        System.out.println("Shortest time: "+allTime.get(0));
    }

    //use oneTick to find what
    public List<Integer> oneTick(List<Integer> allTime, List<Floor> floors, 
    List<Elevator> elevators, String structures, int floorNum, int tick, 
    double passengers, int elevatorCapacity){
        for(Floor f : floors){
            f.floorTick(floorNum, tick, passengers, f, elevators, elevatorCapacity);    
        }
        for(Elevator e : elevators){
            List<Integer> tempTime = e.elevatorTick(e, structures, tick, floors);

            //refresh the allTime in every loop
            //firstElement is the minTime, second is the maxTime, 
            //third is the sumTime, forth is the passenger number
            allTime.set(0,Math.min(tempTime.get(0), allTime.get(0)));
            allTime.set(1,Math.max(tempTime.get(1), allTime.get(1)));
            allTime.set(2,tempTime.get(2)+allTime.get(2));
            allTime.set(3, tempTime.get(3)+allTime.get(3));
        }
        return allTime;
    }

}
