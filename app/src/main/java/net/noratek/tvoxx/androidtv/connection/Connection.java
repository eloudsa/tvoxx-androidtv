package net.noratek.tvoxx.androidtv.connection;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.RealmString;
import net.noratek.tvoxx.androidtv.model.converter.RealmStringListTypeAdapter;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import java.util.concurrent.TimeUnit;

import io.realm.RealmList;
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

		builder.connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
		builder.readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);

		// GSon converter to process RealmString data type
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(new TypeToken<RealmList<RealmString>>(){}.getType(),
						RealmStringListTypeAdapter.INSTANCE)
				.create();

		final Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Constants.TVOXX_API_URL)
				.client(builder.build())
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
		tvoxxApi = retrofit.create(TvoxxApi.class);
	}

	public boolean isOnline() {
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
