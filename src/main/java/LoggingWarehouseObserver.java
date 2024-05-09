import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An implementation of {@link WarehouseObserver} that logs material addition and removal events.
 */
public class LoggingWarehouseObserver implements WarehouseObserver {
    private static final Logger LOGGER = Logger.getLogger(LoggingWarehouseObserver.class.getName());

    public LoggingWarehouseObserver() {
    }

    /**
     * Logs a message when a material is added to the warehouse.
     *
     * @param material The material that was added.
     * @param quantity The quantity of the material that was added.
     */
    @Override
    public void onMaterialAdded(UUID warehouseId, Material material, int quantity) {
        String logMessage = String.format(
                "Material '%s' added to warehouse (ID: %s): Quantity = %d", material, warehouseId, quantity);
        LOGGER.log(Level.INFO, logMessage);
    }

    /**
     * Logs a message when a material is removed from the warehouse.
     *
     * @param material The material that was removed.
     * @param quantity The quantity of the material that was removed.
     */
    @Override
    public void onMaterialRemoved(UUID warehouseId, Material material, int quantity) {
        String logMessage = String.format(
                "Material '%s' removed from warehouse (ID: %s): Quantity = %d", material, warehouseId, quantity);
        LOGGER.log(Level.INFO, logMessage);
    }
}
