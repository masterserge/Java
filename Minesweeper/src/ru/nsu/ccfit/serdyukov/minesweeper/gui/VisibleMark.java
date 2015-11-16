package ru.nsu.ccfit.serdyukov.minesweeper.gui;

public class VisibleMark
{
	public static final int UNDEFINED = 0;

	public static final int FLAG = 1;
	public static final int QUESTION = 2;
	public static final int NONE = 3;
	public static final int ZERO = 4;
	public static final int ONE = 5;
	public static final int TWO = 6;
	public static final int THREE = 7;
	public static final int FOUR = 8;
	public static final int FIVE = 9;
	public static final int SIX = 10;
	public static final int SEVEN = 11;
	public static final int EIGHT = 12;

	public static final int MINE = 21;
	public static final int MINEFLAG = 22;
	public static final int MINEQUEST = 23;
	public static final int MINEWRONG = 24;
	public static final int MINERED = 25;
	public static final int MINENOMARK = 26;
	public static final int MINENOMARKQUEST = 27;

	public static final int LEFTSHADOW = 13;
	public static final int TOPSHADOW = 14;
	public static final int BOTHSHADOWS = 15;
	public static final int NOSHADOW = 16;

	public static final int BACKLIGHT = 17;
	public static final int NOBACKLIGHT = 18;

	public static final int PRESSED = 19;
	public static final int NOTPRESSED = 20;

	private int markType;
	private int backLight;
	private int shadows;
	private int pressed;

	public VisibleMark()
	{
		this(NONE, NOBACKLIGHT, NOSHADOW, NOTPRESSED);
	}
	public VisibleMark(int markType)
	{
		this(markType, NOBACKLIGHT, NOSHADOW, NOTPRESSED);
	}
	public VisibleMark(int markType, int backLight)
	{
		this(markType, backLight, NOSHADOW, NOTPRESSED);
	}
	public VisibleMark(int markType, int backLight, int shadows)
	{
		this(markType, backLight, shadows, NOTPRESSED);
	}
	public VisibleMark(int markType, int backLight, int shadows, int pressed)
	{
		this.markType = markType;
		this.backLight = backLight;
		this.shadows = shadows;
		this.pressed = pressed;
	}
	public void setMarkType(int markType)
	{
		this.markType = markType;
	}
	public void setBackLight(int backLight)
	{
		if (markType == VisibleMark.NONE || markType == VisibleMark.QUESTION || markType == VisibleMark.FLAG || backLight == NOBACKLIGHT)
			this.backLight = backLight;
	}
	public void setShadows(int shadows)
	{
		this.shadows = shadows;
	}
	public void setPressed(int pressed)
	{
		if (markType != VisibleMark.FLAG || pressed == NOTPRESSED)
			this.pressed = pressed;
	}
	public int getMarkType()
	{
		return markType;
	}
	public int getBackLight()
	{
		return backLight;
	}
	public int getShadows()
	{
		return shadows;
	}
	public int getPressed()
	{
		return pressed;
	}
	public boolean isIndented()
	{
		return pressed == PRESSED;
	}
	public int hashCode()
	{
		return shadows + 100 * backLight + 10000 * markType + 1000000 * pressed;
	}
	public boolean equals(Object obj)
	{
		if (obj == null || obj.getClass() != getClass())
			return false;
		final VisibleMark mark = (VisibleMark) obj;
		return (mark.markType == markType && mark.backLight == backLight && mark.shadows == shadows && mark.pressed == pressed);
	}
	public VisibleMark clone()
	{
		VisibleMark mark = new VisibleMark(markType, backLight, shadows, pressed);
		return mark;
	}
}