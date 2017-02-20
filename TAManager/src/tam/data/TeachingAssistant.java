package tam.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class TeachingAssistant<E extends Comparable<E>> implements Comparable<E>  {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email;

    /**
     * Constructor initializes the TA name
     */
    public TeachingAssistant(String initName) {
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty("");
    }
    
    /**
     * Constructor initializes the TA name along with TAemail
     */
    public TeachingAssistant(String initName, String initEmail) {
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getName() {
        return name.get();
    }

    public void setName(String initName) {
        name.set(initName);
    }
    
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String initEmail) {
        name.set(initEmail);
    }

    @Override
    public int compareTo(E otherTA) {
        return getName().compareTo(((TeachingAssistant)otherTA).getName());
    }
    
    /**
     * EQUALS TO CHECK WHETHER A GIVEN TA IS EQUAL TO ANOTHER TA OR NOT
     * @param o
     * @return 
     */
    public boolean equals(Object o){
    	if (this == o) return true;
    	if (!(o instanceof TeachingAssistant)) return false;
		TeachingAssistant otherTA  = (TeachingAssistant) o;
        return (otherTA.getName().equals(this.getName()));    	
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
}