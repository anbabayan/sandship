import java.util.UUID;

/**
 * Interface for observing material addition and removal events in a warehouse.
 */
public interface WarehouseObserver {

    /**
     * Called on material addition event.
     *
     * @param material The material that was added.
     * @param quantity The quantity of the material that was added.
     */
    void onMaterialAdded(UUID warehouseId, Material material, int quantity);

    /**
     * Called on material removal event.
     *
     * @param material The material that was removed.
     * @param quantity The quantity of the material that was removed.
     */
    void onMaterialRemoved(UUID warehouseId, Material material, int quantity);
}
