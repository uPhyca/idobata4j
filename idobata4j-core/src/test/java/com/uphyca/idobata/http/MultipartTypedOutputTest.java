
package com.uphyca.idobata.http;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MultipartTypedOutputTest {

    ByteArrayOutputStream out;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
    }

    @Test
    public void textInput() throws Exception {
        String expectedPayload = new StringBuilder().append("----hogepiyo\r\n")
                                                    .append("Content-Disposition: form-data; name=\"foo\"\r\n")
                                                    .append("\r\n")
                                                    .append("bar\r\n")
                                                    .append("----hogepiyo--")
                                                    .toString();

        MultipartTypedOutput underTest = new MultipartTypedOutput("hogepiyo").addFormField(new MultipartTypedOutput.TextInput("foo", "bar"));
        underTest.writeTo(out);

        assertThat(underTest.mimeType()).isEqualTo("multipart/form-data; charset=UTF-8; boundary=----hogepiyo");
        assertThat(underTest.length()).isEqualTo(expectedPayload.getBytes("UTF-8").length);
        assertThat(out.toByteArray()).isEqualTo(expectedPayload.getBytes("UTF-8"));
    }

    @Test
    public void fileInput() throws Exception {
        String expectedPayload = new StringBuilder().append("----hogepiyo\r\n")
                                                    .append("Content-Disposition: form-data; name=\"foo\"; filename=\"bar.txt\"\r\n")
                                                    .append("Content-Type: text/plain\r\n")
                                                    .append("\r\n")
                                                    .append("abc\r\n")
                                                    .append("----hogepiyo--")
                                                    .toString();

        ByteArrayInputStream content = new ByteArrayInputStream("abc".getBytes("UTF-8"));
        MultipartTypedOutput underTest = new MultipartTypedOutput("hogepiyo").addFormField(new MultipartTypedOutput.FileInput("foo", "bar.txt", "text/plain", content));
        underTest.writeTo(out);

        assertThat(underTest.mimeType()).isEqualTo("multipart/form-data; charset=UTF-8; boundary=----hogepiyo");
        assertThat(underTest.length()).isEqualTo(expectedPayload.getBytes("UTF-8").length);
        assertThat(out.toByteArray()).isEqualTo(expectedPayload.getBytes("UTF-8"));
    }

    @Test
    public void multipleInput() throws Exception {
        String expectedPayload = new StringBuilder().append("----hogepiyo\r\n")
                                                    .append("Content-Disposition: form-data; name=\"foo\"\r\n")
                                                    .append("\r\n")
                                                    .append("bar\r\n")
                                                    .append("----hogepiyo\r\n")
                                                    .append("Content-Disposition: form-data; name=\"abc\"\r\n")
                                                    .append("\r\n")
                                                    .append("xyz\r\n")
                                                    .append("----hogepiyo--")
                                                    .toString();

        MultipartTypedOutput underTest = new MultipartTypedOutput("hogepiyo").addFormField(new MultipartTypedOutput.TextInput("foo", "bar"))
                                                                             .addFormField(new MultipartTypedOutput.TextInput("abc", "xyz"));
        underTest.writeTo(out);

        assertThat(underTest.mimeType()).isEqualTo("multipart/form-data; charset=UTF-8; boundary=----hogepiyo");
        assertThat(underTest.length()).isEqualTo(expectedPayload.getBytes("UTF-8").length);
        assertThat(out.toByteArray()).isEqualTo(expectedPayload.getBytes("UTF-8"));
    }
}
