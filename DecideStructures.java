import java.util.*;
import java.util.LinkedList;
import java.util.List;

public class DecideStructures {
    public Deque<Passenger> creatPassengereQueue(String typeString){
        if(typeString.equals("arrays")){return new ArrayDeque<>();}
        //use the default one if the type is not "array"
        else{return new LinkedList<>();}
    }
    public Deque<Elevator> creatElevatorQueue(String typeString){
        if(typeString.equals("arrays")){return new ArrayDeque<>();}
        //use the default one if the type is not "array"
        else{return new LinkedList<>();}
    }

    
    public List<Integer> createIntegerList(String typeString){
        if(typeString.equals("arrays")){return new ArrayList<>();}
        //use the default one if the type is not "array"
        else{return new LinkedList<>();}
    }

    public List<Floor> createFloorList(String typeString){
        if(typeString.equals("arrays")){return new ArrayList<>();}
        //use the default one if the type is not "array"
        else{return new LinkedList<>();}
    }

    public List<Elevator> createElevatorList(String typeString){
        if(typeString.equals("arrays")){return new ArrayList<>();}
        //use the default one if the type is not "array"
        else{return new LinkedList<>();}
    }
}
