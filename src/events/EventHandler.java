package events;


public  abstract  class EventHandler<T> {
    public EventHandler(){}
    public abstract void handle(T event);
}
