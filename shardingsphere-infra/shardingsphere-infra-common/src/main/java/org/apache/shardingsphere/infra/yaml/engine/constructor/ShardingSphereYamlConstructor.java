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

package org.apache.shardingsphere.infra.yaml.engine.constructor;

import org.apache.shardingsphere.infra.yaml.config.swapper.YamlRuleConfigurationSwapperEngine;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * ShardingSphere YAML constructor.
 */
public class ShardingSphereYamlConstructor extends Constructor {
    
    private final Map<Class<?>, Construct> typeConstructs = new HashMap<>();
    
    private final Class<?> rootClass;
    
    public ShardingSphereYamlConstructor(final Class<?> rootClass) {
        super(rootClass);
        ShardingSphereYamlConstructFactory.newInstances().forEach(each -> typeConstructs.put(each.getType(), each));
        YamlRuleConfigurationSwapperEngine.getYamlShortcuts().forEach((key, value) -> addTypeDescription(new TypeDescription(value, key)));
        this.rootClass = rootClass;
    }
    
    @Override
    protected final Construct getConstructor(final Node node) {
        return typeConstructs.getOrDefault(node.getType(), super.getConstructor(node));
    }
    
    @Override
    protected Class<?> getClassForName(final String className) throws ClassNotFoundException {
        if (className.equals(rootClass.getName())) {
            return super.getClassForName(className);
        }
        throw new IllegalArgumentException(String.format("Class is not accepted: %s", className));
    }
}
