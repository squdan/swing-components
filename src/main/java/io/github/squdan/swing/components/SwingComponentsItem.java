package io.github.squdan.swing.components;

/**
 * Interface that defines required information for component items.
 *
 * @param <T> ID type.
 */
public interface SwingComponentsItem<T> {

    /**
     * Return id value.
     *
     * @return id value.
     */
    T getId();

    /**
     * Return String representation.
     *
     * @return String representation.
     */
    String toTextField();
}
