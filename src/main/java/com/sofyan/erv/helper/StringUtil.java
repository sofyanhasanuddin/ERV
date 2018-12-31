package com.sofyan.erv.helper;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public class StringUtil extends StringUtils {
	
	private StringUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getSimpleClassName(String classWithPackage) {
		
		List<String> l = Arrays.asList( classWithPackage.split("."));
		
		return CollectionUtils.isEmpty(l) ? classWithPackage : l.get( l.size() - 1 );
		
	}
	
	public static String getGenericClass(String classWithGeneric) {
		
		String generic = StringUtils.substringBetween( classWithGeneric.toString(), "<",">");
		if( isEmpty(generic) )
			throw new RuntimeException("One to Many list does not have generic type");
		
		return generic;
		
	}
	

}
