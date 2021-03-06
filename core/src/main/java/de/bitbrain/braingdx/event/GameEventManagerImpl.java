package de.bitbrain.braingdx.event;

import com.badlogic.gdx.Gdx;
import org.apache.commons.collections.map.MultiValueMap;

import java.util.Collection;

/**
 * @author Miguel Gonzalez Sanchez
 * @since 0.2.0
 */
public class GameEventManagerImpl implements GameEventManager {

   private final MultiValueMap listenerMapping = new MultiValueMap();

   @Override
   public <T extends GameEvent> void register(GameEventListener<T> listener, Class<T> clazz) {
      listenerMapping.put(clazz, listener);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T extends GameEvent> void unregister(GameEventListener<T> listener, Class<T> clazz) {
      Collection<?> listeners = (Collection<GameEventListener<T>>) listenerMapping.get(clazz);
      if (listeners.remove(listener)) {
         if (listeners.isEmpty()) {
            listenerMapping.remove(clazz);
         }
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T extends GameEvent> void publish(T event) {
      Collection<GameEventListener<T>> listeners = (Collection<GameEventListener<T>>) listenerMapping.get(event.getClass());
      if (listeners != null) {
         for (GameEventListener<T> listener : listeners) {
            listener.onEvent(event);
         }
      } else {
         Gdx.app.debug("EVENT", "Not publishing event of type " + event.getClass() + " - no listener registered!");
      }
   }

   @Override
   public void clear() {
      listenerMapping.clear();
   }

   @Override
   public <T extends GameEvent> void clear(Class<T> clazz) {
      listenerMapping.remove(clazz);
   }

}
