/*
 * 
 *Copyright 2015 Solid Syntax (www.SolidSyntax.net)
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package net.solidsyntax.jdbc.pool.interceptor;

import org.apache.tomcat.jdbc.pool.interceptor.AbstractQueryReport;

/**
 * CountQueryReport is an extension for the Tomcat JDBC connection pool.
 * CountQueryReport counts the number of statements executed between two defined points.
 * The range in which statements are counted is demarcated by the invocation of
 * {@link #resetCount() resetCount} and {@link #numberOfStatements() numberOfStatements}.
 * Statements are counted separated for each Thread.
 * 
 * @author      Hans Maes
 * @version     1.0
 */
public class CountQueryReport extends AbstractQueryReport {
	private static final ThreadLocalCounter counter = new ThreadLocalCounter();
	
	@Override
	protected void prepareStatement(String sql, long time) {
		counter.increment();
	}

	@Override
	protected void prepareCall(String query, long time) {
		
	}

	@Override
	public void closeInvoked() {
		
	}
	
	public static int numberOfStatements() {
		return counter.get();
	}
	
	public static void resetCount() {
		counter.reset();
	}
	
	private static class ThreadLocalCounter extends ThreadLocal<Integer>{
		@Override
		protected Integer initialValue() {
			return 0;
		}
		
		public void reset() {
			set(initialValue());
		}
		
		public void increment() {
			Integer value = get();
			set(value + 1);
		}
	}

}
