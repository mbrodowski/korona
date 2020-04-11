package dev.onichimiuk.marcin.warehouse;

import dev.onichimiuk.marcin.geolocation.GeoLocation;
import dev.onichimiuk.marcin.warehouse.model.Warehouse;
import org.junit.Test;
import java.util.*;
import java.util.stream.Collectors;
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
        GeoLocation location = new GeoLocation() { @Override public long getX() { return x; } @Override public long getY() { return y; } };

        //when
        var result = SUT.findNearestConfiguration(location,map);

        //then
        List<Warehouse> resultList = new ArrayList<>(result);
        assertEquals("Wrocław", resultList.get(0).getCity());
    }

    @Test
    public void test_findNearestConfiguration_fewProductsMap_returnsNearestWarehouses() throws Exception {
        //given
        var mockRepository = exampleRepository();
        var SUT = new WarehouseService(mockRepository); //System Under Test
        Map<String, Integer> map = new TreeMap<>();
        map.put("rice",20); map.put("pasta",50); map.put("water",25);
        Integer x = 1530; Integer y = 5156; // Zielona Góra
        GeoLocation location = new GeoLocation() { @Override public long getX() { return x; } @Override public long getY() { return y; } };

        //when
        var result = SUT.findNearestConfiguration(location,map);

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
        GeoLocation location = new GeoLocation() { @Override public long getX() { return x; } @Override public long getY() { return y; } };

        //when
        var result = SUT.findNearestConfiguration(location,map);

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
        GeoLocation location = new GeoLocation() { @Override public long getX() { return x; } @Override public long getY() { return y; } };

        //when then
        try{
            var result = SUT.findNearestConfiguration(location,map);
        } catch (NullPointerException e){
            assertEquals("Produkt pasta nie występuje w ilości 9999 w żadnym magazynie. Zmodyfikuj zamówienie.", e.getMessage());
        }
    }

    private WarehouseRepository exampleRepository(){
        return new WarehouseRepository(){
            @Override
            public List<Warehouse> findAll() {
                return super.findAll().stream()
                        .filter(f-> f.getCity().equals("Wrocław") | f.getCity().equals("Gdańsk") | f.getCity().equals("Białystok") | f.getCity().equals("Warszawa"))
                        .collect(Collectors.toList());
            }
        };
    }
}
