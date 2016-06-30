package com.hse.utility;

import java.util.Comparator;

public class MyComparator implements Comparator<Integer> {

	public final static int ASC = 1 ;
	public final static int DESC = -1 ;
	private int sort = ASC ;
	
	public MyComparator(int sort) {
		this.sort = sort ;
	}
	
	@Override
	public int compare(Integer lhs, Integer rhs) {
		if (lhs == null && rhs == null) {  
			return 0;   // lhs = rhs  
		} else if (lhs == null) {  
			return 1 * sort;   // lhs > rhs  
		} else if (rhs == null) {  
			return -1 * sort;  // lhs < rhs  
		}  
		return (lhs).compareTo(rhs) * sort;
	}

}
