/*
 * Copyright 2016 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.messaging.camel.routes;

import org.alfresco.events.types.NodeEvent;
import org.alfresco.events.types.TransactionCommittedEvent;
import org.alfresco.events.types.TransactionRolledBackEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Builds a Camel route for events to be sent out to a queue.
 * 
 * @author sglover
 *
 */
@Component
public class SendRouteBuilder extends SpringRouteBuilder
{
    private static Log logger = LogFactory.getLog(SendRouteBuilder.class);

    private String source = "direct-vm:testEvents";

    @Value("${messaging.txnManager}")
    private String txnManager;

    @Value("${messaging.dataFormat}")
    private String dataFormat;

    @Value("${messaging.queue}")
    public String target;

    @Override
    public void configure() throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Repo node events routes config: ");
            logger.debug("Source is "+source);
            logger.debug("target is "+target);
        }

        from(source)
        .routeId("SendRoute")
        .process(new Processor()
        {
            @Override
            public void process(Exchange exchange) throws Exception
            {
                // Add some headers to the outgoing message
                Message message = exchange.getIn();
                Object body = message.getBody();
                if(body instanceof NodeEvent)
                {
                    NodeEvent nodeEvent = (NodeEvent)body;
                    String txnId = nodeEvent.getTxnId();
                    exchange.getIn().setHeader("txnId", txnId);
                    exchange.getIn().setHeader("JMSXGroupID", txnId);
                }
                else if(body instanceof TransactionCommittedEvent)
                {
                    TransactionCommittedEvent txnEvent = (TransactionCommittedEvent)body;
                    String txnId = txnEvent.getTxnId();
                    exchange.getIn().setHeader("txnId", txnId);
                    exchange.getIn().setHeader("JMSXGroupID", txnId);
                }
                else if(body instanceof TransactionRolledBackEvent)
                {
                    TransactionRolledBackEvent txnEvent = (TransactionRolledBackEvent)body;
                    String txnId = txnEvent.getTxnId();
                    exchange.getIn().setHeader("txnId", txnId);
                    exchange.getIn().setHeader("JMSXGroupID", txnId);
                }
            }
        })
        .marshal(dataFormat)
        .transacted().ref(txnManager)
        .to(target)
        .end();
    }
}