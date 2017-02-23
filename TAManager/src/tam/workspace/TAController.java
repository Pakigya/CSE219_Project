package tam.workspace;

import static tam.TAManagerProp.*;
import djf.ui.AppMessageDialogSingleton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import tam.TAManagerApp;
import tam.data.TAData;
import tam.data.TeachingAssistant;

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

    /**
     * Constructor, note that the app must already be constructed.
     */
    public TAController(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
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
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = (TAData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name, email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addTA(name, email);
            //data.addTA(name);
            
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
        }
        updateToolBar(true);
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
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // GET THE SELECTED TA NAME
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        String taName = ta.getName();
        String taEmail = ta.getEmail();
        data.deleteTA(taName, taEmail);
        updateToolBar(true);
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
    }
    
    public void handleCellHover(Pane pane, boolean flag){
        //GET THE CELL KEY OF THE HOVERED CELL
        String cellKey = pane.getId();
        TAData data = (TAData)app.getDataComponent();
        data.highlightDuringHover(cellKey, flag);
    }
    
    
    void updateToolBar(boolean IsChangeMade)
    {
        app.getGUI().getFileController().markFileAsNotSaved();
         //app.getFileController().markAsEdited(app.getGUI());
        app.getGUI().updateToolbarControls(!IsChangeMade);
         //app.getFileController().markFileAsNotSaved();
    }
}