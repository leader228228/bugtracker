package entities.bt;

import dao.DAO;

public abstract class PersistanceEntity {
    protected DAO DAO;
    public PersistanceEntity(DAO DAO) {
        this.DAO = DAO;
    }
}
