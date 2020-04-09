package dev.onichimiuk.marcin.warehouse;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WarehouseServiceTest {

    @Test
    public void test_findNearestConfiguration_singleProductMap_returnsNearestSingleWarehouse() throws Exception {
        //given
        var mockRepository = exampleRepository();
        var SUT = new WarehouseService(mockRepository); //System Under Test
        Map<String, Integer> map = new TreeMap<>();
        map.put("pasta",15);
        Integer x = 1928; Integer y = 5147; // Łódź

        //when
        var result = SUT.findNearestConfiguration(x,y,map);

        //then
        assertEquals("Wrocław", result.get(0).getCity());
    }

    @Test
    public void test_findNearestConfiguration_fewProductsMap_returnsNearestWarehouses() throws Exception {
        //given
        var mockRepository = exampleRepository();
        var SUT = new WarehouseService(mockRepository); //System Under Test
        Map<String, Integer> map = new TreeMap<>();
        map.put("rice",20); map.put("pasta",50); map.put("water",25);
        Integer x = 1530; Integer y = 5156; // Zielona Góra

        //when
        var result = SUT.findNearestConfiguration(x,y,map);

        //then
        assertTrue(result.removeIf(w -> w.getCity().equals("Wrocław")));
        assertTrue(result.removeIf(w -> w.getCity().equals("Białystok")));
        assertTrue(result.removeIf(w -> w.getCity().equals("Gdańsk")));
        assertEquals(0, result.size());
    }

    @Test
    public void test_findNearestConfiguration_emptyProductMap_returnsEmptyList() throws Exception {
        //given
        var mockRepository = exampleRepository();
        var SUT = new WarehouseService(mockRepository); //System Under Test
        Map<String, Integer> map = new TreeMap<>();
        Integer x = 1928; Integer y = 5147; // Łódź

        //when
        var result = SUT.findNearestConfiguration(x,y,map);

        //then
        assertEquals(0, result.size());
    }

    @Test
    public void test_findNearestConfiguration_tooMuchAmountProduct_returnsBusinessError() throws Exception {
        //given
        var mockRepository = exampleRepository();
        var SUT = new WarehouseService(mockRepository); //System Under Test
        Map<String, Integer> map = new TreeMap<>();
        map.put("pasta",9999);
        Integer x = 1928; Integer y = 5147; // Łódź

        //when then
        try{
            var result = SUT.findNearestConfiguration(x,y,map);
        } catch (NullPointerException e){
            assertEquals("Produkt pasta nie występuje w ilości 9999 w żadnym magazynie. Zmodyfikuj zamówienie.", e.getMessage());
        }
    }

    private WarehouseRepository exampleRepository(){
        return new WarehouseRepository(){
            @Override
            public List<Warehouse> findAll() {
                List<Warehouse> warehouseList = new ArrayList<>();
                warehouseList.add(new Warehouse(1,"Wrocław", 1702, 5107, 71, 44,0));
                warehouseList.add(new Warehouse(2,"Gdańsk", 1838, 5422, 0,16,53));
                warehouseList.add(new Warehouse(3,"Białystok", 2310, 5308, 22,54,0));
                warehouseList.add(new Warehouse(4,"Warszawa", 2102, 5212, 76,12,33));
                warehouseList.add(new Warehouse(5,"Lublin", 2234, 5156, 82,0,25));

                return warehouseList;
            }
        };
    }
}
