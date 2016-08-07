package net.noratek.tvoxx.androidtv.event;

/**
 * Created by eloudsa on 07/08/16.
 */
public class TalkFullEvent {

    private String talkId;

    public TalkFullEvent() {
    }

    public TalkFullEvent(String talkId) {
        this.talkId = talkId;
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String uuid) {
        this.talkId = talkId;
    }
}
