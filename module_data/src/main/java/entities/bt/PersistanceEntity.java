package entities.bt;

import dao.DAO;

public abstract class PersistanceEntity implements Entity {
    protected DAO DAO;
    public PersistanceEntity(DAO DAO) {
        this.DAO = DAO;
    }
}
