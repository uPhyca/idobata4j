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

package com.uphyca.idobata.transform;

import com.uphyca.idobata.http.TypedInput;
import com.uphyca.idobata.transform.ConversionException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Arbiter for converting objects from their representation in HTTP.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface Converter {

    /**
     * Convert an HTTP response body to a concrete object of the specified type.
     * 
     * @param body HTTP response body.
     * @param type Target object type.
     * @return Instance of {@code type} which will be cast by the caller.
     * @throws Exception
     */
    <T> T convert(TypedInput body, Type type) throws ConversionException, IOException;
}
