package net.devsgroup.ultralog;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import net.devsgroup.ultralog.util.Ultralog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ServerTest {
    /*    @Rule
        public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
    private Context context;

    @Before
    public void getAppContext() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        new Ultralog.Builder()
                .fileName("Logs.txt")
                .baseUrl("http://192.168.80.133:8085")
                .tag("TEST")
                .build(context);

    }

    @Test
    public void sendReport() {
        Ultralog.d("gjsolkrjmalgkrnwjre;'prf ");
        Ultralog.e(new NullPointerException());
        Ultralog.d("gjsolkrjmalgkrnwjre;'prf ");
        Ultralog.e(new NullPointerException());
        Ultralog.d("gjsolkrjmalgkrnwjre;'prf ");
        Ultralog.e(new NullPointerException());
        Ultralog.d("gjsolkrjmalgkrnwjre;'prf ");
        Ultralog.e(new NullPointerException());
        Ultralog.d("gjsolkrjmalgkrnwjre;'prf ");
        Ultralog.e(new NullPointerException());
        Ultralog.d("gjsolkrjmalgkrnwjre;'prf ");
        Ultralog.e(new NullPointerException());
        Ultralog.sendLog(context, "8888", () -> {
        }, throwable -> {
        });
    }
}
