package io.github.squdan.swing.components;

public interface ComponentItemManagerService<T extends ComponentItem<K>, K> {

    public void see(T element);

    public T create();

    public void update(T element);

    public void delete(T element);

}
