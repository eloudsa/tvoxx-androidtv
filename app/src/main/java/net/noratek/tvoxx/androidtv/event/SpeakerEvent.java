package net.noratek.tvoxx.androidtv.event;

/**
 * Created by eloudsa on 07/08/16.
 */
public class SpeakerEvent {

    private String uuid;

    public SpeakerEvent() {
    }

    public SpeakerEvent(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
