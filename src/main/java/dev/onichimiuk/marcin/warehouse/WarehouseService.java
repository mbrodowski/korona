package dev.onichimiuk.marcin.warehouse;

import java.util.*;

public class WarehouseService {
    private WarehouseRepository repository;

    public WarehouseService() {
        this(new WarehouseRepository());
    }

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    //Zwracanie listy najbliższych magazynów gdzie można dostać zamówione produkty w podanej ilości. Gdy nie ma jakiegoś produktu
    //zwracany jest błąd biznesowy z komunikatem, że w takiej ilości brakuje produktu w magazynach. Na wejściu przekazuje się
    //współrzędne zamawiającego oraz mapę zamówienia produktów np.  key:rice value:78, key:pasta value:14.
    public List<Warehouse> findNearestConfiguration(Integer x, Integer y, Map<String, Integer> map) throws Exception {
        var warehouseList = repository.findAll();
        List<Warehouse> cumulatedTemporaryList = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());
            List<Warehouse> temporaryList = new ArrayList<>();
            String methodName = "get" + entry.getKey().substring(0,1).toUpperCase() + entry.getKey().substring(1);
            Method currentGetter = Warehouse.class.getMethod(methodName);

            for (Warehouse w : warehouseList) {
                int i = (Integer) currentGetter.invoke(w);
                if (i >= entry.getValue()){
                    temporaryList.add(w);
                    System.out.println(w.getCity());
                }
            }

            try{
                findNearestOfList(temporaryList,x,y).getCity();
            } catch (NullPointerException e){
                String errorMessage = "Produkt "+entry.getKey()+" nie występuje w ilości "+entry.getValue()+" w żadnym magazynie. Zmodyfikuj zamówienie.";
                throw new NullPointerException(errorMessage);
            }

            cumulatedTemporaryList.add(findNearestOfList(temporaryList, x, y));
        }

        HashSet<String> hashSet = new HashSet<>();
        List<Warehouse> uniqueList = new ArrayList<>();
        for (Warehouse w : cumulatedTemporaryList) {
            if (w != null && hashSet.add(w.getCity())) uniqueList.add(w);
        }
        return uniqueList;
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