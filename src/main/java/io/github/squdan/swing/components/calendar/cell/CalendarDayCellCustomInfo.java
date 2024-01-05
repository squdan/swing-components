package io.github.squdan.swing.components.calendar.cell;

import java.util.UUID;

public interface CalendarDayCellCustomInfo {

	public UUID getId();

	public String getInfoDescription();

	public String getTimeAsString();

	public String getCustomInfoAsString();

}
