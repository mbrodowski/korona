package dev.onichimiuk.marcin.warehouse;

import dev.onichimiuk.marcin.HibernateUtil;
import java.util.List;

class WarehouseRepository {

        List<Warehouse> findAll() {
            var session = HibernateUtil.getSessionFactory().openSession();
            var transaction = session.beginTransaction();

            var result = session.createQuery("from Warehouse", Warehouse.class).list();

            transaction.commit();
            session.close();
            return result;
        }
}
