package com.ks.topology;

import com.google.common.collect.ImmutableList;
import com.ks.bolt.CounterBolt;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

public class CounterTopology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			String kafkaZookeeper = "hyman0:2181,hyman1:2181,hyman2:2181/kafka";
			BrokerHosts brokerHosts = new ZkHosts(kafkaZookeeper);
			SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, "test", "/test", "id");
	        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
	        kafkaConfig.zkServers =  ImmutableList.of("hyman0","hyman1","hyman2");
	        kafkaConfig.zkPort = 2181;
			
	        //kafkaConfig.forceFromStart = true;
			
	        TopologyBuilder builder = new TopologyBuilder();
	        builder.setSpout("spout", new KafkaSpout(kafkaConfig), 2);
	        builder.setBolt("counter", new CounterBolt(),1).shuffleGrouping("spout");
	        
	        Config config = new Config();
	        config.setDebug(true);
	        
	        if(args!=null && args.length > 0) {
	            config.setNumWorkers(2);
	            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
	        } else {        
	            config.setMaxTaskParallelism(3);
	
	            LocalCluster cluster = new LocalCluster();
	            cluster.submitTopology("count-test", config, builder.createTopology());
	        
	            Thread.sleep(500000);
	
	            cluster.shutdown();
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
