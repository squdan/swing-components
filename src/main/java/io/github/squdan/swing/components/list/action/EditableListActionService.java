package io.github.squdan.swing.components.list.action;

import io.github.squdan.swing.components.SwingComponentsItem;

/**
 * Interface that create method to execute action at {@link io.github.squdan.swing.components.list.EditableListPanel}.
 *
 * @param <T> Data type.
 * @param <K> ID type.
 */
public interface EditableListActionService<T extends SwingComponentsItem<K>, K> {

    /**
     * Create action to execute.
     */
    T create();

}
