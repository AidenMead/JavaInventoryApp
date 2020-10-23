package Controller;

import Model.*;

import static Model.Inventory.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls all of the functions of the Main Scene (MainView).
 */
public class MainViewController implements Initializable {
    @FXML public TextField partSearchInput;
    @FXML public TextField productSearchInput;
    @FXML public TableView<Part> allPartsTable;
    @FXML public TableColumn<Part, Integer> partIdCol;
    @FXML public TableColumn<Part, String> partNameCol;
    @FXML public TableColumn<Part, Double> partPriceCol;
    @FXML public TableColumn<Part, Integer> partStockCol;

    @FXML public TableView<Product> allProductsTable;
    @FXML public TableColumn<Product, Integer> prodIdCol;
    @FXML public TableColumn<Product, String> prodNameCol;
    @FXML public TableColumn<Product, Double> prodPriceCol;
    @FXML public TableColumn<Product, Integer> prodStockCol;

    /**
     * Changes scene to Add Part when Add Part button is pressed.
     * @throws Exception
     */
    public void handleAddPartBtn() throws Exception{
        Parent addPartView = FXMLLoader.load(getClass().getResource("../Views/AddPartView.fxml"));
        Main.window.setScene(new Scene(addPartView));
        Main.window.setTitle("Add a Part");
    }

    /**
     * Changes scene to Modify Part when Modify Part button is pressed.
     * Passes the currently selected part and index of said part in
     * allParts array to the ModPartController.
     * @throws Exception
     */
    public void handleModPartBtn() throws Exception{
        Part selectedPart = currentPartSelection();
        ModPartController.partIndex = getAllParts().indexOf(selectedPart);
        ModPartController.partToMod = selectedPart;

        Parent modPartView = FXMLLoader.load(getClass().getResource("../Views/ModPartView.fxml"));
        Main.window.setScene(new Scene(modPartView));
        Main.window.setTitle("Modify a Part");
    }

    /**
     * Changes scene to Add Product when Add Product button is pressed.
     * @throws Exception
     */
    public void handleAddProductBtn() throws Exception{
        Parent addProdView = FXMLLoader.load(getClass().getResource("../Views/AddProductView.fxml"));
        Main.window.setScene(new Scene(addProdView));
        Main.window.setTitle("Add a Product");
    }

    /**
     * Changes scene to Modify Product when Modify Product button is pressed.
     * Passes the currently selected prduct and index of said product in
     * allProducts array to the ModProductController.
     * @throws Exception
     */
    public void handleModProductBtn() throws Exception{
        Product selectedProduct = currentProductSelection();
        ModProductController.prodIndex = getAllProducts().indexOf(selectedProduct);
        ModProductController.prodToModify = selectedProduct;

        Pane modProdView = FXMLLoader.load(getClass().getResource("../Views/ModProductView.fxml"));
        Main.window.setScene(new Scene(modProdView));
        Main.window.setTitle("Modify a Product");
    }

    /**
     * Handles deleting a part from the allParts array. Uses currently
     * selected part as the part to delete.
     */
    public void handleDeletePart(){
        Inventory.deletePart(currentPartSelection());
    }

    /**
     * Handles deleting a product from the allProducts array. Uses
     * currently selected product as the product to delete.
     */
    public void handleDeleteProduct(){
        Inventory.deleteProduct(currentProductSelection());
    }

    /**
     * Handles gracefully exiting the program.
     */
    public void handleExit(){
        System.exit(0);
    }

    /**
     * Gets the currently highlighted part in the array to pass to other
     * functions that need to know what part is to be acted upon.
     * @return
     */
    public Part currentPartSelection() {
         Part currentPart = allPartsTable.getSelectionModel().getSelectedItem();
         if (currentPart == null){
             Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a part to continue.");
             alert.show();
         }
         return currentPart;
    }

    /**
     * Gets the currently highlighted product in the array to pass to other
     * functions that need to know what product is to be acted upon.
     * @return
     */
    public Product currentProductSelection() {
         Product currentProduct = allProductsTable.getSelectionModel().getSelectedItem();
         if (currentProduct == null){
             Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a product to continue.");
             alert.show();
         }
         return currentProduct;
    }

    /**
     * Controls the search function for the part table. Uses enter as the key event.
     * @param keyEvent
     */
    public void searchPart(KeyEvent keyEvent){
        if ( keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (partSearchInput.getText() == "") {
                allPartsTable.setItems(getAllParts());
                allPartsTable.getSelectionModel().clearSelection();

                //if the input is empty, return to break the code to avoid
                //console errors from bad attempted assignments of the empty string
                return;
            }

            int partId;
            String partName;
            Part foundPart = null;
            ObservableList<Part> filteredParts = FXCollections.observableArrayList();
            try {
                partId = Integer.parseInt(partSearchInput.getText());
                foundPart = lookupPart(partId);
                filteredParts.add(foundPart);
            } catch(Exception e) {
                partName = partSearchInput.getText();
                filteredParts = lookupPart(partName);
            }

            if (partSearchInput != null) {
                allPartsTable.setItems(filteredParts);
                allPartsTable.getSelectionModel().select(foundPart);
            }
        }
    }

    /**
     * Controls the search function for the product table. Uses enter as the key event.
     * @param keyEvent
     */
    public void searchProduct(KeyEvent keyEvent){
        if ( keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (productSearchInput.getText() == "") {
                allProductsTable.setItems(getAllProducts());
                allProductsTable.getSelectionModel().clearSelection();

                //if the input is empty, return to break the code to avoid
                //console errors from bad attempted assignments of the empty string
                return;
            }

            int prodId;
            String prodName;
            Product foundProduct = null;
            ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
            try {
                prodId = Integer.parseInt(productSearchInput.getText());
                foundProduct = lookupProduct(prodId);
                filteredProducts.add(foundProduct);
            } catch(Exception e) {
                prodName = productSearchInput.getText();
                filteredProducts = lookupProduct(prodName);
            }
            
            if (productSearchInput != null) {
                allProductsTable.setItems(filteredProducts);
                allProductsTable.getSelectionModel().select(foundProduct);
            }
        }
    }

    /**
     * Populates the tables when the scene is initialized with relevant data.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allPartsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        allProductsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        allPartsTable.getColumns().setAll(partIdCol,partNameCol,partStockCol,partPriceCol);
        allPartsTable.setItems(getAllParts());

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        allProductsTable.getColumns().setAll(prodIdCol,prodNameCol,prodStockCol,prodPriceCol);
        allProductsTable.setItems(getAllProducts());
    }
}
