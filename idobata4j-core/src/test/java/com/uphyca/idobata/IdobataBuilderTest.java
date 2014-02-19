
package com.uphyca.idobata;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class IdobataBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullRequestInterceptorThrowsIllegalArgumentException() throws Exception {
        new IdobataBuilder().build();
    }

    @Test
    public void defaultImplementations() throws Exception {
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        Idobata idobata = new IdobataBuilder().setRequestInterceptor(requestInterceptor)
                                              .build();
        assertThat(idobata).isNotNull();
    }
}
