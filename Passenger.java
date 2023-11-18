class Passenger implements Comparable<Passenger>{
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

    public int startFloor(){
        return startFloor;
    }
    public int endFloor(){
        return endFloor;
    }
    public int startTick(){
        return startTick;
    }
    public int endTick(){
        return endTick;
    }
    
    public int timePassenger(passenger p){
        if(p.endTick > p.startTick){
            return p.endTick - p.startTick;
        }
        //if passenger is still not reach the position, send an invalid time
        else{return -1;}        
    }

    public int compareTo(Passenger otherPassenger) {
        return Integer.compare(this.endFloor, otherPassenger.endFloor);
    }
}
