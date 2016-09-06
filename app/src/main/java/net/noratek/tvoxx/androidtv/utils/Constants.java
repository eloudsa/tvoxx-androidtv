package net.noratek.tvoxx.androidtv.utils;

/**
 * Created by eloudsa on 31/07/16.
 */
public class Constants {

    // location of the TVoxx Rest API server
    //public static final String TVOXX_API_URL = "http://devoxx-proxy.cfapps.io/api/";
    public static final String TVOXX_API_URL = "http://192.168.1.39:8080/api/";

    // location of the fallback JSON data files
    public static final String SPEAKERS_JSON_DATA_FILE = "data/speakers.json";
    public static final String TALKS_JSON_DATA_FILE = "data/talks.json";

    // Database
    public static final String DATABASE_NAME = "tvoxx_db";
    public static final int DATABASE_VERSION = 1;

    // REST timeout
    //public static final int CONNECTION_TIMEOUT = 60;
    public static final int CONNECTION_TIMEOUT = 20;

    // Lifetime of data
    //public static final int CACHE_LIFE_TIME_MINS = 1440;
    public static final int CACHE_LIFE_TIME_MINS = 1;


    // Header position
    public static final int HEADER_SPEAKER = 2;

    // Delay before the display of the background image
    public static final int BACKGROUND_UPDATE_DELAY = 300;

    // Card types
    public static final int CARD_TYPE_TALKS = 1;
    public static final int CARD_TYPE_SPEAKERS = 2;
    public static final int CARD_TYPE_WATCHLIST = 3;
    public static final int CARD_TYPE_ABOUT = 4;
    public static final int CARD_TYPE_RELATED_TALK = 5;
    public static final int CARD_TYPE_RELATED_SPEAKER = 6;

    // Details: Header actions
    public static final int TALK_DETAIL_ACTION_PLAY_VIDEO = 1;
    public static final int TALK_DETAIL_ACTION_ADD_WATCHLIST = 2;


    // Used in intent parameters
    public static final String TALK_ID = "UUID";

    public static final String ERROR_FRAGMENT_ID = "ERROR_FRAGMENT_ID";
    public static final String ERROR_RESOURCE_MESSAGE = "ERR_RESOURCE_MESSAGE";
    public static final String ERROR_FINISH_ON_DISMISS = "ERR_FINISH_ON_DISMISS";


    // Data Id
    public static final String SPEAKERS_KEY = "SPEAKERS_KEY";
    public static final String TALKS_KEY = "TALKS_KEY";
    public static final String WATCHLIST_KEY = "WATCHLIST_KEY";


    // Etag Id
    public static final String ETAG_SPEAKERS = "ETAG_SPEAKERS";
    public static final String ETAG_SPEAKER = "ETAG_SPEAKER";
    public static final String ETAG_TALKS = "ETAG_TALKS";
    public static final String ETAG_TALK = "ETAG_TALK";



}
