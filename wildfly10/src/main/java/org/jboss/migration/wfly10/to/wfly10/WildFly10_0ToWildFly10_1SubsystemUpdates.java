/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.migration.wfly10.to.wfly10;

import org.jboss.migration.wfly10.config.task.subsystem.ExtensionNames;
import org.jboss.migration.wfly10.config.task.subsystem.SubsystemNames;
import org.jboss.migration.wfly10.config.task.subsystem.UpdateSubsystemTaskFactory;
import org.jboss.migration.wfly10.config.task.subsystem.infinispan.UpdateWebCache;
import org.jboss.migration.wfly10.config.task.subsystem.undertow.AddHttpsListener;
import org.jboss.migration.wfly10.config.task.subsystem.undertow.EnableHttp2;
import org.jboss.migration.wfly10.config.task.subsystem.undertow.SetDefaultHostResponseHeaderServer;
import org.jboss.migration.wfly10.config.task.subsystem.undertow.SetDefaultHostResponseHeaderXPoweredBy;

/**
 * @author emmartins
 */
public class WildFly10_0ToWildFly10_1SubsystemUpdates {

    public static final UpdateSubsystemTaskFactory INFINISPAN = new UpdateSubsystemTaskFactory.Builder(SubsystemNames.INFINISPAN, ExtensionNames.INFINISPAN)
                                .subtasks(UpdateWebCache.INSTANCE)
                                .build();

    public static final UpdateSubsystemTaskFactory UNDERTOW = new UpdateSubsystemTaskFactory.Builder(SubsystemNames.UNDERTOW, ExtensionNames.UNDERTOW)
            .subtasks(AddHttpsListener.INSTANCE, EnableHttp2.INSTANCE, new SetDefaultHostResponseHeaderServer(), new SetDefaultHostResponseHeaderXPoweredBy())
            .build();

}
