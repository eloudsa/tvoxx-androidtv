package net.noratek.tvoxx.androidtv.data;

import android.content.Context;

import net.noratek.tvoxx.androidtv.utils.Configuration;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

@EBean(scope = EBean.Scope.Singleton)
public class RealmProvider {

    @RootContext
    Context context;

    private boolean inited = false;

    public void init() {
        final RealmConfiguration configuration =
                new RealmConfiguration.Builder(context)
                        .name(Configuration.DATABASE_NAME)
                        .schemaVersion(Configuration.DATABASE_VERSION)
                        .migration(new SchemaMigration())
                        .build();
        Realm.setDefaultConfiguration(configuration);

        inited = true;
    }

    public Realm getRealm() {
        if (!inited) {
            init();
        }

        return Realm.getDefaultInstance();
    }

    private static class SchemaMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            /*
            final RealmSchema schema = realm.getSchema();

            if (oldVersion == 1) {

                if (!schema.contains("RealmSpeaker")) {
                    schema.create("RealmSpeaker");
                }

                if (!schema.contains("RealmTalk")) {
                    schema.create("RealmTalk");
                }
                oldVersion++;
            }
            */
        }
    }
}
