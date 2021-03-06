package cloud.timo.TimoCloud.api.events.base;

import cloud.timo.TimoCloud.api.events.Event;
import cloud.timo.TimoCloud.api.objects.BaseObject;

public interface BaseAvailableRamChangeEvent extends Event {

    Integer getOldValue();

    Integer getNewValue();

    BaseObject getBase();

}
