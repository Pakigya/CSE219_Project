package tam.workspace;

import static tam.TAManagerProp.*;
import djf.ui.AppMessageDialogSingleton;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.json.JsonObject;
import tam.transaction.jTPS;
import properties_manager.PropertiesManager;
import tam.TAManagerApp;
import tam.TAManagerProp;
import tam.data.TAData;
import tam.data.TeachingAssistant;
import tam.transaction.saveDataTransaction;
import tam.validators.EmailValidator;

/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 * @coauthor Pakigya Tuladhar
 * @version 1.0
 */
public class TAController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    TAManagerApp app;

    // HERE WE KEEP TRACK OF TRANSACTIONS USING THE TRANSACTION PROCESSING SYSTEM 
    static jTPS jtps = new jTPS();
    
    public static jTPS getJTPS(){
        return jtps;
    } 
    /**
     * Constructor, note that the app must already be constructed.
     */
    public TAController(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
    }
    
    /**
     * This method checks whether it is add or update
     */
    public void handleAddUpdateTA()
    {        
        // We will need the Properties to compare whether it is update or add TA
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        Button addButton =  workspace.getAddButton();
        String buttonText = addButton.getText();
        String compareText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()).toLowerCase();
        if (buttonText.toLowerCase().contains(compareText))
        {
            handleAddTA();
        }
        else
        {
            handleUpdateTA();
        }
    }
    
    /**
     * This method responds to when the user requests to add
     * a new TA via the UI. Note that it must first do some
     * validation to make sure a unique name and email address
     * has been provided.
     */
    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        EmailValidator ev = new EmailValidator();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));   
            //workspace.getAddButton().setDisable(true);         
        }
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));     
            //workspace.getAddButton().setDisable(true);       
        }
        else if (!ev.validate(email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_TA_EMAIL_TITLE), props.getProperty(INVALID_TA_EMAIL_MESSAGE));     
            //workspace.getAddButton().setDisable(true);       
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name, email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
            //workspace.getAddButton().setDisable(true);                                    
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addTA(name, email);
            clearUpdate();
            workspace.getAddButton().setDisable(true);
            updateToolBar(true);
            handleAddTransaction();
        }
    }
    
     /**
     * This function provides updates TA when update is clicked.
     * 
     */
    public void handleUpdateTA() {
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        EmailValidator ev = new EmailValidator();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        String taName = ta.getName();
        String taEmail = ta.getEmail();
        boolean isError=false;
        
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));   
            //workspace.getAddButton().setDisable(true);         
        }
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));     
            //workspace.getAddButton().setDisable(true);       
        }
        else if (!ev.validate(email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_TA_EMAIL_TITLE), props.getProperty(INVALID_TA_EMAIL_MESSAGE));     
            //workspace.getAddButton().setDisable(true);       
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else{
            data.tempRemoveTA(taName, taEmail);
            if (data.containsTA(name, email)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
                isError= true;
            }
            data.addTA(taName, taEmail);
        // EVERYTHING IS FINE, UPDATE TA
            if (isError==false)
            {
                data.updateTA(taName, taEmail, name, email);
                clearUpdate();
                workspace.getAddButton().setDisable(true);
                updateToolBar(true);
                handleAddTransaction();
            }
        }
    }
    
    /**
     * This function changes the text on add/update TA button to 
     * Add TA
     */
    public void clearUpdate(){
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        
        TableView taTable = workspace.getTATable();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        Button addButton = workspace.getAddButton();

        // CLEAR THE TEXT FIELDS
        nameTextField.setText("");
        emailTextField.setText("");
        taTable.getSelectionModel().clearSelection();
        
        addButton.setText(props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()));
        // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
        nameTextField.requestFocus();
        workspace.getAddButton().setDisable(true);
        updateToolBar(true);
    }
    
    public void clearSelection(){
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        
        TableView taTable = workspace.getTATable();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        Button addButton = workspace.getAddButton();

        // CLEAR THE TEXT FIELDS
        nameTextField.setText("");
        emailTextField.setText("");
        taTable.getSelectionModel().clearSelection();
        
        addButton.setText(props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()));
        // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
        //nameTextField.requestFocus();
        workspace.getAddButton().setDisable(true);
        updateToolBar(true);
    }
    
    
    /**
     * This function changes the text of the add/update button to 
     * Update TA
     */
    public void changeToUpdate(){
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        Button addButton = workspace.getAddButton();
        addButton.setText(props.getProperty(TAManagerProp.UPDATE_BUTTON_TEXT.toString()));
    }
    
    /**
     * This method responds to when the user deletes 
     * a TA via the UI. 
     */
    public void handleDeleteTA(){
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // GET THE SELECTED TA NAME
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        String taName = ta.getName();
        String taEmail = ta.getEmail();
        data.deleteTA(taName, taEmail);
        updateToolBar(true);
        if (data.getTeachingAssistants().size() ==0)
        {
            clearSelection();
        }
        handleAddTransaction();
    }
    
    /**
     * This function provides updates the data of the AddBox when a TA is clicked.
     * 
     */
    public void handleShowUpdateTA() {
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        Button addButton = workspace.getAddButton();
        
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        String taName = ta.getName();
        String taEmail = ta.getEmail();
        //if (selectedItem != null){
        if (data.getTeachingAssistants().size() !=0)
        {
            nameTextField.setText(taName);
            emailTextField.setText(taEmail);
            changeToUpdate();
        }
        else
        {
            //clearSelection();
            nameTextField.setText("");
            emailTextField.setText("");
            taTable.getSelectionModel().clearSelection();

            addButton.setText(props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()));
            //Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        }
    }
    
    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     * 
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        String taName = ta.getName();
        TAData data = (TAData)app.getDataComponent();
        String cellKey = pane.getId();
        
        // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
        data.toggleTAOfficeHours(cellKey, taName);
        updateToolBar(true);
        handleAddTransaction();
    }
    
    public void handleCellHover(Pane pane, boolean flag){
        //GET THE CELL KEY OF THE HOVERED CELL
        String cellKey = pane.getId();
        TAData data = (TAData)app.getDataComponent();
        data.highlightDuringHover(cellKey, flag);
    }
    
    public void handleUpdateTimeGrid(){
        
        // GET THE WORKSPACE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TAData data = (TAData)app.getDataComponent();
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        int startTime = workspace.getUpdateStartTimeComboBox().getSelectionModel().getSelectedIndex();
        int endTime = workspace.getUpdateEndTimeComboBox().getSelectionModel().getSelectedIndex();
        
        // DID THE USER HAVE AN END TIME BEFORE START TIME?
        if (startTime>= endTime) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_UPDATE_TIME_TITLE), props.getProperty(INVALID_UPDATE_TIME_MESSAGE));  
        }
        // EVERYTHING IS FINE, UPDATE TIME and GRID
        /*else if (startTime>data.getStartHour() || endTime<data.getEndHour()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_UPDATE_TIME_TITLE), props.getProperty(INVALID_UPDATE_TIME_MESSAGE));  
        } */  
        else{
            data.setStartHour(startTime);
            data.setEndHour(endTime);
            JsonObject json;
            try {
                json = app.getFileComponent().saveJsonData(data, startTime, endTime);
           
            // RESET THE WORKSPACE
                app.getWorkspaceComponent().resetWorkspace();

            // RESET THE DATA
                //app.getDataComponent().resetData();
            
            data.initHours(startTime+"", endTime+"");
            
            // LOAD THE FILE INTO THE DATA
                 app.getFileComponent().loadJsonData(data,json);
            // MAKE SURE THE WORKSPACE IS ACTIVATED
                app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());
            
            updateToolBar(true);
            handleAddTransaction();
             } catch (IOException ex) {
                System.out.println("Didnot save to json");
            }
        }
    }
    
    public void handleAddTransaction()
    {
        saveDataTransaction transaction = new saveDataTransaction(app);
        jtps.addTransaction(transaction);
        handleShowUpdateTA();
        //clearSelection();
        //clearUpdate();
    }
    
    public void handleDoTransaction()
    {
        jtps.doTransaction();
        clearSelection();
    }
    
    public void handleUndoTransaction()
    {
        jtps.undoTransaction();
        clearSelection();
    }
    void updateToolBar(boolean IsChangeMade)
    {
        app.getGUI().getFileController().markFileAsNotSaved();
         //app.getFileController().markAsEdited(app.getGUI());
        app.getGUI().updateToolbarControls(!IsChangeMade);
         //app.getFileController().markFileAsNotSaved();
    }
}