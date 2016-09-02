package net.noratek.tvoxx.androidtv.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by eloudsa on 22/08/16.
 */
public class Etag extends RealmObject {

    @PrimaryKey
    private String id;
    private String eTag;
    private String url;

    public Etag() {
    }


    public Etag(String id, String eTag, String url) {
        this.id = id;
        this.eTag = eTag;
        this.url = url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return eTag;
    }

    public void setEtag(String eTag) {
        this.eTag = eTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
