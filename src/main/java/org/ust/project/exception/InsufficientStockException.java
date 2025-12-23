package org.ust.project.exception;

public class InsufficientStockException extends Exception {
	
	public InsufficientStockException(String name)
	{
		super("Insufficient Stock for item :"+name);
	}
}
