/*
 * Copyright 2016 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.repo.events;

import java.io.Serializable;

import org.alfresco.rest.framework.resource.UniqueId;

/**
 * Simple event bean.
 * 
 * @author sglover
 *
 */
public class TestEvent implements Serializable
{
    private static final long serialVersionUID = -2438796569622960695L;

    private String id;
    private String message;

    public TestEvent()
    {
    }

    public TestEvent(String message)
    {
        super();
        this.message = message;
    }

    @UniqueId
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "TestEvent [message=" + message + "]";
    }

}
