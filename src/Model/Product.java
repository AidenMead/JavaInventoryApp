package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Aiden Mead
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Product constructor with no associated parts.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Product constructor with a list of associated parts.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param associatedParts
     */
    public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = associatedParts;
    }

    /**
     * Constructor with no arguments for instantiation.
     */
    public Product() {

    }

    /**
     * Sets id
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Sets name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets stock
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets minimum
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets maximum
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Returns ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns price
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns stock
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     * Returns minimum
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     * Returns maximum
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds associated part to the associated parts list.
     * @param part
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Removes selected associated part from the associated parts list.
     * @param selectedAssociatedPart
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Returns associated parts list
     * @return
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
}
