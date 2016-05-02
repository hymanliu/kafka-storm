package com.hyman.realtime.kafka;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class kafkaProducer extends Thread{

	private String topic;
	
	public kafkaProducer(String topic){
		super();
		this.topic = topic;
	}
	
	
	@Override
	public void run() {
		Producer<String, String> producer = createProducer();
		int i=0;
		while(true){
			producer.send(new KeyedMessage<String, String>(topic, "message: " + i));
			System.out.println("hello: " + i);
			try {
				TimeUnit.SECONDS.sleep(1);
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Producer<String, String> createProducer() {
		
		Properties props = new Properties();
		props.put("zookeeper.connect", "hyman0:2181,hyman1:2181,hyman2:2181/kafka");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("producer.type", "async");
		props.put("compression.codec", "1");
		props.put("metadata.broker.list", "hyman0:9092,hyman1:9092,hyman2:9092");
		
		
		return new Producer<String, String>(new ProducerConfig(props));
	 }
	
	public static void main(String[] args) {
		new kafkaProducer("test").start();
		
	}
	 
}
