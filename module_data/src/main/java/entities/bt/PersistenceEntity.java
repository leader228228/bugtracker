package entities.bt;

import dao.DAO;

public abstract class PersistenceEntity {
    protected DAO DAO;
    public PersistenceEntity(DAO DAO) {
        this.DAO = DAO;
    }
}
