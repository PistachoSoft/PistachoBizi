package service;


public class JsonInput {
    private int id = -1;

    public JsonInput(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JsonInput{" +
                "id=" + id +
                '}';
    }
}