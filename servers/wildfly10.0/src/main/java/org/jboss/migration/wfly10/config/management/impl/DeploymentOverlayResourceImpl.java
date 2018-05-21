/*
 * Copyright 2017 Red Hat, Inc.
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

package org.jboss.migration.wfly10.config.management.impl;

import org.jboss.as.controller.PathAddress;
import org.jboss.dmr.ModelNode;
import org.jboss.migration.wfly10.config.management.DeploymentOverlayResource;
import org.jboss.migration.wfly10.config.management.HostControllerConfiguration;
import org.jboss.migration.wfly10.config.management.ManageableResourceSelectors;
import org.jboss.migration.wfly10.config.management.ServerGroupResource;

import java.util.Collection;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT_OVERLAY;

/**
 * @author emmartins
 */
public class DeploymentOverlayResourceImpl extends AbstractManageableResource<DeploymentOverlayResource.Parent> implements DeploymentOverlayResource {

    private static final String[] EMPTY = {};

    private DeploymentOverlayResourceImpl(String resourceName, PathAddress pathAddress, Parent parent) {
        super(resourceName, pathAddress, parent);
    }

    @Override
    public String[] getDeploymentLinks() {
        final ModelNode resourceConfig = getResourceConfiguration();
        if (resourceConfig == null) {
            throw new IllegalStateException("resource does not exists");
        } else {
            if (getServerConfiguration() instanceof HostControllerConfiguration) {
                // in domain these are in the server groups overlays, with same name
                final Collection<DeploymentOverlayResource> resources = ManageableResourceSelectors.selectResources(ServerGroupResource.class)
                        .andThen(ManageableResourceSelectors.selectResources(DeploymentOverlayResource.class, getResourceName()))
                        .fromResources(getServerConfiguration());
                return resources.stream()
                        .map(resource -> resource.getResourceConfiguration())
                        .filter(config -> config.hasDefined(DEPLOYMENT))
                        .flatMap(config -> config.get(DEPLOYMENT).keys().stream())
                        .distinct()
                        .toArray(String[]::new);
            } else {
                // deployment links are in config
                if (!resourceConfig.hasDefined(DEPLOYMENT)) {
                    return EMPTY;
                } else {
                    return resourceConfig.get(DEPLOYMENT).keys().stream().toArray(String[]::new);
                }
            }
        }
    }

    public static class Factory extends AbstractManageableResource.Factory<DeploymentOverlayResource, Parent> {
        public Factory(PathAddress pathAddressBase, Parent parentResource) {
            super(RESOURCE_TYPE, pathAddressBase, DEPLOYMENT_OVERLAY, parentResource);
        }
        @Override
        public DeploymentOverlayResource newResourceInstance(String resourceName) {
            return new DeploymentOverlayResourceImpl(resourceName, getResourcePathAddress(resourceName), parentResource);
        }
    }
}
