/**
 * Represents a material type with its properties.
 */
public record Material(String name, String description, String icon, int maxCapacity) {

    @Override
    public String toString() {
        return "[" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", maxCapacity=" + maxCapacity +
                ']';
    }
}
