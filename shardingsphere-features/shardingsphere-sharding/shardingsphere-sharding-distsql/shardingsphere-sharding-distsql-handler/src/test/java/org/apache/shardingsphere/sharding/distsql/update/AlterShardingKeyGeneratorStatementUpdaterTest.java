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

package org.apache.shardingsphere.sharding.distsql.update;

import org.apache.shardingsphere.distsql.parser.segment.AlgorithmSegment;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.distsql.exception.DistSQLException;
import org.apache.shardingsphere.infra.distsql.exception.rule.DuplicateRuleException;
import org.apache.shardingsphere.infra.distsql.exception.rule.InvalidAlgorithmConfigurationException;
import org.apache.shardingsphere.infra.distsql.exception.rule.RequiredAlgorithmMissedException;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.distsql.handler.update.AlterShardingKeyGeneratorStatementUpdater;
import org.apache.shardingsphere.sharding.distsql.parser.segment.ShardingKeyGeneratorSegment;
import org.apache.shardingsphere.sharding.distsql.parser.statement.AlterShardingKeyGeneratorStatement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Properties;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class AlterShardingKeyGeneratorStatementUpdaterTest {
    
    @Mock
    private ShardingSphereMetaData shardingSphereMetaData;
    
    private final AlterShardingKeyGeneratorStatementUpdater updater = new AlterShardingKeyGeneratorStatementUpdater();
    
    @Before
    public void before() {
        when(shardingSphereMetaData.getDatabaseName()).thenReturn("test");
    }
    
    @Test(expected = DuplicateRuleException.class)
    public void assertExecuteWithDuplicate() throws DistSQLException {
        Properties props = new Properties();
        props.put("inputKey", "inputValue");
        ShardingKeyGeneratorSegment keyGeneratorSegment = new ShardingKeyGeneratorSegment("inputAlgorithmName", new AlgorithmSegment("inputAlgorithmName", props));
        updater.checkSQLStatement(shardingSphereMetaData, createSQLStatement(keyGeneratorSegment, keyGeneratorSegment), null);
    }
    
    @Test(expected = RequiredAlgorithmMissedException.class)
    public void assertExecuteWithNotExist() throws DistSQLException {
        Properties props = new Properties();
        props.put("inputKey", "inputValue");
        ShardingKeyGeneratorSegment keyGeneratorSegment = new ShardingKeyGeneratorSegment("notExistAlgorithmName", new AlgorithmSegment("inputAlgorithmName", props));
        ShardingRuleConfiguration ruleConfig = new ShardingRuleConfiguration();
        ruleConfig.getShardingAlgorithms().put("existAlgorithmName", new ShardingSphereAlgorithmConfiguration("hash_mod", props));
        updater.checkSQLStatement(shardingSphereMetaData, createSQLStatement(keyGeneratorSegment), ruleConfig);
    }
    
    @Test(expected = InvalidAlgorithmConfigurationException.class)
    public void assertExecuteWithInvalidAlgorithm() throws DistSQLException {
        Properties props = new Properties();
        props.put("inputKey", "inputValue");
        ShardingKeyGeneratorSegment keyGeneratorSegment = new ShardingKeyGeneratorSegment("existAlgorithmName", new AlgorithmSegment("inputAlgorithmName", props));
        ShardingRuleConfiguration ruleConfig = new ShardingRuleConfiguration();
        ruleConfig.getKeyGenerators().put("existAlgorithmName", new ShardingSphereAlgorithmConfiguration("InvalidAlgorithm", props));
        updater.checkSQLStatement(shardingSphereMetaData, createSQLStatement(keyGeneratorSegment), ruleConfig);
    }
    
    private AlterShardingKeyGeneratorStatement createSQLStatement(final ShardingKeyGeneratorSegment... ruleSegments) {
        return new AlterShardingKeyGeneratorStatement(Arrays.asList(ruleSegments));
    }
}
