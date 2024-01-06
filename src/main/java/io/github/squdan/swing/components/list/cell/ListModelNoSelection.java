package io.github.squdan.swing.components.list.cell;

import io.github.squdan.swing.components.list.ListPanel;

import javax.swing.*;

/**
 * {@link DefaultListSelectionModel} implementation that overrides some methods to allow at {@link ListPanel} to block
 * user selections.
 */
public class ListModelNoSelection extends DefaultListSelectionModel {

    @Override
    public void setAnchorSelectionIndex(final int anchorIndex) {
    }

    @Override
    public void setLeadAnchorNotificationEnabled(final boolean flag) {
    }

    @Override
    public void setLeadSelectionIndex(final int leadIndex) {
    }

    @Override
    public void setSelectionInterval(final int index0, final int index1) {
    }
}
