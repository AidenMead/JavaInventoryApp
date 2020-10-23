package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import static Model.Inventory.nextPartId;

public class AddPartController{

    @FXML final ToggleGroup partType = new ToggleGroup();

    @FXML public RadioButton InHouseRb = new RadioButton();

    @FXML public RadioButton OutsourcedRb = new RadioButton();
    
    @FXML public Label newPartIdLbl;

    @FXML public TextField newPartIdInput;
    
    @FXML public Label newPartNameLbl;
    
    @FXML public TextField newPartNameInput;
    
    @FXML public Label newPartPriceLbl;
    
    @FXML public TextField newPartPriceInput;
    
    @FXML public Label newPartStockLbl;
    
    @FXML public TextField newPartStockInput;
    
    @FXML public Label newPartMinLbl;
    
    @FXML public TextField newPartMinInput;
    
    @FXML public Label newPartMaxLbl;
    
    @FXML public TextField newPartMaxInput;

    @FXML public Label newPartVariantLbl;

    @FXML public TextField newPartVariantInput;

    private boolean InHouseBool;

    /**
     * Adds the buttons to the toggle group on scene initialize.
     */
    public void initialize(){
        InHouseRb.setToggleGroup(partType);
        OutsourcedRb.setToggleGroup(partType);
        newPartIdInput.setText(String.valueOf(nextPartId));
    }

    /**
     * Sets appropriate field depending on which radio is selected.
     */
    public void handleTypeToggle() {
        if (InHouseRb.isSelected()){
            newPartVariantLbl.setText("MachineId");
        } else if (OutsourcedRb.isSelected()){
            newPartVariantLbl.setText("Company Name");
        }
    }

    /**
     * Returns user to Main Scene when cancel button is pressed.
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
     *
     * I was running into an issue where a user could hit save with no values, and a runtime error
     * was occurring, throwing a NumberFormatException: empty string. This ensures no empty data
     * is pushed to the constructor.
     * @param stock
     * @param min
     * @param max
     * @return boolean
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
    public void validateFieldsExist(){
        if ( newPartNameInput.getText().isEmpty() ||
                newPartStockInput.getText().isEmpty() ||
                newPartPriceInput.getText().isEmpty() ||
                newPartMinInput.getText().isEmpty() ||
                newPartMaxInput.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Blank Fields");
            alert.setContentText("All fields must be filled before saving.");
            alert.show();
            return;
        }
    }

    /**
     * Grabs all values and creates the part with respective values if everything checks out.
     * @throws Exception
     */
    public void handleSaveBtn() throws Exception{
        validateFieldsExist();

        //Set part type from radio button
        if (InHouseRb.isSelected()){
            InHouseBool = true;
        } else if (OutsourcedRb.isSelected()){
            InHouseBool = false;
        }

        //Get the data from the input fields
        String name = newPartNameInput.getText();
        double price = Double.parseDouble(newPartPriceInput.getText());
        int stock = Integer.parseInt(newPartStockInput.getText());
        int min = Integer.parseInt(newPartMinInput.getText());
        int max = Integer.parseInt(newPartMaxInput.getText());

        if(!checkValues(stock, min, max)){
            //stop function if values are wrong
            return;
        }

        if (InHouseBool){
            int machineId = Integer.parseInt(newPartVariantInput.getText());
            Inventory.addPart(new InHouse(nextPartId, name, price, stock, min, max, machineId));
        } else {
            String companyName = newPartVariantInput.getText();
            Inventory.addPart(new OutSourced(nextPartId, name, price, stock, min, max, companyName));
        }

        //Go back to main.
        Parent backHome = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));
        Main.window.getScene().setRoot(backHome);
    }

}
