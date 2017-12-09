package controller;

import controller.viewmanager.ViewManagerException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.DatabaseModel;
import model.User;

import java.io.IOException;

public class MainMenuController extends Controller
{
    @FXML
    private Button buttonNewOrder, buttonInventory, buttonSettings, buttonFiles, buttonAnalytics;

    public MainMenuController(String fxmlpath, String csspath) throws IOException
    {
        super(fxmlpath, csspath);
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
            int roleID = newValue.getRole().getRoleID();
            if(newValue != null)
                hasPrivileges = roleID == 1 || roleID == 2;
        });
    }

    private void loadUsers()
    {
        comboName.setItems(FXCollections.observableArrayList(dbm.getUsers()));
    }
}
