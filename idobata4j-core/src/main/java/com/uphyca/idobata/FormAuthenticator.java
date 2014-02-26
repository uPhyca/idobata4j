/*
 * Copyright (C) 2014 uPhyca Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uphyca.idobata;

import com.uphyca.idobata.http.*;

import java.io.*;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents form authentication.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class FormAuthenticator implements RequestInterceptor {

    private final String email;
    private final String password;

    /**
     * @param email The email for authentication.
     * @param password The password for authentication.
     */
    public FormAuthenticator(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Response execute(Client client, Request request) throws IdobataError {
        try {
            Response response = executeInternal(client, request);
            int status = response.getStatus();
            if (status >= 500) {
                throw new HttpError(request.getUrl(), status, response.getReason());
            }
            if (status >= 400) {
                return authAndRetry(client, request);
            }
            return response;
        } catch (IOException e) {
            throw new IdobataError(e);
        }
    }

    private Response authAndRetry(Client client, Request request) throws IOException {
        String authenticityToken = acquireAuthenticityToken(client);
        signIn(client, authenticityToken);
        return executeInternal(client, request);
    }

    private Response executeInternal(Client client, Request request) throws IOException {
        Response response = client.execute(request);
        return response;
    }

    private void signIn(Client client, String authenticityToken) throws IOException {
        FormUrlEncodedTypedOutput body = new FormUrlEncodedTypedOutput().addField("utf8", "✓")
                                                                        .addField("authenticity_token", authenticityToken)
                                                                        .addField("guy[email]", email)
                                                                        .addField("guy[password]", password)
                                                                        .addField("commit", "Sign in");
        Request request = new Request("POST", "https://idobata.io/users/sign_in", Collections.<Header> emptyList(), body);
        Response response = executeInternal(client, request);
        BufferedReader reader = null;
        try {
            reader = createReader(response);
            grepAuthenticityToken(reader);
        } finally {
            closeQuietly(reader);
        }

    }

    private String acquireAuthenticityToken(Client client) throws IOException {
        Request request = new Request("GET", "https://idobata.io/users/sign_in", Collections.<Header> emptyList(), null);
        Response response = executeInternal(client, request);
        BufferedReader reader = null;
        try {
            reader = createReader(response);
            return grepAuthenticityToken(reader);
        } finally {
            closeQuietly(reader);
        }
    }

    private static BufferedReader createReader(Response response) throws IOException {
        return new BufferedReader(new InputStreamReader(response.getBody()
                                                                .in()));
    }

    private static void closeQuietly(Closeable resource) {
        if (resource == null) {
            return;
        }
        try {
            resource.close();
        } catch (IOException ignore) {
        }
    }

    private static final Pattern AUTHENTICITY_TOKEN_PATTERN = Pattern.compile("<meta content=\"([^\"]+)\" name=\"csrf-token\" />");

    private String grepAuthenticityToken(BufferedReader reader) throws IOException {
        return grep(reader, AUTHENTICITY_TOKEN_PATTERN);
    }

    private String grep(BufferedReader reader, Pattern pt) throws IOException {
        for (String each; (each = reader.readLine()) != null;) {
            Matcher mt = pt.matcher(each);
            if (mt.find() && mt.groupCount() == 1) {
                return mt.group(1);
            }
        }
        IOException tokenNotFound = new FileNotFoundException(String.format("No token found (%s)", pt.pattern()));
        tokenNotFound.fillInStackTrace();
        throw tokenNotFound;
    }
}
