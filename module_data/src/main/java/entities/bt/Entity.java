package entities.bt;

public interface Entity {
    void update() throws Exception;
    void save() throws Exception;
    void delete() throws Exception;
}
