package com.koscom.marketdata.bot.worker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorkerPool<T extends Thread & Worker> {
	private static Log logger = LogFactory.getLog(WorkerPool.class);

	private List<T> pool;
	
	private int size;

	private Class<T> clazz; 

	private boolean inited;
	
	
	public WorkerPool(Class<T> clazz, int size) {
		this.clazz = clazz;
		this.size = size;
		pool = new ArrayList<T>(this.size );
	}
	
	synchronized public void init() throws InstantiationException, IllegalAccessException {
		if (!inited) {
			
			logger.info("initialize worker pool.");
			
			for(int i = 0; i < size; i++) {
				T worker = clazz.newInstance();
				worker.init();
				worker.setName(worker.getClass().getName() + " - Worker " + (i+1));
				pool.add(worker);
			}
		}
		
		inited = true;
	}
	
	synchronized public void start() {
		if(!inited) return;

		logger.info("starting worker threads...");
		
		for(T worker : pool) {
			worker.start();
		}
	}
	
	synchronized public void destroy() {
		if(!inited) return;
				
		logger.info("interrupting worker threads...");
		
		for(T worker : pool) {
			try { worker.interrupt(); } catch (Exception e) { logger.error(e); }
		}
		
		pool.clear();
		pool = null;
		
		inited = false;

		logger.info("worker pool destroyed.");

	}
	
}
