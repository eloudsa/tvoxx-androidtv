package net.noratek.tvoxx.androidtv.data.manager;

import net.noratek.tvoxx.androidtv.data.downloader.SpeakersDownloader;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.IOException;

/**
 * Created by eloudsa on 02/08/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class WatchlistManager {

    @Bean
    SpeakersDownloader speakersDownloader;

    public void fetchWatchlist() throws IOException {
        speakersDownloader.fetchAllSpeakers();
    }

    public void fetchSpeaker(String uuid) throws IOException {
        speakersDownloader.fetchSpeaker(uuid);
    }


}
