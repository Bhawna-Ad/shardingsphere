/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.algorithm.sharding.cosid;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Properties;

/**
 * Properties Utility.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {
    
    /**
     * Get the value according to the key and verify whether the value exists. If it does not exist, an {@link IllegalArgumentException} will be thrown.
     *
     * @param props Properties
     * @param key key of property
     * @return value of property
     * @throws IllegalArgumentException throw an exception when the key does not exist
     */
    public static String getRequiredValue(final Properties props, final String key) {
        Preconditions.checkArgument(props.containsKey(key), "%s can not be null.", key);
        return props.get(key).toString();
    }
}
