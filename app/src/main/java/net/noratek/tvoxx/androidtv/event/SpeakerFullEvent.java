package net.noratek.tvoxx.androidtv.event;

/**
 * Created by eloudsa on 07/08/16.
 */
public class SpeakerFullEvent {

    private String uuid;

    public SpeakerFullEvent() {
    }

    public SpeakerFullEvent(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
