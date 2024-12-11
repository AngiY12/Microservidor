
package org.traccar.handler;

import jakarta.inject.Inject;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.model.Driver;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

public class DriverHandler extends BasePositionHandler {

    private final CacheManager cacheManager;
    private final boolean useLinkedDriver;

    @Inject
    public DriverHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        useLinkedDriver = config.getBoolean(Keys.PROCESSING_USE_LINKED_DRIVER);
    }

    @Override
    public void onPosition(Position position, Callback callback) {
        if (useLinkedDriver && !position.hasAttribute(Position.KEY_DRIVER_UNIQUE_ID)) {
            var drivers = cacheManager.getDeviceObjects(position.getDeviceId(), Driver.class);
            if (!drivers.isEmpty()) {
                position.set(Position.KEY_DRIVER_UNIQUE_ID, drivers.iterator().next().getUniqueId());
            }
        }
        callback.processed(false);
    }

}
