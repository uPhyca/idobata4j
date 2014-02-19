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

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class HttpError extends IdobataError {

    private final String url;

    private final int status;

    private final String reason;

    public HttpError(String url, int status, String reason) {
        this.url = url;
        this.status = status;
        this.reason = reason;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return super.toString() + "," + "url='" + url + '\'' + ", status=" + status + ", reason='" + reason + '\'';
    }
}
