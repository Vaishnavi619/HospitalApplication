package com.example.demo.dto;



public class AvailableSlotDto {

	 private String startTime;   // "16:00:00"
	    private String endTime;     // "16:15:00"

	    private String slot;        // "16:00 - 16:15"
	    private String slotTime;    // "16:00:00"
	    private String display;     // "4:00 PM – 4:15 PM"
		public final String getStartTime() {
			return startTime;
		}
		public final void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public final String getEndTime() {
			return endTime;
		}
		public final void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public final String getSlot() {
			return slot;
		}
		public final void setSlot(String slot) {
			this.slot = slot;
		}
		public final String getSlotTime() {
			return slotTime;
		}
		public final void setSlotTime(String slotTime) {
			this.slotTime = slotTime;
		}
		public final String getDisplay() {
			return display;
		}
		public final void setDisplay(String display) {
			this.display = display;
		}
		public AvailableSlotDto() {
			super();
		}

  
    
}
