package org.ust.project.exception;

public class ConsultationNotFoundException extends RuntimeException{
	
	public ConsultationNotFoundException(Long id)
	{
		super("Consultation does not exist with Id : "+id);
	}
}
