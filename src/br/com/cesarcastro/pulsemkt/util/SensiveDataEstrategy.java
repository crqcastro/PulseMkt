package br.com.cesarcastro.pulsemkt.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

@SuppressWarnings("rawtypes")
public class SensiveDataEstrategy implements ExclusionStrategy{

	private String fieldName;
	private Class clazz;
	
	public SensiveDataEstrategy(String fieldName, Class clazz) {
		this.fieldName = fieldName;
		this.clazz = clazz;
	}
	
	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return (f.getDeclaringClass() == clazz && f.getName().equals(this.fieldName));
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
