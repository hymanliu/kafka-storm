package com.hyman.storm.topology;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hyman.storm.bolt.TransferBolt;
import com.hyman.storm.bolt.WriterBolt;
import com.hyman.storm.spout.RandomWordSpout;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class TopoMain {

	private static final Log log = LogFactory.getLog(TopoMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("random", new RandomWordSpout(), 2);
		builder.setBolt("transfer", new TransferBolt(), 4).shuffleGrouping("random");
		builder.setBolt("writer", new WriterBolt(), 4).fieldsGrouping("transfer", new Fields("word"));
		Config conf = new Config();
		conf.setNumWorkers(2);
		conf.setDebug(true);
		log.warn("$$$$$$$$$$$ submitting topology...");
		StormSubmitter.submitTopology("life-cycle", conf, builder.createTopology());
		log.warn("$$$$$$$4$$$ topology submitted !");
	}

}
