package controller;

import controller.viewmanager.ViewManagerException;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DatabaseModel;
import model.User;
import view.dialog.PasswordDialogFactory;

import java.io.IOException;

public class MainMenuController extends Controller
{
    @FXML
    private Button buttonNewOrder, buttonInventory, buttonSettings, buttonFiles, buttonAnalytics;

    @FXML
    private ComboBox comboName;

    private DatabaseModel dbm;
    private boolean hasPrivileges;
    private Stage stage;

    public MainMenuController(String fxmlpath, String csspath, Stage primaryStage) throws IOException
    {
        super(fxmlpath, csspath);
        dbm = new DatabaseModel();
        stage = primaryStage;
    }

    @Override
    public void load() throws ViewManagerException
    {
        if(isFirstLoad())
        {
            setupComboName();

            buttonNewOrder.addEventHandler(ActionEvent.ACTION, e ->
                    viewManager.switchViews("NewOrderController"));

            buttonInventory.addEventHandler(ActionEvent.ACTION, e ->
                    viewManager.switchViews("InventoryController"));

            buttonSettings.addEventHandler(ActionEvent.ACTION, e ->
                    viewManager.switchViews("SettingsController"));

            buttonFiles.addEventHandler(ActionEvent.ACTION, e ->
                    viewManager.switchViews("FilesController"));

            buttonAnalytics.addEventHandler(ActionEvent.ACTION, e ->
                    viewManager.switchViews("AnalyticsController"));
        }

        loadUsers();
    }

    @Override
    public void clear()
    {

    }

    protected boolean isPrivileged()
    {
        return hasPrivileges;
    }

    private void setupComboName()
    {
        Callback<ListView<User>, ListCell<User>> factory = new Callback<ListView<User>, ListCell<User>>()
        {
            @Override
            public ListCell<User> call(ListView<User> param)
            {
                return new ListCell<User>()
                {
                    @Override
                    protected void updateItem(User user, boolean empty)
                    {
                        super.updateItem(user, empty);

                        if(user != null)
                            setText(user.getUsername());
                        else
                            setText(null);
                    }
                };
            }
        };

        comboName.setCellFactory(factory);
        comboName.setButtonCell(factory.call(null));
        comboName.valueProperty().addListener((ChangeListener<User>) (ov, oldValue, newValue) ->
        {
            PasswordDialogFactory pdf = new PasswordDialogFactory();
            Dialog d = pdf.create();
            d.show();
            if(pdf.getPasswordField().getText().equals(newValue.getPassword()) && newValue != null)
            {
                int roleID = newValue.getRole().getRoleID();
                hasPrivileges = roleID == 1 || roleID == 2;
            }
        });
    }

    private void loadUsers()
    {
        comboName.setItems(FXCollections.observableArrayList(dbm.getUsers()));
    }
}
