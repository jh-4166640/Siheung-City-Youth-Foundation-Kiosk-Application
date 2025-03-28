/*
import android.content.Context;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferenceDataStoreFactory;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.preferencesDataStoreFile;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.File;
import java.util.concurrent.Executor;


public class DataStoreManager {

    private static final String DATA_STORE_FILE_NAME = "settings";
    private final DataStore<Preferences> dataStore;

    public DataStoreManager(Context context) {
        File dataStoreFile = new File(context.getFilesDir(), "datastore");
        this.dataStore = PreferenceDataStoreFactory.create(
                new androidx.datastore.core.handlers.ReplaceFileCorruptionHandler<>(
                        (exception) -> {
                            // CorruptionException 처리
                            return Preferences.emptyPreferences();
                        }
                ),
                () -> dataStoreFile.toPath().resolve(DATA_STORE_FILE_NAME)
        );
    }


    public DataStore<Preferences> getDataStore() {
        return dataStore;
    }
    /*
    DATA_STORE_FILE_NAME: DataStore 파일의 이름을 정의합니다. 여기서는 "settings"로 설정했습니다.
    PreferenceDataStoreFactory.create(): DataStore 인스턴스를 생성합니다.
    CorruptionException 처리: DataStore 파일이 손상되었을 때 처리하는 로직을 제공합니다. 여기서는 빈 Preferences 객체를 반환하도록 설정했습니다.
    dataStoreFile.toPath().resolve(DATA_STORE_FILE_NAME): DataStore 파일이 저장될 경로를 설정합니다.
    */



/*데이터저장*/
/*
    private static final Preferences.Key<Boolean> EXAMPLE_COUNTER_KEY = PreferencesKeys.booleanKey("example_counter");
    private final Executor executor = MoreExecutors.directExecutor();

    public ListenableFuture<Void> saveCounterValue(boolean value) {
        ListenableFuture<EditResult<Preferences>> future = dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(EXAMPLE_COUNTER_KEY, value);
            return Futures.immediateFuture(mutablePreferences);
        }, executor);

        return Futures.transform(future, result -> null, executor);
    }
    */
    /*
    EXAMPLE_COUNTER_KEY: 저장할 데이터의 키를 정의합니다. 여기서는 boolean 타입의 "example_counter" 키를 사용합니다.
dataStore.updateDataAsync(): DataStore에 데이터를 비동기적으로 저장합니다.
prefsIn.toMutablePreferences(): Preferences 객체를 수정 가능한 MutablePreferences 객체로 변환합니다.
mutablePreferences.set(EXAMPLE_COUNTER_KEY, value): 데이터를 저장합니다.
Futures.immediateFuture(mutablePreferences): MutablePreferences 객체를 ListenableFuture로 래핑합니다.
Futures.transform(): ListenableFuture의 결과를 변환합니다. 여기서는 결과를 null로 변환하여 Void 타입의 ListenableFuture를 반환합니다.
executor: DataStore 작업을 실행할 Executor를 설정합니다. 여기서는 MoreExecutors.directExecutor()를 사용하여 현재 스레드에서 작업을 실행하도록 설정했습니다.
     */

/*데이터 읽기*/
/*
    public ListenableFuture<Boolean> getCounterValue() {
        ListenableFuture<Preferences> future = dataStore.getData(executor);
        return Futures.transform(future, prefs -> prefs.get(EXAMPLE_COUNTER_KEY) == null ? false : prefs.get(EXAMPLE_COUNTER_KEY), executor);
    }

 */
    /*
    dataStore.getData(): DataStore에서 데이터를 비동기적으로 읽습니다.
Futures.transform(): ListenableFuture의 결과를 변환합니다. 여기서는 Preferences 객체에서 EXAMPLE_COUNTER_KEY에 해당하는 값을 가져와 반환합니다.
prefs.get(EXAMPLE_COUNTER_KEY) == null ? false : prefs.get(EXAMPLE_COUNTER_KEY): 값이 없을 경우 기본값으로 false를 반환합니다.
     */
//}


/* 데이터 상사용
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class MainActivity extends AppCompatActivity {

    private DataStoreManager dataStoreManager;
    private TextView counterTextView;
    private Button counterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataStoreManager = new DataStoreManager(this);
        counterTextView = findViewById(R.id.counterTextView);
        counterButton = findViewById(R.id.counterButton);

        // 초기 카운터 값 로드
        loadCounterValue();

        counterButton.setOnClickListener(v -> {
            // 카운터 값 토글 및 저장
            toggleCounterValue();
        });
    }

    private void loadCounterValue() {
        ListenableFuture<Boolean> future = dataStoreManager.getCounterValue();
        Futures.addCallback(future, new FutureCallback<Boolean>() {
            @Override
            public

 */