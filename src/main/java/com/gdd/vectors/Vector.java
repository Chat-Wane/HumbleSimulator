package com.gdd.vectors;

interface Vector {

	public Integer get(Integer index);
	
	public void increment();
	
	public void incrementFrom(Vector v);
	
	public boolean isReady(Vector v);
	
	public boolean isLeq(Vector v);
	
}
