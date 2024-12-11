
package org.traccar.handler.events;

import jakarta.inject.Inject;
import org.traccar.helper.model.PositionUtil;
import org.traccar.model.Event;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

public class DriverEventHandler extends BaseEventHandler {

    private final CacheManager cacheManager;

    @Inject
    public DriverEventHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void onPosition(Position position, Callback callback) {
        if (!PositionUtil.isLatest(cacheManager, position)) {
            return;
        }
        String driverUniqueId = position.getString(Position.KEY_DRIVER_UNIQUE_ID);
        if (driverUniqueId != null) {
            String oldDriverUniqueId = null;
            Position lastPosition = cacheManager.getPosition(position.getDeviceId());
            if (lastPosition != null) {
                oldDriverUniqueId = lastPosition.getString(Position.KEY_DRIVER_UNIQUE_ID);
            }
            if (!driverUniqueId.equals(oldDriverUniqueId)) {
                Event event = new Event(Event.TYPE_DRIVER_CHANGED, position);
                event.set(Position.KEY_DRIVER_UNIQUE_ID, driverUniqueId);
                callback.eventDetected(event);
            }
        }
    }

}
