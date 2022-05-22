package com.anbu.pubsubdemo.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;

@RestController
public class PubSubDemoController {
	
	String message;
	@GetMapping("getMessage")
	public String getMessage() {
		return "Message from GCP "+message;
	}
	
	@Bean
	public PubSubInboundChannelAdapter messageAdapter (
			@Qualifier("inputChannel") MessageChannel inputChannel,
			PubSubTemplate template
			) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(template, "pubSubDemoTopic-sub");
		adapter.setOutputChannel(inputChannel);
		return adapter;
	}
	
	@Bean
	MessageChannel inputChannel() {
		return new DirectChannel();
	}
	
	@ServiceActivator(inputChannel = "inputChannel")
	public void receiveMessage(String paylod) {
		this.message = paylod;
	}

}
