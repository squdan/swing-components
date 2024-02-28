package io.github.squdan.swing.components;

/**
 * Interface to define required methods to implement from origin context using some components.
 */
public interface SwingComponentsView<T> {

    /**
     * Refresh current view.
     * <p>
     * This method will be called by some components after execute some operation that modify current elements
     * (create, update, delete...).
     * <p>
     * - Updating showing information.
     * - Repainting if necessary.
     */
    void refresh(T data);
}
