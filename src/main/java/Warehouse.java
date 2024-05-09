import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a warehouse with materials and their quantities.
 */
public class Warehouse {
    private static final Logger LOGGER = Logger.getLogger(Warehouse.class.getName());
    private final UUID id;
    private final Map<Material, Integer> materialsByQuantity;
    private List<WarehouseObserver> observers;

    /**
     * Constructs a new Warehouse with a unique identifier.
     */
    public Warehouse() {
        this.id = UUID.randomUUID();
        this.materialsByQuantity = new ConcurrentHashMap<>();
    }

    /**
     * Constructs a new Warehouse with a unique identifier.
     *
     * @param materialsByQuantity materials with their quantities.
     */
    public Warehouse(Map<Material, Integer> materialsByQuantity) {
        this.id = UUID.randomUUID();
        this.materialsByQuantity = materialsByQuantity;
    }

    public Map<Material, Integer> getMaterialsByQuantity() {
        return materialsByQuantity;
    }

    public UUID getId() {
        return id;
    }

    public void addObserver(WarehouseObserver observer) {
        if (Objects.isNull(observers)) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    public void removeObserver(WarehouseObserver observer) {
        if (Objects.isNull(observers)) {
            LOGGER.log(Level.INFO, "No observer to remove in the warehouse with id " + getId());
            return;
        }
        observers.remove(observer);
    }

    /**
     * Adds a material to the warehouse with the specified quantity.
     *
     * @param material The material to be added.
     * @param quantity The quantity of the material to be added.
     */
    public void addMaterial(Material material, int quantity) {
        if (quantity < 0) {
            LOGGER.log(Level.INFO, "Material quantity cannot be negative");
            return;
        }
        if (Objects.isNull(material)) {
            LOGGER.log(Level.INFO, "Material cannot be null");
            return;
        }
        if (quantity > material.maxCapacity()) {
            LOGGER.log(Level.INFO, "Material quantity cannot be larger than max capacity");
            return;
        }
        if (materialsByQuantity.containsKey(material)) {
            final Integer existingQuantity = materialsByQuantity.get(material);
            if (existingQuantity + quantity <= material.maxCapacity()) {
                materialsByQuantity.put(material, existingQuantity + quantity);
                notifyMaterialAdded(getId(), material, quantity);
            } else {
                LOGGER.log(Level.INFO, "Cannot add, reaching max capacity for material with name "
                        + material.name() + " in the warehouse with id " + getId());
            }
        } else {
            materialsByQuantity.put(material, quantity);
            notifyMaterialAdded(getId(), material, quantity);
        }
    }

    /**
     * Removes a material from the warehouse with the specified quantity.
     *
     * @param material The material to be removed.
     * @param quantity The quantity of the material to be removed.
     */
    public void removeMaterial(Material material, int quantity) {
        if (quantity < 0) {
            LOGGER.log(Level.INFO, "Material quantity cannot be negative");
            return;
        }
        if (Objects.isNull(material)) {
            LOGGER.log(Level.INFO, "Material cannot be null");
            return;
        }
        if (!materialsByQuantity.containsKey(material)) {
            LOGGER.log(Level.INFO, "Cannot remove, material with name " + material.name()
                    + " is not found in the warehouse with id " + getId());
            return;
        }
        final Integer existingQuantity = materialsByQuantity.get(material);
        if (existingQuantity >= quantity) {
            materialsByQuantity.put(material, existingQuantity - quantity);
            notifyMaterialRemoved(getId(), material, quantity);
        } else {
            LOGGER.log(Level.INFO, "Cannot remove, not enough material with name "
                    + material.name() + " in the warehouse with id " + getId());
        }
    }

    /**
     * Moves a specified quantity of a material from this warehouse to another.
     *
     * @param toWarehouse The destination warehouse.
     * @param material    The material to be moved.
     * @param quantity    The quantity of the material to be moved.
     */
    public void moveMaterial(Warehouse toWarehouse, Material material, int quantity) {
        if (quantity < 0) {
            LOGGER.log(Level.INFO, "Material quantity cannot be negative");
            return;
        }
        if (Objects.isNull(material)) {
            LOGGER.log(Level.INFO, "Material cannot be null");
            return;
        }
        if (Objects.isNull(toWarehouse)) {
            LOGGER.log(Level.INFO, "Destination warehouse cannot be null");
            return;
        }
        if (!materialsByQuantity.containsKey(material)) {
            LOGGER.log(Level.INFO, "Cannot move, material with name "
                    + material.name() + " is not found in the warehouse with id " + getId());
            return;
        }
        if (materialsByQuantity.get(material) < quantity) {
            LOGGER.log(Level.INFO, "Material with name "
                    + material.name() + " is not enough to move from the warehouse with id " + getId());
            return;
        }
        if (toWarehouse.getMaterialCount(material) + quantity > material.maxCapacity()) {
            LOGGER.log(Level.INFO, " Cannot move, reaching max capacity for material with name "
                    + material.name() + " in the warehouse with id " + getId());
            return;
        }

        removeMaterial(material, quantity);
        toWarehouse.addMaterial(material, quantity);
    }

    /**
     * Retrieves the quantity of the specified material stored in the warehouse.
     *
     * @param material The material.
     * @return The quantity of the specified material in the warehouse.
     */
    public Integer getMaterialCount(Material material) {
        if (Objects.isNull(material)) {
            LOGGER.log(Level.INFO, "Material cannot be null");
            return null;
        }
        if (!materialsByQuantity.containsKey(material)) {
            LOGGER.log(Level.INFO, "Material with name "
                    + material.name() + " is not found in the warehouse with id " + getId());
            return 0;
        }
        return materialsByQuantity.get(material);
    }

    /**
     * Notifies observers when a material is added to the warehouse.
     *
     * @param material The material that was added.
     * @param quantity The quantity of the material that was added.
     */
    private void notifyMaterialAdded(UUID warehouseId, Material material, int quantity) {
        if (observers == null || observers.isEmpty()) {
            LOGGER.log(Level.INFO, "No observers to notify for the warehouse with id " + warehouseId);
            return;
        }
        for (WarehouseObserver observer : observers) {
            observer.onMaterialAdded(warehouseId, material, quantity);
        }
    }

    /**
     * Notifies observers when a material is removed from the warehouse.
     *
     * @param material The material that was removed.
     * @param quantity The quantity of the material that was removed.
     */
    private void notifyMaterialRemoved(UUID warehouseId, Material material, int quantity) {
        if (observers == null || observers.isEmpty()) {
            LOGGER.log(Level.INFO, "No observers to notify for the warehouse with id " + warehouseId);
            return;
        }
        for (WarehouseObserver observer : observers) {
            observer.onMaterialRemoved(warehouseId, material, quantity);
        }
    }

    /**
     * Logs the warehouse data.
     */
    public void printWarehouseData() {
        LOGGER.log(Level.INFO, toString());
    }

    @Override
    public String toString() {
        StringBuilder warehouse = new StringBuilder();
        warehouse.append("Warehouse: ").append(id);
        for (Material material : materialsByQuantity.keySet()) {
            warehouse.append(" [Material: ")
                    .append(material)
                    .append(" Quantity: ")
                    .append(materialsByQuantity.get(material))
                    .append("]")
                    .append("\n");
        }
        return warehouse.toString();
    }
}
