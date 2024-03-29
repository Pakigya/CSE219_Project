package tam.workspace;

import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import java.util.ArrayList;
import java.util.HashMap;
import tam.TAManagerApp;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import tam.TAManagerProp;
import tam.style.TAStyle;
import tam.data.TAData;
import tam.data.TeachingAssistant;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * 
 * @author Richard McKenna
 * @coauthor Pakigya Tuladhar
 */
public class TAWorkspace extends AppWorkspaceComponent {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    TAManagerApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    TAController controller;

    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    
    // Check if the first transaction is done or not
    boolean isFirstTransactionDone=false;
    
    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    
    // THE TA INPUT
    HBox addBox;
    TextField nameTextField;
    TextField emailTextField;
    Button addButton;
    Button clearButton;

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    
    // FOR THE HEADER AT THE RIGHT SIDE
    HBox updateTimeHeaderBox;
    Label updateTimeHeaderLabel;
    Label updateStartTimeLabel;
    Label updateEndTimeLabel;
    ComboBox updateStartTimeComboBox;
    ComboBox updateEndTimeComboBox;
    Button updateTimeButton;
    
    //JUST CHECKING DO AND UNDO BUTTONS
    Button doButton;
    Button undoButton;
    
    // THE OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;

    /**
     * The constructor initializes the user interface, except for
     * the full office hours grid, since it doesn't yet know what
     * the hours will be until a file is loaded or a new one is created.
     */
    public TAWorkspace(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox();
        String tasHeaderText = props.getProperty(TAManagerProp.TAS_HEADER_TEXT.toString());
        tasHeaderLabel = new Label(tasHeaderText);
        tasHeaderBox.getChildren().add(tasHeaderLabel);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TAData data = (TAData) app.getDataComponent();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setItems(tableData);
        String nameColumnText = props.getProperty(TAManagerProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(TAManagerProp.EMAIL_COLUMN_TEXT.toString());
        nameColumn = new TableColumn(nameColumnText);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        ); 
        
        taTable.getColumns().add(nameColumn);
        
        emailColumn = new TableColumn(emailColumnText);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(0.4)); // changing the width of email Tab
        taTable.getColumns().add(emailColumn);

        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(TAManagerProp.NAME_PROMPT_TEXT.toString());
        String emailPromptText = props.getProperty(TAManagerProp.EMAIL_PROMPT_TEXT.toString());
        String addButtonText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString());
        String clearButtonText = props.getProperty(TAManagerProp.CLEAR_BUTTON_TEXT.toString());
        nameTextField = new TextField();
        emailTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        emailTextField.setPromptText(emailPromptText);
        addButton = new Button(addButtonText);
        clearButton = new Button(clearButtonText);
        addBox = new HBox();
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        String officeHoursGridText = props.getProperty(TAManagerProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();


        // INIT THE HEADER ON THE LEFT
        updateTimeHeaderBox = new HBox();
        String timeHeaderText = props.getProperty(TAManagerProp.UPDATE_TIME_TEXT.toString());
        updateTimeHeaderLabel = new Label(timeHeaderText);
        String startTimeText = props.getProperty(TAManagerProp.START_TIME_TEXT.toString());
        updateStartTimeLabel = new Label(startTimeText);
        String endTimeText = props.getProperty(TAManagerProp.END_TIME_TEXT.toString());
        updateEndTimeLabel = new Label(endTimeText);
        updateTimeHeaderBox.getChildren().add(updateTimeHeaderLabel);
        updateStartTimeComboBox = new ComboBox(data.getOptions());
        updateEndTimeComboBox = new ComboBox(data.getOptions());
        String updateButtonText = props.getProperty(TAManagerProp.UPDATE_TIME_TEXT.toString());
        updateTimeButton = new Button(updateButtonText);
        
        updateStartTimeComboBox.getSelectionModel().select(data.getStartHour());
        updateEndTimeComboBox.getSelectionModel().select(data.getEndHour());
        
        //JUST CHECKING DO AND UNDO
        doButton = new Button("do");
        undoButton = new Button("undo");
        
        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);        
        leftPane.getChildren().add(taTable);        
        leftPane.getChildren().add(addBox);
        
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);
        
        VBox sidePane = new VBox();
        sidePane.getChildren().add(updateTimeHeaderBox);
        sidePane.getChildren().add(updateStartTimeLabel);
        sidePane.getChildren().add(updateStartTimeComboBox);
        sidePane.getChildren().add(updateEndTimeLabel);
        sidePane.getChildren().add(updateEndTimeComboBox);
        sidePane.getChildren().add(updateTimeButton);
        
        //Adding do and undo buttons to the sidepane
        sidePane.getChildren().add(doButton);
        sidePane.getChildren().add(undoButton);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, new ScrollPane(rightPane), sidePane);
        
        //DIVIDE THE PANE EVENLY
        sPane.setDividerPositions(0.4f, 0.9f, 0.95f);
         workspace = new BorderPane();
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(sPane);

        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        taTable.prefHeightProperty().bind(workspace.heightProperty().multiply(1.9));

        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new TAController(app);
        
            addButton.setDisable(true);
            
            //addButton.setDisable(false);
            
        
        //ADDING THE FIRST TRANSACTION WHEN THE APP LOADS
        
        if (data.isLoaded)
        {
            controller.handleAddTransaction();
        }
               
        nameTextField.setOnKeyTyped(e ->{
             if (nameTextField.getText() == "" || emailTextField.getText()== "")
            {
                addButton.setDisable(true);
            }
            else
            {
                addButton.setDisable(false);
            }
        });
        emailTextField.setOnKeyTyped(e ->{
             if (nameTextField.getText().trim() == "" || emailTextField.getText().trim() == "")
            {
                addButton.setDisable(true);
            }
            else
            {
                addButton.setDisable(false);
            }
        });
        
        // CONTROLS FOR ADDING and UPDATING TAs
        nameTextField.setOnAction(e -> { 
               updateInitialTransaction();               
            controller.handleAddUpdateTA();
        });
        emailTextField.setOnAction(e -> {
               updateInitialTransaction();
            controller.handleAddUpdateTA();
        });
        addButton.setOnAction(e -> {
               updateInitialTransaction();
            controller.handleAddUpdateTA();
        });
        
        clearButton.setOnAction(e -> {
            updateInitialTransaction();
           controller.clearUpdate(); 
        });
       
       taTable.setOnMouseClicked(e ->{
          updateInitialTransaction();
          controller.handleShowUpdateTA();
       });
       
       // FOR DELETING TA
       taTable.setOnKeyPressed(e -> {
           if (e.getCode() == KeyCode.DELETE )
           {
               updateInitialTransaction();
               controller.handleDeleteTA();
           }  
       });
       
       
       // FOR UPDATING TA OFFICE HOUR GRID USING COMBOBOX
       updateTimeButton.setOnAction(e -> {
           //updateInitialTransaction();
           controller.handleUpdateTimeGrid();
       });
       
       //FOR ADDING DOING UNDOING TRANSACTIONS
        doButton.setOnAction(e -> {
            controller.handleDoTransaction();
        });
        undoButton.setOnAction(e -> {
            controller.handleUndoTransaction();
        });
        
        // Adding Do and Undo for key events
        sPane.setOnKeyPressed(e -> {
           if (e.getCode() == KeyCode.Z && e.isControlDown() )
           {
            controller.handleUndoTransaction();
           }  
           if (e.isControlDown() && e.getCode() == KeyCode.Y )
           {
            controller.handleDoTransaction();
           }  
        });
       
        
    }
    
    private void updateInitialTransaction(){
        if(!isFirstTransactionDone){ controller.handleAddTransaction(); isFirstTransactionDone=true;}
    }
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    
    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }
    
    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Button getAddButton() {
        return addButton;
    }
    
    public Button getClearButton() {
        return clearButton;
    }

    // ACCESSING THE RIGHT SIDE PANE
    
    public HBox getUpdateTimeHeaderBox() {
        return updateTimeHeaderBox;
    }

    public Label getUpdateTimeHeaderLabel() {
        return updateTimeHeaderLabel;
    }
    public Label getUpdateStartTimeLabel() {
        return updateEndTimeLabel;
    }
    
    public ComboBox getUpdateStartTimeComboBox() {
        return updateStartTimeComboBox;
    }
    
    public Label getUpdateEndTimeLabel() {
        return updateEndTimeLabel;
    }
    
    public ComboBox getUpdateEndTimeComboBox() {
        return updateEndTimeComboBox;
    }
    
    // ACCESSING THE RIGHT PANE
    
    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }
    
    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        
        if (hour==0){
            hour = 12;
            String cellText = "" + hour + ":" + minutes;
            cellText += "am";
            return cellText;
        }
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();
        
        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        TAData taData = (TAData)dataComponent;
        reloadOfficeHoursGrid(taData);
    }

    public void reloadWorkspace(AppDataComponent dataComponent, int startTime, int endTime) {
        TAData taData = (TAData)dataComponent;
        reloadOfficeHoursGrid(taData);
    }
    public void reloadOfficeHoursGrid(TAData dataComponent) {        
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }
        
        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));            
        }
        
        // THEN THE TIME AND TA CELLS
        // FILLS 4 CELLS AT A GIVEN TIME
        int row = 1;
        int startTime = dataComponent.getStartHour();
        int endTime = dataComponent.getEndHour();
        if (startTime==0){
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(0, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(0, "30"));

            // END TIME COLUMN
            col++;
            //int endHour = 12;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(0, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(1, "00"));
            col++;
            
            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            
            startTime = 1;
            row += 2;
            
        }
        //for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
        for (int i = startTime; i < endTime; i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(endHour+1, "00"));
            col++;

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
        }
        
        // CONTROLS FOR MOUSE ENTER IN TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseEntered( e-> {
                controller.handleCellHover((Pane) e.getSource(), true);
            });
        }
        
        // CONTROLS FOR MOUSE ENTER IN TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseExited(e-> {
                controller.handleCellHover((Pane) e.getSource(), false);
            });
        }
        
        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        TAStyle taStyle = (TAStyle)app.getStyleComponent();
        taStyle.initOfficeHoursGridStyle();
    }
    
    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {       
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);
        
        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);
        
        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);
        
        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());        
    }
}
