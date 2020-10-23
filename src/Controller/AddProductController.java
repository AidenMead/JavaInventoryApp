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
 * Controls all functions of adding a product scene.
 *
 * TODO: Expand add a part from products scene:
 * It would be nice in future versions is there was a link to add a part from the products view, since
 * a user might realize they need a part only when trying to create a product. This could be done maybe
 * in a modal for best user experience.
 */
public class AddProductController implements Initializable {
    public Product newProduct = new Product();

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

    @FXML public TextField newProductId;
    @FXML public TextField newProductName;
    @FXML public TextField newProductStock;
    @FXML public TextField newProductPrice;
    @FXML public TextField newProductMin;
    @FXML public TextField newProductMax;

    /**
     * Handles adding the selected part to the associated part array when the button is pressed.
     * Shows dialog if no part is selected.
     */
    public void handleAddAssocPartBtn(){
        Part currentPart = availPartsTable.getSelectionModel().getSelectedItem();
        if(currentPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Part Selected");
            alert.setContentText("You must select a part for it to be added.");
            alert.show();
        } else {
            newProduct.addAssociatedPart(currentPart);
        }
    }

    /**
     * Removes the selected part from the associated parts array.
     * Shows dialog to confirm part has been removed.
     */
    public void handleRemoveAssocPart() {
        Part currentPart = assocPartsTable.getSelectionModel().getSelectedItem();
        newProduct.deleteAssociatedPart(currentPart);
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setHeaderText("Removed");
        confirm.setContentText(currentPart.getName() + " has been removed.");
        confirm.show();
    }

    /**
     * Takes user back to main scene.
     * @throws Exception
     */
    public void handleCancelBtn() throws Exception {
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.getScene().setRoot(backHome);
        Main.window.setTitle("Inventory");
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
     * Check that fields have values
     */
    public void validateFieldsExist(){
        if ( newProductName.getText().isEmpty() ||
                newProductStock.getText().isEmpty() ||
                newProductPrice.getText().isEmpty() ||
                newProductMin.getText().isEmpty() ||
                newProductMax.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Blank Fields");
            alert.setContentText("All fields must be filled before saving.");
            alert.show();
            return;
        }
    }

    /**
     * Gets all of the product data in the form and creates a new product with it.
     * @throws Exception
     */
    public void handleSaveBtn() throws Exception {
        validateFieldsExist();

        int id = Inventory.nextProdId;
        String name = newProductName.getText();
        int stock = Integer.parseInt(newProductStock.getText());
        double price = Double.parseDouble(newProductPrice.getText());
        int min = Integer.parseInt(newProductMin.getText());
        int max = Integer.parseInt(newProductMax.getText());
        ObservableList<Part> newProdAssocParts = newProduct.getAssociatedParts();

        if(!checkValues(stock, min, max)){
            //stop function if values are wrong
            return;
        }

        Inventory.addProduct(new Product(id, name, price, stock, min, max, newProdAssocParts));

        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.getScene().setRoot(backHome);
        Main.window.setTitle("Inventory");
    }

    /**
     * Finds part in available parts table based on ID or name (partial or full name).
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
     * Starts the scene with the availble parts table populated and handles populating the
     * associated parts table when there is a change.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newProductId.setText(String.valueOf(Inventory.nextProdId));

        //for no known reason, the textfield is focused on intialize - this removes that
        partSearchInput.setFocusTraversable(false);

        //populate parts that can be associated
        availPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        availPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        availPartStockCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        availPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        availPartsTable.getColumns().setAll(availPartIdCol,availPartNameCol,availPartStockCol,availPartPriceCol);
        availPartsTable.setItems(Inventory.getAllParts());

        //populate parts that are already associated (only populates when modify product)
        partIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        assocPartsTable.getColumns().setAll(partIdCol,partNameCol,partStockCol,partPriceCol);
        assocPartsTable.setItems(newProduct.getAssociatedParts());
    }
}
