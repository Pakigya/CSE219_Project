package tam.transaction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author McKillaGorilla
 */
public class jTPS {
    private ArrayList<saveDataTransaction> transactions = new ArrayList();
    private int mostRecentTransaction = -1;
    
    public jTPS() {}
    
    public void addTransaction(saveDataTransaction transaction) {
        if (mostRecentTransaction>=0)
        {   
            //try {
                transaction.addPrevData(transactions.get(mostRecentTransaction));
            //} catch (CloneNotSupportedException ex) {
                
                   System.out.println("DOESNT COPY");
            //}
        }
        // IS THIS THE FIRST TRANSACTION?
        if (mostRecentTransaction < 0) {
            // DO WE HAVE TO CHOP THE LIST?
            if (transactions.size() > 0) {
                transactions = new ArrayList();
            }
            transactions.add(transaction);
            System.out.print(transaction) ;
        }
        // ARE WE ERASING ALL THE REDO TRANSACTIONS?
        else if (mostRecentTransaction < (transactions.size()-1)) {
            transactions.set(mostRecentTransaction+1, transaction);
            transactions = new ArrayList(transactions.subList(0, mostRecentTransaction+2));
        }
        // IS IT JUST A TRANSACTION TO APPEND TO THE END?
        else {
            transactions.add(transaction);
            System.out.print(transaction) ;
        }
        mostRecentTransaction++;
        //doTransaction();
    }
    
    public void doTransaction() {
        if (mostRecentTransaction < (transactions.size()-1)) {
            saveDataTransaction transaction = transactions.get(mostRecentTransaction+1);
            transaction.doTransaction();
            mostRecentTransaction++;
        }
    }
    
    public void undoTransaction() {
        if (mostRecentTransaction >= 0) {
            System.out.print("does it reach here? jtps undo") ;
            saveDataTransaction transaction = transactions.get(mostRecentTransaction);
            transaction.undoTransaction();
            System.out.print("does it reach here? jtps undo after") ;
            mostRecentTransaction--;
        }
    }
    
    public String toString() {
        String text = "--Number of Transactions: " + transactions.size() + "\n";
        text += "--Current Index on Stack: " + mostRecentTransaction + "\n";
        text += "--Current Transaction Stack:\n";
        for (int i = 0; i <= mostRecentTransaction; i++) {
            saveDataTransaction jT = transactions.get(i);
            text += "----" + jT.toString() + "\n";
        }
        return text;
    }
}