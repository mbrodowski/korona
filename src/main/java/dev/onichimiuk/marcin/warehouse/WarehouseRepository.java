package dev.onichimiuk.marcin.warehouse;

import dev.onichimiuk.marcin.HibernateUtil;
import dev.onichimiuk.marcin.warehouse.model.Warehouse;
import java.util.List;

public class WarehouseRepository {

    public List<Warehouse> findAll() {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        var result = session.createQuery("from Warehouse", Warehouse.class).list();

        transaction.commit();
        session.close();
        return result;
    }
}
