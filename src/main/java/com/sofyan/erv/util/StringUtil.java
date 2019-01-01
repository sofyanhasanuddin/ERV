package com.sofyan.erv.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public class StringUtil extends StringUtils {
	
	private StringUtil() {}
	
	public static String getSimpleClassName(String classWithPackage) {
		
		List<String> l = Arrays.asList( classWithPackage.split("\\."));
		
		return CollectionUtils.isEmpty(l) ? classWithPackage : l.get( l.size() - 1 );
		
	}
	
	public static String getGenericClass(String classWithGeneric) {
		
		String generic = StringUtils.substringBetween( classWithGeneric.toString(), "<",">");
		if( isEmpty(generic) )
			throw new RuntimeException("One to Many list does not have generic type");
		
		return generic;
		
	}
	
	public static String getPackageFromClass(String classWithPackage) {
		
		String temp[] = classWithPackage.split("\\.");
		if( temp == null || temp.length == 1 )
			throw new RuntimeException("Not a valid class with package : " + classWithPackage);
		
		String pkg[] = Arrays.copyOf( temp, temp.length - 1);
		
		return join( pkg, ".");
		
	}
	

}
