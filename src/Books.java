public class Books {
    private String type;
    private int id;

    public Books(String type , int id) {
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
        if (type.equals("P")) {
            return "Created new book: " + "Printed" + " [id: " + getId()+ "]";
        }
        return "Created new book: " + "Handwritten" + " [id: " + getId()+ "]";
    }
}
