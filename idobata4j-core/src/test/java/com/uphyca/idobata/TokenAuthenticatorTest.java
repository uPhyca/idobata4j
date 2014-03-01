
package com.uphyca.idobata;

import com.uphyca.idobata.http.Client;
import com.uphyca.idobata.http.Header;
import com.uphyca.idobata.http.Request;
import com.uphyca.idobata.http.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TokenAuthenticatorTest {

    @Mock
    Client mClient;

    @Test
    public void constantTokenString() throws Exception {

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        given(mClient.execute(requestCaptor.capture())).willReturn(mock(Response.class));

        TokenAuthenticator underTest = new TokenAuthenticator("abc");
        Request request = new Request(null, null, Collections.<Header> emptyList(), null);

        underTest.execute(mClient, request);

        Request actualRequest = requestCaptor.getValue();
        Header header = actualRequest.getHeaders()
                                     .get(0);

        assertThat(header.getName()).isEqualTo("X-API-Token");
        assertThat(header.getValue()).isEqualTo("abc");
    }

    @Test
    public void tokenProvider() throws Exception {

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        given(mClient.execute(requestCaptor.capture())).willReturn(mock(Response.class));

        TokenAuthenticator.TokenProvider tokenProvider = mock(TokenAuthenticator.TokenProvider.class);
        given(tokenProvider.get()).willReturn("abc");

        TokenAuthenticator underTest = new TokenAuthenticator(tokenProvider);
        Request request = new Request(null, null, Collections.<Header> emptyList(), null);

        underTest.execute(mClient, request);

        Request actualRequest = requestCaptor.getValue();
        Header header = actualRequest.getHeaders()
                                     .get(0);

        assertThat(header.getName()).isEqualTo("X-API-Token");
        assertThat(header.getValue()).isEqualTo("abc");
    }
}
