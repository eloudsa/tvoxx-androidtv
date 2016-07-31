package net.noratek.tvoxx.androidtv.model;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by eloudsa on 31/07/16.
 */
public class Card {

    private static final String TAG = Card.class.getSimpleName();

    private long id;
    private String title;
    private String content;
    private String cardImageUrl;

    public Card() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
