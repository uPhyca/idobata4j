
package com.uphyca.idobata.http;

import java.io.*;
import java.nio.charset.Charset;

public class MultipartTypedOutput implements TypedOutput {

    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final int BUFFER_SIZE = 8192;

    public interface FormField {
        String header();

        InputStream body() throws IOException;
    }

    public static class TextInput implements FormField {
        private final String name;
        private final String value;

        public TextInput(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String header() {
            return String.format("Content-Disposition: form-data; name=\"%s\"", name);
        }

        @Override
        public InputStream body() throws IOException {
            return new ByteArrayInputStream(value.getBytes(CHARSET));
        }
    }

    public static class FileInput implements FormField {
        private final String name;
        private final String fileName;
        private final String contentType;
        private final InputStream content;

        public FileInput(String name, String fileName, String contentType, InputStream content) {
            this.name = name;
            this.fileName = fileName;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String header() {
            return String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s", name, fileName, contentType);
        }

        @Override
        public InputStream body() throws IOException {
            return content;
        }
    }

    private static final String MIME_TYPE = "multipart/form-data; charset=UTF-8; boundary=%s";

    private String boundary;
    private ByteArrayOutputStream payload = new ByteArrayOutputStream();

    public MultipartTypedOutput(String boundary) {
        this.boundary = boundary;
    }

    @Override
    public String mimeType() {
        return String.format(MIME_TYPE, boundary);
    }

    @Override
    public long length() {
        return payload.size() + "--".getBytes(CHARSET).length + boundary.getBytes(CHARSET).length + "--".getBytes(CHARSET).length;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(payload.toByteArray());
        out.write("--".getBytes(CHARSET));
        out.write(boundary.getBytes(CHARSET));
        out.write("--".getBytes(CHARSET));
    }

    public MultipartTypedOutput addFormField(FormField formField) throws IOException {
        payload.write("--".getBytes(CHARSET));
        payload.write(boundary.getBytes(CHARSET));
        payload.write("\r\n".getBytes(CHARSET));
        payload.write(formField.header()
                               .getBytes(CHARSET));
        payload.write("\r\n".getBytes(CHARSET));
        payload.write("\r\n".getBytes(CHARSET));
        InputStream body = formField.body();
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            for (int count; (count = body.read(buf)) > -1;) {
                payload.write(buf, 0, count);
            }
        } finally {
            body.close();
        }
        payload.write("\r\n".getBytes(CHARSET));
        return this;
    }
}
