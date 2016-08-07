package net.noratek.tvoxx.androidtv.data.cache;

public interface DataCache<DataType, StorageType> {

	void upsert(StorageType rawData, String query);

	String upsert(DataType rawData);

	DataType getData();

	DataType getData(String query);

	boolean isValid();

	boolean isValid(String query);

	void clearCache(String query);
}
