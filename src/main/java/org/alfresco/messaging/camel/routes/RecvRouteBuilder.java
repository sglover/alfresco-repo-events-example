/*
 * Copyright 2016 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.messaging.camel.routes;

import org.alfresco.repo.events.EventListener;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Builds a Camel route for receiving and processing events from a queue.
 * 
 * @author sglover
 *
 */
@Component
public class RecvRouteBuilder extends SpringRouteBuilder
{
    protected static Log logger = LogFactory.getLog(RecvRouteBuilder.class);

    @Value("${messaging.txnManager}")
    private String txnManager;

    @Value("${messaging.dataFormat}")
    private String dataFormat;

    @Value("${messaging.queue}")
    private String sourceQueue;

    @Autowired(required=true)
    @Qualifier("eventListener")
    private EventListener eventListener;

    @Override
    public void configure() throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("sourceQueue is "+sourceQueue);
        }

        from(sourceQueue)
        .routeId("RecvRoute")
        .transacted().ref(txnManager)
        .unmarshal(dataFormat)
        .process(new Processor()
        {
            @Override
            public void process(Exchange exchange) throws Exception
            {
                // Do some processing on the incoming message. In this case, calculate the broker
                // time i.e. how long it took for ActiveMQ to process the message.
                Message message = exchange.getIn();
                if(message instanceof JmsMessage)
                {
                    javax.jms.Message jmsMessage = ((JmsMessage)message).getJmsMessage();
                    if(jmsMessage instanceof ActiveMQMessage)
                    {
                        ActiveMQMessage activeMQMessage = (ActiveMQMessage)jmsMessage;
                        long brokerInTime = activeMQMessage.getBrokerInTime();
                        long brokerOutTime = activeMQMessage.getBrokerOutTime();
                        System.out.println("Broker time = " + (brokerOutTime - brokerInTime) + "ms");
                    }
                }
            }
        })
        // send the event to the onEvent method of the eventListener
        .bean(eventListener, "onEvent")
        .end();
    }
}