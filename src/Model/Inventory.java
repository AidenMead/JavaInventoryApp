package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * @author Aiden Mead
 */
public class Inventory {
    final private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    final private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static int nextPartId = 1;
    public static int nextProdId = 1;

    /**
     * Generic constructor for instantiation
     */
    public Inventory(){
    }

    /**
     * Adds part to the allParts ObservableList
     * Increments the PartId to create unique IDs
     * @param newPart
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
        nextPartId++;
    }

    /**
     * Adds product to the allProducts ObservableList
     * Increments the ProdId to create unique IDs
     * @param newProd
     */
    public static void addProduct(Product newProd) {
        allProducts.add(newProd);
        nextProdId++;
    }

    /**
     * Iterates through parts to find a match based on partId.
     * Returns the matching part, or returns an error if none found.
     * @param partId
     * @return
     */
    public static Part lookupPart(int partId){
        Part foundPart = null;
        for(Part a: allParts){
            if (a.getId() == partId) {
                foundPart = a;
            }
        }
        if(foundPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Part was found.");
            alert.show();
        }
        return foundPart;
    }

    /**
     * Iterates through allParts to find matches by name.
     * Returns an array of all matches or error if none found.
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for(Part b: allParts){
            //make everything lowercase to prevent mismatch from case sensitivity
            if (b.getName().toLowerCase().contains(partName.toLowerCase())){
                filteredParts.add(b);
            }
        }
        if(filteredParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Part was found.");
            alert.show();
        }
        return filteredParts;
    }

    /**
     * Iterates through allProducts to find match based on ID.
     * Returns matching product or error if none are found.
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId) {
        Product foundProd = null;
       for(Product a: allProducts){
           if (a.getId() == productId){
               foundProd = a;
           }
       }
        if(foundProd == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Product was found.");
            alert.show();
        }
       return foundProd;
    }

    /**
     * Iterates through allProducts and searches for match by name.
     * Returns a list of all matches or error if none are found.
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for(Product b: allProducts){
            //make them all lowercase for case sensitivity
            if(b.getName().toLowerCase().contains(productName.toLowerCase())){
                filteredProducts.add(b);
            }
        }
        if(filteredProducts.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR, "No Product was found.");
             alert.show();
        }
        return filteredProducts;
    }

    /**
     * Updates selected part.
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates selected product.
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes selected part from allParts array and presents dialog to confirm it has been deleted.
     * @param selectedPart
     */
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setHeaderText("Deleted");
        confirm.setContentText(selectedPart.getName() + " has been deleted.");
        confirm.show();
    }

    /**
     * Deletes selected product from allProducts arrays and presents dialog to confirm it has been deleted.
     * Also presents an error if the product has associated parts, which it cannot have to be deleted.
     * @param selectedProduct
     */
    public static void deleteProduct(Product selectedProduct) {
        int currentAssoc = selectedProduct.getAssociatedParts().size();
        if (currentAssoc <= 0){
            allProducts.remove(selectedProduct);
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setHeaderText("Deleted");
            confirm.setContentText(selectedProduct.getName() + " has been deleted.");
            confirm.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Cannot Delete Product");
            alert.setContentText("This product has associated parts \n " +
                    "All associated parts must be removed before deleting.\n " +
                    "Remove parts by modifying the product.");
            alert.show();
        }

    }

    /**
     * Returns the ObservableList of all of the parts.
     * @return
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns the ObservableList of all of the products.
     * @return
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
