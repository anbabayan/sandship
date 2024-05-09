import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WarehouseTest {
    private Warehouse warehouseA;
    private Warehouse warehouseB;
    private Material iron;
    private Material copper;
    private WarehouseObserver warehouseObserverA;
    private WarehouseObserver warehouseObserverB;

    @Before
    public void setUp() {
        warehouseA = new Warehouse();
        warehouseB = new Warehouse();
        warehouseObserverA = new LoggingWarehouseObserver();
        warehouseObserverB = new LoggingWarehouseObserver();
        warehouseA.addObserver(warehouseObserverA);
        warehouseB.addObserver(warehouseObserverB);

        iron = new Material("Iron", "Common metal", "iron_icon.png", 20);
        copper = new Material("Copper", "Reddish metal", "copper_icon.png", 30);
    }

    @Test
    public void testAddMaterial() {
        warehouseA.addMaterial(iron, 5);
        warehouseA.addMaterial(copper, 3);
        warehouseA.printWarehouseData();

        assertEquals(5, (int) warehouseA.getMaterialsByQuantity().get(iron));
        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testRemoveMaterial() {
        warehouseA.addMaterial(iron, 5);
        warehouseA.addMaterial(copper, 3);

        warehouseA.removeMaterial(iron, 2);
        warehouseA.removeMaterial(copper, 1);

        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(iron));
        assertEquals(2, (int) warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testMoveMaterial() {
        warehouseA.addMaterial(iron, 5);
        warehouseA.addMaterial(copper, 3);

        warehouseA.moveMaterial(warehouseB, iron, 2);
        warehouseA.moveMaterial(warehouseB, copper, 1);

        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(iron));
        assertEquals(2, (int) warehouseA.getMaterialsByQuantity().get(copper));

        assertEquals(2, (int) warehouseB.getMaterialsByQuantity().get(iron));
        assertEquals(1, (int) warehouseB.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testAddMaterialWithNegativeQuantity() {
        warehouseA.addMaterial(iron, -1);
        assertNull(warehouseA.getMaterialsByQuantity().get(iron));
    }

    @Test
    public void testAddMaterialWithExceedingCapacity() {
        warehouseA.addMaterial(copper, 35);
        assertNull(warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testRemoveMaterialNotFound() {
        warehouseA.addMaterial(iron, 5);
        warehouseA.removeMaterial(copper, 2);

        assertEquals(5, (int) warehouseA.getMaterialsByQuantity().get(iron));
    }

    @Test
    public void testRemoveMaterialWithInsufficientQuantity() {
        warehouseA.addMaterial(copper, 3);
        warehouseA.removeMaterial(copper, 5);

        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testMoveMaterialNotFound() {
        warehouseA.addMaterial(copper, 3);
        warehouseA.moveMaterial(warehouseB, iron, 2);

        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testMoveMaterialWithNegativeQuantity() {
        warehouseA.addMaterial(copper, 3);
        warehouseA.moveMaterial(warehouseB, copper, -2);

        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testMoveMaterialWithInsufficientQuantity() {
        warehouseA.addMaterial(copper, 3);
        warehouseA.moveMaterial(warehouseB, copper, 5);

        assertEquals(3, (int) warehouseA.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testMoveMaterialReachedMaxCap() {
        warehouseA.addMaterial(copper, 13);
        warehouseB.addMaterial(copper, 18);
        warehouseA.moveMaterial(warehouseB, copper, 13);

        assertEquals(13, (int) warehouseA.getMaterialsByQuantity().get(copper));
        assertEquals(18, (int) warehouseB.getMaterialsByQuantity().get(copper));
    }

    @Test
    public void testGetMaterialCount() {
        warehouseA.addMaterial(copper, 13);
        warehouseB.addMaterial(copper, 18);

        assertEquals(13, (int) warehouseA.getMaterialCount(copper));
        assertEquals(18, (int) warehouseB.getMaterialCount(copper));
    }

    @Test
    public void testGetMaterialCountNotFound() {
        warehouseA.addMaterial(copper, 13);

        assertEquals(13, (int) warehouseA.getMaterialCount(copper));
        assertEquals(0, (int) warehouseA.getMaterialCount(iron));
    }
}
