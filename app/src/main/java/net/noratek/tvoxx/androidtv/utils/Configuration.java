package net.noratek.tvoxx.androidtv.utils;

public final class Configuration {
	// location of the TVoxx Rest API server
	public static final String TVOXX_API_URL = "http://devoxx-proxy.cfapps.io/api/";

	// location of the fallback JSON data files
	public static final String SPEAKERS_JSON_DATA_FILE = "data/speakers.json";

	// Database
	public static final String DATABASE_NAME = "tvoxx_db";
	public static final int DATABASE_VERSION = 1;

	// REST timeout
	public static final int CONNECTION_TIMEOUT = 60;

	// Lifetime of data
	public static final int SPEAKERS_CACHE_LIFE_TIME_MINS = 5;
}
