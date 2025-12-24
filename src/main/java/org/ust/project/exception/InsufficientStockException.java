package org.ust.project.exception;

public class InsufficientStockException extends RuntimeException {
	
	public InsufficientStockException(String name)
	{
		super("Insufficient Stock for item :"+name);
	}
}
