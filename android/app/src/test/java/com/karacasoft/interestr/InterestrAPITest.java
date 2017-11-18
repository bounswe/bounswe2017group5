package com.karacasoft.interestr;

import android.content.Context;

import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Created by karacasoft on 18.11.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class InterestrAPITest {

    @Mock
    Context mMockContext;

    @Test
    public void login_isWorking() throws InterruptedException {
        InterestrAPI api = new InterestrAPIImpl(mMockContext);

        CountDownLatch latch = new CountDownLatch(1);

        api.login("KaracaSoft", "Mahmut95", new InterestrAPI.Callback<Token>() {
            @Override
            public void onResult(InterestrAPIResult<Token> result) {
                if(result == null) fail();
                latch.countDown();
            }

            @Override
            public void onError(String error_message) {
                fail();
            }
        });

        latch.await();
    }

}
