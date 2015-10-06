package thefinal;

/**
 * Created by Neopterix on 24/08/15.
 */
public class Video {
    private Integer identification;
    private Integer userid;
    private String name;
    private String location;
    private String date;
    private String length;
    private String controllername;


    public Video(Integer identification, Integer userid, String name, String location, String date, String length, String controllername) {
        this.identification=identification;
        this.userid=userid;
        this.name = name;
        this.location = location;
        this.date = date;
        this.length = length;
        this.controllername = controllername;
    }

    public Integer getIdentification() {return identification;}

    public Integer getUserid() {return userid;}

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

    public String getControllername() {
        return controllername;
    }
}
