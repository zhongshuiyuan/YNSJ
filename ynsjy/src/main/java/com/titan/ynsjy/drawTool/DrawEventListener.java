package com.titan.ynsjy.drawTool;

import java.util.EventListener;


public interface DrawEventListener extends EventListener {

	void handleDrawEvent(DrawEvent event);
}
