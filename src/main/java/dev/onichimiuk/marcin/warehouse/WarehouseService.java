package dev.onichimiuk.marcin.warehouse;

import java.util.*;
import java.util.stream.Collectors;

class WarehouseService {
    private WarehouseRepository repository;

    WarehouseService() {
        this(new WarehouseRepository());
    }

    WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    //Szukanie najbliższej konfiguracji zasobów w magazynach. Na liście mogą byc zarówno pełne magazyny, magazyny z częścią produktów lub lista może być pusta,
    // gdy nie ma magazynów z szukanym produktem. Na wejściu położenie zamawiającego (x ,y) oraz tablica wyborów produktów z listy np. b=[true,false,true]. Czyli rise=true; pasta=false; water=true
    List<Warehouse> findNearestConfiguration(Integer x, Integer y, Boolean... b) {
            List<Warehouse> warehouseList = new ArrayList<>();
            var allWarehouses = repository.findAll();
            if (b[0]) warehouseList.add(findNearestOfList(allWarehouses.stream().filter(Warehouse::getRice).collect(Collectors.toList()), x, y));
            if (b[1]) warehouseList.add(findNearestOfList(allWarehouses.stream().filter(Warehouse::getPasta).collect(Collectors.toList()), x, y));
            if (b[2]) warehouseList.add(findNearestOfList(allWarehouses.stream().filter(Warehouse::getWater).collect(Collectors.toList()), x, y));
//            if (b[3]) warehouseList.add(findNearestOfList(allWarehouses.stream().filter(f -> f.getNastępnyGetter()==b[3]).collect(Collectors.toList()), x, y));

            HashSet<String> hashSet = new HashSet<>();
            List<Warehouse> uniqueList = new ArrayList<>();
            for (Warehouse w : warehouseList) {
                if( w!=null && hashSet.add(w.getCity()) ) uniqueList.add(w);
            }
            return uniqueList;
    }

    // Metoda przydatna do wykorzystania w zadaniu z kurierem by najpierw sprawdzić czy są blisko pełne magazyny od zamawijącego,
    // a później jeśli zbyt daleko to odpytać o najbliższe zasoby poprzez findNearestConfiguration.
    //Zwracanie najbliżej położonego pełnego magazynu od miejsca pobytu szukającego (x, y) lub null gdy takiego brak.
    Warehouse findNearestFullWarehouse(Integer x, Integer y) {
        var fullWarehouses = repository.findAll()
                .stream()
                .filter(f -> f.getRice() & f.getWater() & f.getPasta()) // & f.getNastępnyGetter()  Narazie zamockowana baza z 3 produktami
                .collect(Collectors.toList());

        return findNearestOfList(fullWarehouses, x, y);
    }

    //Obliczanie odległości na płaszczyźnie między punktem (x1, y1) i (x2, y2).
    private Integer calculateDistance(Integer x1, Integer y1, Integer x2, Integer y2) {
        return (int) Math.sqrt((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }

    //Szukanie najbliższego magazynu z podanej listy od miejsca pobytu szukającego (x, y) lub null gdy lista pusta.
    private Warehouse findNearestOfList(List<Warehouse> warehousesList, Integer x, Integer y) {
        if (warehousesList.size() > 0) {
            Warehouse nearest = new Warehouse();
            int minimum = Integer.MAX_VALUE;
            for (Warehouse i : warehousesList) {
                var check = calculateDistance(i.getX(), i.getY(), x, y);
                if (check < minimum) {
                    minimum = check;
                    nearest = i;
                }
            }
            return nearest;
        } else {
            return null;
        }
    }
}