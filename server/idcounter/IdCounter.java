package server.idcounter;
// this class exists to provide sequential id numbers for other objects
public class IdCounter {
    private int current_id;

    public IdCounter() {
        current_id = 0;
    }

    public int getId() {
        return current_id++;
    }
    
    @Override
    public String toString() {
        return "" + current_id;
    }
}