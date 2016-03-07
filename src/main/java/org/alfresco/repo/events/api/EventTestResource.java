/*
 * Copyright 2015 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.repo.events.api;

import java.util.List;

import org.alfresco.repo.events.EventGenerator;
import org.alfresco.repo.events.TestEvent;
import org.alfresco.rest.framework.resource.EntityResource;
import org.alfresco.rest.framework.resource.actions.interfaces.EntityResourceAction;
import org.alfresco.rest.framework.resource.parameters.Parameters;
import org.springframework.beans.factory.InitializingBean;

/**
 * Simple REST API for sending events to a queue.
 * 
 * @author sglover
 *
 */
@EntityResource(name="eventtest", title = "Event Test")
public class EventTestResource implements EntityResourceAction.Create<TestEvent>, InitializingBean
{
    private EventGenerator eventGenerator;

    public EventTestResource(EventGenerator eventGenerator)
    {
        super();
        this.eventGenerator = eventGenerator;
    }

    @Override
    public void afterPropertiesSet()
    {
    }

    @Override
    public List<TestEvent> create(List<TestEvent> events, Parameters params)
    {
        for(TestEvent event : events)
        {
            eventGenerator.sendEvent(event);
        }

        return events;
    }
}