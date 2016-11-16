package eWorld.database.impl;

import java.util.ArrayList;

public class Util {
	
	public static String composePreparedStatementWherePart(Column[] columns) {
		assert null != columns;
		
		String string = "";
		
		for (int i = 0; i < columns.length; i++) {
			if (0 != i) {
				string += " AND ";
			}
			
			string += columns[i].getName() + " = ?";	// FIXME ? <-> '?'
		}
		
		return string;
	}
	
	public static String composePreparedStatementWherePart(ArrayList<Column> columns) {
		return composePreparedStatementWherePart(columns.toArray(new Column[0]));
	}
	
	/**
	 * 
	 * @param columns
	 * @return names of all columns separated through commas
	 */
	public static String composePreparedStatementSelectPart(Column[] columns) {
		assert null != columns;
		
		String string = "";
		
		for (int i = 0; i < columns.length; i++) {
			if (0 != i) {
				string += ", ";
			}
			
			string += columns[i].getName();
		}
		
		return string;
	}
	
	/**
	 * constructs a new array that contains the elements of argument objects and argument object (in that order, object is appended to objects)
	 * @param objects
	 * @param object
	 * @return the constructed array
	 */
	public static Object[] appendToArray(Object[] objects, Object object) {
		assert null != objects;
		assert null != object;
		
		Object[] re = new Object[objects.length + 1];
		
		for (int i = 0; i < objects.length; i++) {
			re[i] = objects[i];
		}
		
		re[objects.length] = object;
		
		return re;
	}
	
	/**
	 * compares through '=='
	 * @param columns
	 * @param column
	 * @return true if columns contains column
	 */
	public static boolean contains(final Column[] columns, final Column column) {
		assert null != columns;
		assert null != column;
		
		for (final Column col : columns) {
			if (col == column) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * FIXME how to convert an object to a long when you know that it should be a long?
	 * @param objects
	 * @return formatted like: (obj1, obj2, ..., obj_n)
	 */
	public static String toTuple(Object... objects) {
		assert null != objects;
		
		String string = "(";
		
		for (int i = 0; i < objects.length; i++) {
			if (i != 0) {
				string += ", ";
			}
			
//			string += (long)objects[i];	// doesn't work
//			string += (Long)objects[i];	// doesn't work
			string += objects[i];	// doesn't work, but no error
		}
		
		string += ")";
		
		return string;
	}
}
