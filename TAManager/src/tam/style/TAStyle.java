package tam.style;

import djf.AppTemplate;
import djf.components.AppStyleComponent;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import tam.data.TeachingAssistant;
import tam.workspace.TAWorkspace;

/**
 * This class manages all CSS style for this application.
 * 
 * @author Richard McKenna
 * @coauthor Pakigya Tuladhar
 * @version 1.0
 */
public class TAStyle extends AppStyleComponent {
    // FIRST WE SHOULD DECLARE ALL OF THE STYLE TYPES WE PLAN TO USE
    
    // WE'LL USE THIS FOR ORGANIZING LEFT AND RIGHT CONTROLS
    public static String CLASS_PLAIN_PANE = "plain_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";
    public static String CLASS_HEADER_LABEL = "header_label";

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE1 = "office_hours_grid_ta_cell_pane1";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER = "office_hours_grid_ta_cell_pane_hover";
    
    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
    
    /**
     * This constructor initializes all style for the application.
     * 
     * @param initApp The application to be stylized.
     */
    public TAStyle(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // LET'S USE THE DEFAULT STYLESHEET SETUP
        super.initStylesheet(app);

        // INIT THE STYLE FOR THE FILE TOOLBAR
        app.getGUI().initFileToolbarStyle();

        // AND NOW OUR WORKSPACE STYLE
        initTAWorkspaceStyle();
    }

    /**
     * This function specifies all the style classes for
     * all user interface controls in the workspace.
     */
    private void initTAWorkspaceStyle() {
        // LEFT SIDE - THE HEADER
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getTAsHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getTAsHeaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);

        // LEFT SIDE - THE TABLE
        TableView<TeachingAssistant> taTable = workspaceComponent.getTATable();
        taTable.getStyleClass().add(CLASS_TA_TABLE);
        for (TableColumn tableColumn : taTable.getColumns()) {
            tableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER);
        }

        // LEFT SIDE - THE TA DATA ENTRY
        workspaceComponent.getAddBox().getStyleClass().add(CLASS_ADD_TA_PANE);
        workspaceComponent.getNameTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        workspaceComponent.getAddButton().getStyleClass().add(CLASS_ADD_TA_BUTTON);

        // RIGHT SIDE - THE HEADER
        workspaceComponent.getOfficeHoursSubheaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getOfficeHoursSubheaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        
        //RIGHT SIDE (SIDE) - THE HEADER
        workspaceComponent.getUpdateTimeHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getUpdateTimeHeaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    
    /**
     * This helper method initializes the style of all the nodes in the nodes
     * map to a common style, styleClass.
     */
    private void setStyleClassOnAll(HashMap nodes, String styleClass) {
        for (Object nodeObject : nodes.values()) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
    
    /*
    public void setHoverStyle( boolean flag, Pane pane){
       // TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        if (flag == true)
        {
            //workspaceComponent.getTACellPane(cellKey).getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
           pane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
// setStyleClassOnAll(pane, CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
            //setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
        }
        else 
        {
            
        }
    }*/
    
    public static String Hover(boolean flag)
    {
        if (flag == true)
        {
            return CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER;
        }
        else
        {
            return CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE1;
        }
    }
    /*
    public void setHoverStyle( boolean flag, String cellKey){
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        if (flag == true)
        {
            workspaceComponent.getTACellPane(cellKey).getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
            setStyleClassOnAll(pane, CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
            //setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE_HOVER);
        }
        else 
        {
            
        }
    }*/
}