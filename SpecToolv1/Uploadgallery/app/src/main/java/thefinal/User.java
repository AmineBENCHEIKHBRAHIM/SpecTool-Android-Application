package thefinal;

import java.io.Serializable;

/**
 * Created by Neopterix on 01/09/15.
 */
public class User implements Serializable{
    private Integer id=null;
    private String name="";
    private String occupation="";


    public User(Integer id, String name, String occupation) {
        this.id = id;
        this.name = name;
        this.occupation = occupation;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
