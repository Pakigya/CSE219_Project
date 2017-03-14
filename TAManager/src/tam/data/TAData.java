package tam.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import properties_manager.PropertiesManager;
import tam.TAManagerApp;
import tam.TAManagerProp;
import static tam.style.TAStyle.Hover;
import tam.workspace.TAWorkspace;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 * @coauthor Pakigya Tuladhar
 */
public class TAData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    TAManagerApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;
    
    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public TAData(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();
        
        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(TAManagerProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(TAManagerProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
    }
    
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        officeHours.clear();
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    public ObservableList<String> getOptions(){
        //ObservableList<String> options = new ObservableList<String>();
        ObservableList<String> options = FXCollections.observableArrayList();
        /*return  FXCollections.observableArrayList(
        "Option 1",
        "Option 2",
        "Option 3"
    );*/
        options.add("12:00 am");
        int hour=1;
        String minutesText = "00";
        
        /*if (!onHour) {
            minutesText = "30";
        }*/
        int hourToAdd;
        for (hour=1; hour<24;hour++)
        {
            hourToAdd = hour;
            if (hour > 12) {
                hourToAdd -= 12;
                options.add(hourToAdd + ":" + minutesText + " pm");
            }
            else
            {
                options.add(hourToAdd + ":" + minutesText + " am");
            }
        }
        return options;
    }   

    
    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList getTeachingAssistants() {
        return teachingAssistants;
    }
    
    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }
    
    public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    public String getCellKey(String day, String time) {
        int col = gridHeaders.indexOf(day);
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour;
        if (hour < startHour)
            milHour += 12;
        row += (milHour - startHour) * 2;
        if (time.contains("_30"))
            row += 1;
        return getCellKey(col, row);
    }
    
    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return ta;
            }
        }
        return null;
    }
    
    /**
     * This method is for giving this data manager the string property
     * for a given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }    
    
    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid,
                                int column, int row, StringProperty prop) {
        grid.get(row).set(column, prop);
    }
    
    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour = initStartHour;
        endHour = initEndHour;
        
        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();
            
        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        workspaceComponent.reloadOfficeHoursGrid(this);
    }
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if ((initStartHour >= MIN_START_HOUR)
                && (initEndHour <= MAX_END_HOUR)
                && (initStartHour <= initEndHour)) {
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
        }
    }

    public boolean containsTA(TeachingAssistant testTA) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.equals(testTA) ) {
                return true;
            }
        }
        return false;
    }    
    
    public boolean containsTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().toLowerCase().equals(testName.toLowerCase()) ) {
                return true;
            }
        }
        return false;
    }    
    
    public boolean containsTA(String testName, String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().toLowerCase().equals(testName.toLowerCase()) || ta.getEmail().toLowerCase().equals(testEmail.toLowerCase()) ) {
                return true;
            }
        }
        return false;
    }    
    
    public boolean containsTAEmail(String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getEmail().equals(testEmail)) {
                return true;
            }
        }
        return false;
    }

    public void addTA(String initName) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(initName);

        // ADD THE TA
        if (!containsTA(initName)) {
            teachingAssistants.add(ta);
        }

        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }
    public void addTA(String initName, String initEmail) {
        // MAKE THE TA along with email
        TeachingAssistant ta = new TeachingAssistant(initName.trim(), initEmail.trim());

        // ADD THE TA along with email
        if (!containsTA(initName)) {
            teachingAssistants.add(ta);
        }
        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }
    
    public void updateTA(String initName, String initEmail, String newName, String newEmail) {
        // Delete THE TA along with email
        TeachingAssistant ta = new TeachingAssistant(initName.trim(), initEmail.trim());
        TeachingAssistant newTA = new TeachingAssistant(newName.trim(), newEmail.trim());
        if (containsTA(ta)) {
            teachingAssistants.remove(ta);
            teachingAssistants.add(newTA);
            replaceFromEverywhere(initName, newName);
        }
        // SORT THE TAS
        Collections.sort(teachingAssistants);
            System.out.println(teachingAssistants);
    }
    
    public void deleteTA(String initName, String initEmail) {
        // Delete THE TA along with email
        TeachingAssistant ta = new TeachingAssistant(initName.trim(), initEmail.trim());
        if (containsTA(ta)) {
            teachingAssistants.remove(ta);
        }
        removeFromEverywhere(initName);
        // SORT THE TAS
        Collections.sort(teachingAssistants);
            System.out.println(teachingAssistants);
    }

    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }
    
    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();
        if (isThereTAInCell(cellProp, taName) == true)
        {
            removeTAFromCell(cellProp, taName);
        }
        else
        {
            cellProp.setValue(cellText + "\n" + taName);
        }
    }
    
    /**
     * This method removes all instances of TA
     * from the Grid Cell
     * @param taName 
     */
    public void removeFromEverywhere(String taName)
    {
        // COLUMNS from 2 to 6 // ROWS from 1 to 22
        int row =1; int col = 2;
        for (row=1; row<=22;row++ )
        {
            for (col=2;col<=6;col++)
            {
                String cellKey = col + "_" + row;
                StringProperty cellProp = officeHours.get(cellKey);
                if (isThereTAInCell(cellProp, taName) == true)
                {
                    removeTAFromCell(cellProp, taName);
                }
            }
        }
    }
    /**
     * This method removes all instances of TA
     * from the Grid Cell
     * @param taName 
     * @param newTAName 
     */
    public void replaceFromEverywhere(String taName, String newTAName)
    {
        // COLUMNS from 2 to 6 // ROWS from 1 to 22
        int row =1; int col = 2;
        for (row=1; row<=22;row++ )
        {
            for (col=2;col<=6;col++)
            {
                String cellKey = col + "_" + row;
                StringProperty cellProp = officeHours.get(cellKey);
                if (isThereTAInCell(cellProp, taName) == true)
                {
                    replaceTAFromCell(cellProp, taName, newTAName);
                }
            }
        }
    }
    
    /**
     * This method checks whether the TA exists in the name office
     * grid cell already
     */
    public boolean isThereTAInCell(StringProperty cellProp, String taName){
         // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        if (cellText.contains(taName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * This method removes taName from the office grid cell
     * represented by cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            int startIndex = cellText.indexOf("\n" + taName);
            String initial = cellText.substring(0, startIndex); // Saves the data before the index of TA
            String remaining = cellText.substring(startIndex+taName.length()+1); //Save the data after the fullname of the TA
            cellText = initial + remaining; //Removes the TA
            cellProp.setValue(cellText);
        }
    }
    
     /**
     * This method replaces taName from the office grid cell
     * represented by cellProp.
     * @param cellProp
     * @param taName
     * @param newName
     */
    public void replaceTAFromCell(StringProperty cellProp, String taName, String newName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue(newName);
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(newName + "\n" + cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            int startIndex = cellText.indexOf("\n" + taName);
            String initial = cellText.substring(0, startIndex); // Saves the data before the index of TA
            String remaining = cellText.substring(startIndex+taName.length()+1); //Save the data after the fullname of the TA
            cellText = initial + "\n" + newName +  remaining; //Replaces the TA
            cellProp.setValue(cellText);
        }
    }
    
    /**
     * This method highlights all the left and above cells when a cell is hovered
     * @param cellKey
     */
    public void highlightDuringHover(String cellKey, boolean flag)
    {
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        //TAStyle styleComponent = (TAStyle)app.getStyleComponent();
        // COLUMNS from 2 to 6 // ROWS from 1 to 22
        
        int row =0; int col = 2;
        String arr[] = cellKey.split("_");
        int col1 = Integer.parseInt(arr[0]);
        int row1 = Integer.parseInt(arr[1]);
        if (flag ==  true)
        {
            
            //workspaceComponent.getOfficeHoursGridPane(getCellKey(col1,0)).getStyleClass().add("-fx-border-color: #fcffc4;");
            for (row=1; row< row1; row++ )
            {
                workspaceComponent.getTACellPane(getCellKey(col1,row)).getStyleClass().add(Hover(flag));
            }

            //workspaceComponent.getTACellPane(getCellKey(0,row1)).getStyleClass().add("-fx-border-color: #fcffc4;");
            //workspaceComponent.getTACellPane(getCellKey(1,row1)).getStyleClass().add("-fx-border-color: #fcffc4;");
            for (col=2;col< col1 ; col++)
            {
                workspaceComponent.getTACellPane(getCellKey(col,row1)).getStyleClass().add(Hover(flag));
            }
        }
        else
        {
            //workspaceComponent.getTACellPane(getCellKey(col1,0)).getStyleClass().remove(Hover(flag));
            for (row=1; row< row1; row++ )
            {
                workspaceComponent.getTACellPane(getCellKey(col1,row)).getStyleClass().remove(Hover(true));
            }

            //workspaceComponent.getTACellPane(getCellKey(0,row1)).getStyleClass().remove("-fx-border-color: #fcffc4;");
            //workspaceComponent.getTACellPane(getCellKey(1,row1)).getStyleClass().remove("-fx-border-color: #fcffc4;");
            for (col=2;col< col1 ; col++)
            {
                workspaceComponent.getTACellPane(getCellKey(col,row1)).getStyleClass().remove(Hover(true));
            }
        }
    }
}