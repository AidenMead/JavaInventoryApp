package Controller;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static Model.Inventory.*;

/**
 * Main class which launches the application in the MainView scene, and populates
 * some test items on start to verify tables work properly.
 */
public class Main extends Application {
    public static Stage window;

    /**
     * On application start, will launch the main and only stage and set the scene to
     * the initial Main scene. Main scene function is controlled by the MainViewController.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent homeView = FXMLLoader.load(getClass().getResource("/Views/MainView.fxml"));
        window.setScene(new Scene(homeView));
        window.setTitle("Inventory");
        window.show();

        addTestItems();
    }

    /**
     * Creates some initial parts, products, and associations between the two so data is
     * viewable right from the beginning of the application. Data outside of these provides
     * test items are only stored in memory and will not persist. These items are created
     * at the beginning of each launch and changes to them will not persist after app is closed.
     */
    public void addTestItems(){
        Inventory.addPart(new InHouse(nextPartId, "Hard Drive", 110.00, 3, 1, 9, 34970));
        Inventory.addPart(new OutSourced(nextPartId, "CPU", 410.00, 3, 1, 9, "Intel"));
        Inventory.addPart(new InHouse(nextPartId, "Solid State Hard Drive", 220.00, 4, 1, 9, 34982));
        Inventory.addProduct(new Product(nextProdId, "Tower Computer", 699.99, 5,1,10));
        Inventory.addProduct(new Product(nextProdId, "Mini Computer", 1099.99, 2,1,10));

        Part hardDrive = getAllParts().get(0);
        Part cpu = getAllParts().get(1);
        Product tower = getAllProducts().get(0);
        Product mini = getAllProducts().get(1);
        tower.addAssociatedPart(hardDrive);
        mini.addAssociatedPart(cpu);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
