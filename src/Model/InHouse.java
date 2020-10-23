package Model;

/**
 * @author Aiden Mead
 * this is the inhouse subclass of abstract part
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * Constructor for InHouse Part
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets machine ID
     * @param machineId
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * Returns machine ID
     * @return
     */
    public int getMachineId(){
        return machineId;
    }

}
