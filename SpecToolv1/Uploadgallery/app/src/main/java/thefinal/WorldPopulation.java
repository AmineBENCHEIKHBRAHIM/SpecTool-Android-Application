package thefinal;

/**
 * Created by Neopterix on 24/08/15.
 */
public class WorldPopulation {
    private Integer identification;
    private String name;
    private String location;
    private String date;
    private String length;
    private Integer edited;


    public WorldPopulation(Integer identification, String name, String location, String date, String length, Integer edited) {
        this.identification=identification;
        this.name = name;
        this.location = location;
        this.date = date;
        this.length = length;
        this.edited = edited;
    }

    public Integer getIdentification() {return identification;}

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getLength() {
        return length;
    }

    public String getDate() {
        return date;
    }

    public Integer getEdited() {
        return edited;
    }
}
