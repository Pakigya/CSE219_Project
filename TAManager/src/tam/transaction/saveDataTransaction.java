/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tam.transaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import tam.TAManagerApp;
import tam.data.TAData;

/**
 *
 * @author PLT
 */
public class saveDataTransaction implements jTPS_Transaction {
    private TAData data;
    //private TAData prevData;
    private JsonObject json;
    private JsonObject prevJson;
    private TAManagerApp app;
    
    public saveDataTransaction(TAManagerApp app){
       data = (TAData)app.getDataComponent();
        try {
            json = app.getFileComponent().saveJsonData(app.getDataComponent());
        } catch (IOException ex) {
            System.out.println("Error loading json file");
        }
       this.app = app;
       //prevData = null;
       prevJson  = null;
    }
    
    public void addDataTransaction(TAManagerApp app){
       data = (TAData)app.getDataComponent();
       this.app = app;
        try {
            json = app.getFileComponent().saveJsonData(app.getDataComponent());
        } catch (IOException ex) {
            System.out.println("Error loading json file");
        }
       prevJson = null;
    }
    
    public void addPrevData(saveDataTransaction prevTransaction){
       data = (TAData)app.getDataComponent();
       try{
       String jsonData =  prevTransaction.json.toString();
	InputStream is = new ByteArrayInputStream(jsonData.getBytes());
        JsonReader jsonReader = Json.createReader(is);
        prevJson = jsonReader.readObject();
	jsonReader.close();
	is.close();
       }
       catch (IOException e)
       {
           System.out.println("DOESNT COPY");
       }
    }
    
    public TAData getData(){
        return data;
    }
    
    @Override
    public void doTransaction() {
        try {
            // RESET THE WORKSPACE
            app.getWorkspaceComponent().resetWorkspace();

            // RESET THE DATA
            app.getDataComponent().resetData();
            // LOAD THE DATA
            //app.getWorkspaceComponent().reloadWorkspace(data);
            
            // LOAD THE FILE INTO THE DATA
            app.getFileComponent().loadJsonData(app.getDataComponent(),json);
            
            // MAKE SURE THE WORKSPACE IS ACTIVATED
            app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());

        } catch (IOException ex) {
        }
        
                
    }

    @Override
    public void undoTransaction() {
        
            System.out.print("does it reach here? UNDO") ;
        try{
//            if (prevData != null){
            if (prevJson != null){
                    System.out.print("does it reach here? jtps undo") ;
                    // RESET THE WORKSPACE
                    app.getWorkspaceComponent().resetWorkspace();

            System.out.print("does it reach here? jtps undo 1") ;
                    // RESET THE DATA
                    app.getDataComponent().resetData();

                    // LOAD THE DATA
                    //app.getWorkspaceComponent().reloadWorkspace(prevData);

            System.out.print("does it reach here? jtps undo 2") ;
                    // LOAD THE DATA
                    //app.getFileComponent().loadJsonData(prevData);
                    
                    app.getFileComponent().loadJsonData(app.getDataComponent(),prevJson);

            System.out.print("does it reach here? jtps undo 3") ;
                    // MAKE SURE THE WORKSPACE IS ACTIVATED
                    app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());
            System.out.print("does it reach here? UNDOOO") ;
            }
        }
        catch(IOException e){
            System.out.print("undo cant do lol!") ;
        }
    }
    
}
