/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.roadm;

import com.google.common.collect.ImmutableList;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.onosproject.ui.UiExtension;
import org.onosproject.ui.UiExtensionService;
import org.onosproject.ui.UiMessageHandlerFactory;
import org.onosproject.ui.UiView;
import org.onosproject.ui.UiViewHidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ONOS UI for ROADM application.
 */
@Component(immediate = true)
public class RoadmComponent {

    private static final String DEVICE_VIEW_ID = "roadmDevice";
    private static final String DEVICE_VIEW_TEXT = "ROADM";

    private static final String RESOURCE_PATH = "webgui";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected UiExtensionService uiExtensionService;

    // List of application views
    private final List<UiView> deviceViews = ImmutableList.of(
            new UiView(UiView.Category.OTHER, DEVICE_VIEW_ID, DEVICE_VIEW_TEXT),
            new UiViewHidden("roadmPort"),
            new UiViewHidden("roadmFlow")
    );

    // Factory for UI message handlers
    private final UiMessageHandlerFactory messageHandlerFactory =
            () -> ImmutableList.of(
                    new RoadmDeviceViewMessageHandler(),
                    new RoadmPortViewMessageHandler(),
                    new RoadmFlowViewMessageHandler()
            );

    // Device UI extension
    protected UiExtension deviceExtension =
            new UiExtension.Builder(getClass().getClassLoader(), deviceViews)
                    .resourcePath(RESOURCE_PATH)
                    .messageHandlerFactory(messageHandlerFactory)
                    .build();

    @Activate
    protected void activate() {
        uiExtensionService.register(deviceExtension);
        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        uiExtensionService.unregister(deviceExtension);
        log.info("Stopped");
    }

}
