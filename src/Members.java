public class Members {
    private String type;
    private int id;

    public Members(String type , int id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (type.equals("S")) {
            return "Created new member: " + "Student" + " [id: " + getId() + "]";
        }
        return "Created new member: " + "Academic" + " [id: " + getId() + "]";

    }
}
