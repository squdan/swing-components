package io.github.squdan.swing.components.panel.list.action;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.panel.list.ListPanel;

/**
 * Interface that action method to execute action at {@link ListPanel}.
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
