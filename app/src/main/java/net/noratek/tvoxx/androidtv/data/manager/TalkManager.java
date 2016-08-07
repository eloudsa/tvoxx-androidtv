package net.noratek.tvoxx.androidtv.data.manager;

import net.noratek.tvoxx.androidtv.data.downloader.TalksDownloader;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.IOException;

/**
 * Created by eloudsa on 02/08/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class TalkManager {

    @Bean
    TalksDownloader talksDownloader;


    public void fetchSpeakerFullASync(String talkId) throws IOException {
        talksDownloader.fetchTalk(talkId);
    }


}
