package io.github.squdan.swing.components;

public interface ComponentItemDefaultActionService<T extends ComponentItem<K>, K> {

    public void action(T element);

}
