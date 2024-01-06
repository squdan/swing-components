package io.github.squdan.swing.components.panel.list.action;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.panel.list.EditableListPanel;

/**
 * Interface that create method to execute action at {@link EditableListPanel}.
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
