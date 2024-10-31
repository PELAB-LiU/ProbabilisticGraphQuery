package reliability.events;

@SuppressWarnings("all")
public interface ModifiableEvent {
  void add(final Event event);

  void remove(final Event event);
}
