package com.example.demo.exception;

public class NoDataFoundInDatabaseException extends RuntimeException {
		private	 String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public NoDataFoundInDatabaseException(String message) {
			super();
			this.message = message;
		}
		
}
