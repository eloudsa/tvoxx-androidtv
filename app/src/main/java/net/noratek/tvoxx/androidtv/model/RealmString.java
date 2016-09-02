package net.noratek.tvoxx.androidtv.model;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.RealmStringRealmProxy;

/**
 * Created by eloudsa on 02/09/16.
 */
@Parcel(implementations = { RealmStringRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { RealmString.class })
public class RealmString extends RealmObject {

    public String value;

}
