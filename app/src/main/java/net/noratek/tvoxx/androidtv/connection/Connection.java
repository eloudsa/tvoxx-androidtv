package net.noratek.tvoxx.androidtv.connection;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.noratek.tvoxx.androidtv.utils.Configuration;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class Connection {

	@SystemService
	ConnectivityManager cm;

	private TvoxxApi tvoxxApi;

	@AfterInject void afterInject() {
		initTvoxxApi();
	}


	public TvoxxApi getTvoxxApi() {
		return tvoxxApi;
	}

	private void initTvoxxApi() {
		final OkHttpClient.Builder builder = new OkHttpClient.Builder();

		builder.connectTimeout(Configuration.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
		builder.readTimeout(Configuration.CONNECTION_TIMEOUT, TimeUnit.SECONDS);

		final Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Configuration.TVOXX_API_URL)
				.client(builder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		tvoxxApi = retrofit.create(TvoxxApi.class);
	}

	public boolean isOnline() {
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
