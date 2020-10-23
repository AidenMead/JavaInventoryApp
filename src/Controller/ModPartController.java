package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls all functions for modifying a part.
 */
public class ModPartController implements Initializable {
    public static Part partToMod;

    public static int partIndex;

    private boolean InHouseBool;

    @FXML final ToggleGroup partType = new ToggleGroup();

    @FXML private RadioButton InHouseRb;

    @FXML private RadioButton OutsourcedRb;

    @FXML private TextField modPartIdInput;

    @FXML private TextField modPartNameInput;

    @FXML private TextField modPartPriceInput;

    @FXML private TextField modPartStockInput;

    @FXML private TextField modPartMinInput;

    @FXML private TextField modPartMaxInput;

    @FXML private TextField modPartVariantInput;

    @FXML private Label modPartVariantLbl;

    /**
     * Sets the field to either machine id or company name depending
     * on whether the InHouse Part or Outsourced Part button is selected.
     */
    public void handleTypeToggle() {
        if (InHouseRb.isSelected()){
            modPartVariantLbl.setText("Machine ID");
        } else if (OutsourcedRb.isSelected()){
            modPartVariantLbl.setText("Company Name");
        }
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
     * Grabs all of the information input into the form and updates the part
     * with the new data.
     * @throws Exception
     */
    public void handleModSaveBtn() throws Exception {
        //Set part type from radio button - will control what time is saved as
        if (InHouseRb.isSelected()){
            InHouseBool = true;
        } else if (OutsourcedRb.isSelected()){
            InHouseBool = false;
        }

        int newId = partToMod.getId(); //Stays the same.
        String newName = modPartNameInput.getText();
        double newPrice = Double.parseDouble(modPartPriceInput.getText());
        int newStock = Integer.parseInt(modPartStockInput.getText());
        int newMin = Integer.parseInt(modPartMinInput.getText());
        int newMax = Integer.parseInt(modPartMaxInput.getText());

        if(!checkValues(newStock, newMin, newMax)){
            //stop function if values are wrong
            return;
        }

        Part newPart;
        if (InHouseBool) {
            int newMachineId = Integer.parseInt(modPartVariantInput.getText());
            newPart = new InHouse(newId, newName, newPrice, newStock, newMin, newMax, newMachineId);
        } else {
            String newCompanyName = modPartVariantInput.getText();
            newPart = new OutSourced(newId, newName, newPrice, newStock, newMin, newMax, newCompanyName);
        }
        Inventory.updatePart(partIndex, newPart);

        //Go back to main.
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.getScene().setRoot(backHome);
    }

    /**
     * Takes the user back to the Main scene without any changes.
     * @param actionEvent
     * @throws Exception
     */
    public void handleModCancelBtn(ActionEvent actionEvent) throws Exception{
        //Go back to main screen.
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.setTitle("Inventory");
        Main.window.getScene().setRoot(backHome);
    }

    /**
     * Populates the fields as soon as scene is initialized with the current values
     * for the part that was selected. Adds the radio buttons to the toggle group.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InHouseRb.setToggleGroup(partType);
        OutsourcedRb.setToggleGroup(partType);

        //Fill in selected part's values
        modPartIdInput.setText(String.valueOf(partToMod.getId()));
        modPartNameInput.setText(partToMod.getName());
        modPartPriceInput.setText(String.valueOf(partToMod.getPrice()));
        modPartStockInput.setText(String.valueOf(partToMod.getStock()));
        modPartMinInput.setText(String.valueOf(partToMod.getMin()));
        modPartMaxInput.setText(String.valueOf(partToMod.getMax()));

        //Get the relevant variant based on subclass and set the selected toggle
        if (partToMod instanceof InHouse){
            modPartVariantInput.setText(String.valueOf(((InHouse) partToMod).getMachineId()));
            InHouseRb.setSelected(true);
        } else {
            modPartVariantInput.setText(((OutSourced) partToMod).getCompanyName());
            OutsourcedRb.setSelected(true);
        }
        handleTypeToggle();
    }
}
