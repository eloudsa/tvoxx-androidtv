package net.noratek.tvoxx.model;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by eloudsa on 31/07/16.
 */
public class Video {

    private static final String TAG = Video.class.getSimpleName();

    private long id;
    private String title;
    private String studio;
    private String cardImageUrl;

    public Video() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public URI getCardImageURI() {
        try {
            return new URI(getCardImageUrl());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
