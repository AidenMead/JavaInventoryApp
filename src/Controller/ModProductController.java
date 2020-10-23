package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static Model.Inventory.getAllParts;
import static Model.Inventory.lookupPart;

/**
 * handles all functions for modifying a part
 */
public class ModProductController implements Initializable{
    public static int prodIndex;
    public static Product prodToModify;

    @FXML public TextField partSearchInput;

    @FXML public TableView<Part> assocPartsTable;
    @FXML public TableView<Part> availPartsTable;
    @FXML public TableColumn<Part, Integer> partIdCol;
    @FXML public TableColumn<Part, String> partNameCol;
    @FXML public TableColumn<Part, Integer> partStockCol;
    @FXML public TableColumn<Part, Double> partPriceCol;
    @FXML public TableColumn<Part, Integer> availPartIdCol;
    @FXML public TableColumn<Part, String> availPartNameCol;
    @FXML public TableColumn<Part, Integer> availPartStockCol;
    @FXML public TableColumn<Part, Double> availPartPriceCol;

    @FXML public TextField modProductId;
    @FXML public TextField modProductStock;
    @FXML public TextField modProductName;
    @FXML public TextField modProductPrice;
    @FXML public TextField modProductMin;
    @FXML public TextField modProductMax;

    /**
     * handles adding a part to the associated parts array
     */
    public void handleAddAssocPartBtn(){
        Part currentPart = availPartsTable.getSelectionModel().getSelectedItem();

        if(currentPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Part Selected");
            alert.setContentText("You must select a part for it to be added.");
            alert.show();
        } else {
            prodToModify.addAssociatedPart(currentPart);
        }
    }

    /**
     * handles removing a part from the associated parts array
     * shows dialog to confirm it has been removed
     */
    public void handleRemoveAssocPart() {
        Part currentPart = assocPartsTable.getSelectionModel().getSelectedItem();
        prodToModify.deleteAssociatedPart(currentPart);
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setHeaderText("Removed");
        confirm.setContentText(currentPart.getName() + " has been removed.");
        confirm.show();
    }

    /**
     * Validates that values are within logical constraints.
     * On hand inventory must be between minimum on hand and max on hand.
     * Minimum must be less than maximum.
     * @param stock
     * @param min
     * @param max
     * @return
     */
    public Boolean checkValues(int stock, int min, int max) {
        if (min>max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Value Error");
            alert.setContentText("Minimum must be less than maximum.");
            alert.show();
            return false;
        } else if(min>stock || stock>max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Value Error");
            alert.setContentText("On hand inventory must be between minimum and maximum.");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Grabs all of the new fields and creates a product, then adds it to the product array.
     * @throws Exception
     */
    public void handleSaveBtn() throws Exception {
        int id = Integer.parseInt(modProductId.getText());
        String name = modProductName.getText();
        int stock = Integer.parseInt(modProductStock.getText());
        double price = Double.parseDouble(modProductPrice.getText());
        int min = Integer.parseInt(modProductMin.getText());
        int max = Integer.parseInt(modProductMax.getText());
        ObservableList<Part> modProdAssocParts = prodToModify.getAssociatedParts();

        if(!checkValues(stock, min, max)){
            //stop function if values are wrong
            return;
        }

        Inventory.updateProduct(prodIndex, new Product(id, name, price, stock, min, max, modProdAssocParts));

        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.getScene().setRoot(backHome);
        Main.window.setTitle("Inventory");
    }

    /**
     * takes user back to main scene
     * @throws Exception
     */
    public void handleCancelBtn() throws Exception {
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.getScene().setRoot(backHome);
        Main.window.setTitle("Inventory");
    }

    /**
     * searches parts in the available parts table
     * @param keyEvent
     */
    public void searchPart(KeyEvent keyEvent){
        if ( keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (partSearchInput.getText() == "") {
                availPartsTable.setItems(getAllParts());
                availPartsTable.getSelectionModel().clearSelection();

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
                availPartsTable.setItems(filteredParts);
                availPartsTable.getSelectionModel().select(foundPart);
            }
        }
    }

    /**
     * begins the scene with the available parts and associated parts tables populated
     * and sets all of the values for the product that is being modified
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //for no known reason, the textfield is focused on initialize - this removes that
        partSearchInput.setFocusTraversable(false);

        //Set the fields with selected product
        modProductId.setText(String.valueOf(prodToModify.getId()));
        modProductName.setText(prodToModify.getName());
        modProductStock.setText(String.valueOf(prodToModify.getStock()));
        modProductPrice.setText(String.valueOf(prodToModify.getPrice()));
        modProductMax.setText(String.valueOf(prodToModify.getMax()));
        modProductMin.setText(String.valueOf(prodToModify.getMin()));

        //Fill in list of all parts that can be associated
        availPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        availPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        availPartStockCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        availPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        availPartsTable.getColumns().setAll(availPartIdCol,availPartNameCol,availPartStockCol,availPartPriceCol);
        availPartsTable.setItems(Inventory.getAllParts());

        //Fill in list of all parts already associated
        partIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        assocPartsTable.getColumns().setAll(partIdCol,partNameCol,partStockCol,partPriceCol);
        assocPartsTable.setItems(prodToModify.getAssociatedParts());
    }
}
