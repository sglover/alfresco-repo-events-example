/*
 * Copyright 2016 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.repo.events;

/**
 * Event consumer callback.
 * 
 * @author sglover
 *
 */
public class EventListener
{
    public void onEvent(Object event)
    {
        System.out.println("Got event " + event);
    }
}
