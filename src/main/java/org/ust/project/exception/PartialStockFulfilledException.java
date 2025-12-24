package org.ust.project.exception;

public class PartialStockFulfilledException extends RuntimeException {
	public PartialStockFulfilledException(String message)
	{
		super(message);
	}
}
