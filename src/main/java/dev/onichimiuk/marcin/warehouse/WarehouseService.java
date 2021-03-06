package dev.onichimiuk.marcin.warehouse;

import java.util.*;
import java.util.stream.Collectors;
import dev.onichimiuk.marcin.geolocation.GeoService;
import dev.onichimiuk.marcin.warehouse.model.Warehouse;
import dev.onichimiuk.marcin.geolocation.GeoLocation;

public class WarehouseService {

    WarehouseService(WarehouseRepository repository){ this.repository = repository; }
    WarehouseService(){ repository = new WarehouseRepository(); }

    WarehouseRepository repository;
    GeoService geoService = new GeoService();

    //Zwracanie listy najbliższych magazynów gdzie można dostać zamówione produkty w podanej ilości. Gdy nie ma jakiegoś produktu
    //zwracany jest błąd biznesowy z komunikatem, że w takiej ilości brakuje produktu w magazynach. Na wejściu przekazuje się
    //lokacje zamawiającego oraz mapę zamówienia produktów np.  key:rice value:78, key:pasta value:14.
    public Set<Warehouse> findNearestConfiguration(GeoLocation location, Map<String, Integer> map) throws Exception {
        var warehouseList = repository.findAll();
        Set<Warehouse> cumulatedWarehouses = new HashSet<>();

        for (Map.Entry<String,Integer> entry : map.entrySet()) {

            List<Warehouse> warehousesWithProduct = warehouseList.stream()
                    .filter(w -> w.getProductStocks()
                            .stream()
                            .filter(productStock -> productStock.getProductCode().equals(entry.getKey()))
                            .anyMatch(productStock -> productStock.getAmount() >= entry.getValue()))
                    .collect(Collectors.toList());

            Warehouse nearestWarehouse = geoService.findNearestOfList(warehousesWithProduct, location);
            if (nearestWarehouse != null) {
                cumulatedWarehouses.add(nearestWarehouse);
            }
            else {
                String errorMessage = "Produkt "+entry.getKey()+" nie występuje w ilości "+entry.getValue()+" w żadnym magazynie. Zmodyfikuj zamówienie.";
                throw new NullPointerException(errorMessage);
            }
        }
        return cumulatedWarehouses;
    }
}