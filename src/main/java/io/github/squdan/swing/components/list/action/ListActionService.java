package io.github.squdan.swing.components.list.action;

import io.github.squdan.swing.components.SwingComponentsItem;

/**
 * Interface that action method to execute action at {@link io.github.squdan.swing.components.list.ListPanel}.
 *
 * @param <T> Data type.
 * @param <K> ID type.
 */
public interface ListActionService<T extends SwingComponentsItem<K>, K> {

    /**
     * Action to execute.
     *
     * @param element selected to execute the action over it.
     */
    void action(T element);

}
