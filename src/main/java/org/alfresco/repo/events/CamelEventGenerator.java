/*
 * Copyright 2016 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.repo.events;

import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.gytheio.messaging.MessagingException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author sglover
 *
 */

public class CamelEventGenerator implements EventGenerator
{
    protected static final String ERROR_SENDING = "Could not send message";
    
    protected ProducerTemplate producer;
    protected String endpoint;
    protected ObjectMapper objectMapper;

    /**
     * The Camel producer template
     * 
     * @param producer
     */
    public void setProducer(ProducerTemplate producer)
    {
        this.producer = producer;
    }

    /**
     * The Camel endpoint for initial delivery of the messages into the Camel context which
     * can then be routed as needed
     * 
     * @param endpoint
     */
    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }
    
    /**
     * Optional object mapper to be used programatically in {@link #send(Object, String)}
     * or {@link #send(Object, String, Map)} methods when an alternate endpoint is specified.
     * 
     * @param objectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendEvent(Object event)
    {
        try
        {
            
            if (objectMapper != null && !(event instanceof String))
            {
                event = objectMapper.writeValueAsString(event);
            }
            //amqp:queue:testQueue?jmsMessageType=Text
            producer.sendBody("direct-vm:testEvents", event);
        }
        catch (Exception e)
        {
            throw new MessagingException(ERROR_SENDING, e);
        }
    }
}