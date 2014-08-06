package com.mh.jxltool.constant;

import java.util.HashMap;
import java.util.Map;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;

/**
 * 
 * @author jasonyao
 * 
 */
public class StaticVariables {

	private static Map<String, Colour> hmColour = new HashMap<String, Colour>();
	private static Map<String, VerticalAlignment> hmVertical = new HashMap<String, VerticalAlignment>();
	private static Map<String, Alignment> hmAlign = new HashMap<String, Alignment>();

	public static Colour getColour(String colour) {
		return hmColour.get(colour);
	}

	public static VerticalAlignment getVertical(String vertical) {
		return hmVertical.get(vertical);
	}

	public static Alignment getAlign(String align) {
		return hmAlign.get(align);
	}

	static {
		hmColour.put("GRAY_25", Colour.GRAY_25);
		hmColour.put("GRAY_50", Colour.GRAY_50);
		hmColour.put("GRAY_80", Colour.GRAY_80);
		hmColour.put("RED", Colour.RED);
		hmColour.put("GOLD", Colour.GOLD);
		hmColour.put("GREEN", Colour.GREEN);
		hmColour.put("BLACK", Colour.BLACK);
		hmColour.put("BLUE", Colour.BLUE);
		hmColour.put("YELLOW", Colour.YELLOW);
		hmColour.put("PINK", Colour.PINK);
		hmColour.put("PALE_BLUE", Colour.PALE_BLUE);
		hmColour.put("WHITE", Colour.WHITE);

		hmVertical.put("TOP", VerticalAlignment.TOP);
		hmVertical.put("CENTRE", VerticalAlignment.CENTRE);
		hmVertical.put("JUSTIFY", VerticalAlignment.JUSTIFY);
		hmVertical.put("BOTTOM", VerticalAlignment.BOTTOM);

		hmAlign.put("CENTRE", Alignment.CENTRE);
		hmAlign.put("FILL", Alignment.FILL);
		hmAlign.put("GENERAL", Alignment.GENERAL);
		hmAlign.put("JUSTIFY", Alignment.JUSTIFY);
		hmAlign.put("LEFT", Alignment.LEFT);
		hmAlign.put("RIGHT", Alignment.RIGHT);
	}

}
