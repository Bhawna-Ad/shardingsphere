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

package org.apache.shardingsphere.infra.metadata.schema.builder;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * System schema builder rule.
 */
@RequiredArgsConstructor
@Getter
public enum SystemSchemaBuilderRule {
    
    MYSQL_INFORMATION_SCHEMA("MySQL", "information_schema", Sets.newHashSet("columns", "engines",
            "parameters", "routines", "schemata", "tables", "views")),
    
    MYSQL_MYSQL("MySQL", "mysql", Sets.newHashSet("db")),
    
    MYSQL_PERFORMANCE_SCHEMA("MySQL", "performance_schema", Sets.newHashSet("accounts")),
    
    MYSQL_SYS("MySQL", "sys", Sets.newHashSet("sys")),
    
    POSTGRESQL_INFORMATION_SCHEMA("PostgreSQL", "information_schema", Sets.newHashSet("columns", "tables", "views")),
    
    POSTGRESQL_PG_CATALOG("PostgreSQL", "pg_catalog", Sets.newHashSet("pg_class", "pg_database", "pg_inherits", "pg_tablespace", "pg_trigger")),
    
    OPEN_GAUSS_INFORMATION_SCHEMA("openGauss", "information_schema", Collections.emptySet()),
    
    OPEN_GAUSS_PG_CATALOG("openGauss", "pg_catalog", Collections.emptySet()),
    
    OPEN_GAUSS_BLOCKCHAIN("openGauss", "blockchain", Collections.emptySet()),
    
    OPEN_GAUSS_CSTORE("openGauss", "cstore", Collections.emptySet()),
    
    OPEN_GAUSS_DB4AI("openGauss", "db4ai", Collections.emptySet()),
    
    OPEN_GAUSS_DBE_PERF("openGauss", "dbe_perf", Collections.emptySet()),
    
    OPEN_GAUSS_DBE_PLDEBUGGER("openGauss", "dbe_pldebugger", Collections.emptySet()),
    
    OPEN_GAUSS_GAUSSDB("openGauss", "gaussdb", Collections.emptySet()),
    
    OPEN_GAUSS_ORACLE("openGauss", "oracle", Collections.emptySet()),
    
    OPEN_GAUSS_PKG_SERVICE("openGauss", "pkg_service", Collections.emptySet()),
    
    OPEN_GAUSS_SNAPSHOT("openGauss", "snapshot", Collections.emptySet()),
    
    OPEN_GAUSS_SQLADVISOR("openGauss", "sqladvisor", Collections.emptySet());
    
    private static final Map<String, SystemSchemaBuilderRule> SCHEMA_PATH_SYSTEM_SCHEMA_BUILDER_RULE_MAP = new HashMap<>(values().length, 1);
    
    private final String databaseType;
    
    private final String schema;
    
    private final Collection<String> tables;
    
    static {
        for (SystemSchemaBuilderRule each : values()) {
            SCHEMA_PATH_SYSTEM_SCHEMA_BUILDER_RULE_MAP.put(each.getDatabaseType() + "." + each.getSchema(), each);
        }
    }
    
    /**
     * Value of builder rule.
     * 
     * @param databaseType database type
     * @param schema schema
     * @return builder rule
     */
    public static SystemSchemaBuilderRule valueOf(final String databaseType, final String schema) {
        String schemaPath = databaseType + "." + schema;
        SystemSchemaBuilderRule result = SCHEMA_PATH_SYSTEM_SCHEMA_BUILDER_RULE_MAP.get(schemaPath);
        if (null == result) {
            throw new IllegalArgumentException(String.format("Can not find builder rule: `%s`", schemaPath));
        }
        return result;
    }
    
    /**
     * Judge whether current table is system table or not.
     * 
     * @param schema schema
     * @param tableName table name
     * @return whether current table is system table or not
     */
    public static boolean isSystemTable(final String schema, final String tableName) {
        for (SystemSchemaBuilderRule each : values()) {
            if (each.getSchema().equals(schema) && each.getTables().contains(tableName)) {
                return true;
            }
        }
        return false;
    }
}
