package ru.nsu.ccfit.serdyukov.minesweeper.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import ru.nsu.ccfit.serdyukov.minesweeper.gui.VisibleMark;
import static ru.nsu.ccfit.serdyukov.minesweeper.gui.VisibleMark.*;

public class ImageResources
{
	public static final int TOPSHADOWIMAGE = 0;
	public static final int LEFTSHADOWIMAGE = 1;
	public static final int ICON = 2;
	public static final int CLOCKSIGN = 3;
	public static final int MINESIGN = 4;
	public static final int PANEL7STYLE = 5;

	public static final int DIGIT0 = 25;
	public static final int DIGIT1 = 26;
	public static final int DIGIT2 = 27;
	public static final int DIGIT3 = 28;
	public static final int DIGIT4 = 29;
	public static final int DIGIT5 = 30;
	public static final int DIGIT6 = 31;
	public static final int DIGIT7 = 32;
	public static final int DIGIT8 = 33;
	public static final int DIGIT9 = 34;
	public static final int DIGITMINUS = 35;

	public static final int SMILEOK = 20;
	public static final int SMILEOH = 21;
	public static final int SMILEBAD = 22;
	public static final int SMILEWIN = 23;
	public static final int SMILEOKPRESSED = 24;

	public static final int TOPLEFTCORNER = 6;
	public static final int TOPRIGHTCORNER = 7;
	public static final int BOTTOMLEFTCORNER = 8;
	public static final int BOTTOMRIGHTCORNER = 9;
	public static final int MIDDLELEFTCORNER = 10;
	public static final int MIDDLERIGHTCORNER = 11;

	public static final int HORTOPFILLER = 12;
	public static final int HORMIDDLEFILLER = 13;
	public static final int HORBOTTOMFILLER = 14;
	public static final int VERTTOPLEFTFILLER = 15;
	public static final int VERTTOPRIGHTFILLER = 16;
	public static final int VERTBOTTOMLEFTFILLER = 17;
	public static final int VERTBOTTOMRIGHTFILLER = 18;

	public static final int PANEL = 19;

	public static final int PIXELFILLER = 36;

	public static final int SEVENSTYLE = 0;
	public static final int XPSTYLE = 1;
	public static final int CUSTOMSTYLE = 2;

	public static final int SKINHEIGHT = 122;
	public static final int SKINWIDTH = 144;

	public static final int SEVENSKINHEIGHT = 1506;
	public static final int SEVENSKINWIDTH = 2259;

	private static boolean wasInitialized = false;
	private static Map<Integer, Map<VisibleMark, Image>> styles;
	private static Map<Integer, Map<Integer, Image>> images;
	private static Image icon;

	private static void initXPStyle() throws IOException, InvalidSkinFileException
	{
		initCustomStyle(ImageResources.class.getResourceAsStream("xpskin.bmp"), XPSTYLE);
	}
	private static void init7Style() throws IOException, InvalidSkinFileException
	{
		Map<VisibleMark, Image> vimgs = new HashMap<VisibleMark, Image>();
		Map<Integer, Image> imgs = new HashMap<Integer, Image>();
		BufferedImage bi = null;

		URL url = ImageResources.class.getResource("7skin.jpg");
		if (url == null)
			throw new IOException();
		bi = ImageIO.read(url);

		if (bi.getHeight() != SEVENSKINHEIGHT || bi.getWidth() != SEVENSKINWIDTH)
			throw new InvalidSkinFileException();

		vimgs.put(new VisibleMark(NONE), bi.getSubimage(0, 0, 251, 251));
		vimgs.put(new VisibleMark(NONE, BACKLIGHT), bi.getSubimage(251, 0, 251, 251));
		vimgs.put(new VisibleMark(ZERO, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(502, 0, 251, 251));
		vimgs.put(new VisibleMark(ONE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(753, 0, 251, 251));
		vimgs.put(new VisibleMark(TWO, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(1004, 0, 251, 251));
		vimgs.put(new VisibleMark(THREE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(1255, 0, 251, 251));
		vimgs.put(new VisibleMark(FOUR, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(1506, 0, 251, 251));
		vimgs.put(new VisibleMark(FIVE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(1757, 0, 251, 251));
		vimgs.put(new VisibleMark(SIX, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(2008, 0, 251, 251));
		vimgs.put(new VisibleMark(SEVEN, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(0, 251, 251, 251));
		vimgs.put(new VisibleMark(EIGHT, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(251, 251, 251, 251));
		vimgs.put(new VisibleMark(FLAG), bi.getSubimage(502, 251, 251, 251));
		vimgs.put(new VisibleMark(FLAG, BACKLIGHT), bi.getSubimage(753, 251, 251, 251));
		vimgs.put(new VisibleMark(QUESTION), bi.getSubimage(1004, 251, 251, 251));
		vimgs.put(new VisibleMark(QUESTION, BACKLIGHT), bi.getSubimage(1255, 251, 251, 251));
		vimgs.put(new VisibleMark(QUESTION, NOBACKLIGHT, BOTHSHADOWS, PRESSED), bi.getSubimage(1506, 251, 251, 251));
		vimgs.put(new VisibleMark(QUESTION, NOBACKLIGHT, TOPSHADOW, PRESSED), bi.getSubimage(1757, 251, 251, 251));
		vimgs.put(new VisibleMark(QUESTION, NOBACKLIGHT, LEFTSHADOW, PRESSED), bi.getSubimage(2008, 251, 251, 251));
		vimgs.put(new VisibleMark(QUESTION, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(0, 502, 251, 251));
		vimgs.put(new VisibleMark(MINE, NOBACKLIGHT, BOTHSHADOWS, PRESSED), bi.getSubimage(251, 502, 251, 251));
		vimgs.put(new VisibleMark(MINE, NOBACKLIGHT, TOPSHADOW, PRESSED), bi.getSubimage(502, 502, 251, 251));
		vimgs.put(new VisibleMark(MINE, NOBACKLIGHT, LEFTSHADOW, PRESSED), bi.getSubimage(753, 502, 251, 251));
		vimgs.put(new VisibleMark(MINE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(1004, 502, 251, 251));
		vimgs.put(new VisibleMark(MINERED, NOBACKLIGHT, BOTHSHADOWS, PRESSED), bi.getSubimage(1255, 502, 251, 251));
		vimgs.put(new VisibleMark(MINERED, NOBACKLIGHT, TOPSHADOW, PRESSED), bi.getSubimage(1506, 502, 251, 251));
		vimgs.put(new VisibleMark(MINERED, NOBACKLIGHT, LEFTSHADOW, PRESSED), bi.getSubimage(1757, 502, 251, 251));
		vimgs.put(new VisibleMark(MINERED, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(2008, 502, 251, 251));
		vimgs.put(new VisibleMark(MINEWRONG, NOBACKLIGHT, BOTHSHADOWS, PRESSED), bi.getSubimage(0, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEWRONG, NOBACKLIGHT, TOPSHADOW, PRESSED), bi.getSubimage(251, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEWRONG, NOBACKLIGHT, LEFTSHADOW, PRESSED), bi.getSubimage(502, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEWRONG, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(753, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEFLAG, NOBACKLIGHT, BOTHSHADOWS, PRESSED), bi.getSubimage(1004, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEFLAG, NOBACKLIGHT, TOPSHADOW, PRESSED), bi.getSubimage(1255, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEFLAG, NOBACKLIGHT, LEFTSHADOW, PRESSED), bi.getSubimage(1506, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEFLAG, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(1757, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEQUEST, NOBACKLIGHT, BOTHSHADOWS, PRESSED), bi.getSubimage(2008, 753, 251, 251));
		vimgs.put(new VisibleMark(MINEQUEST, NOBACKLIGHT, TOPSHADOW, PRESSED), bi.getSubimage(0, 1004, 251, 251));
		vimgs.put(new VisibleMark(MINEQUEST, NOBACKLIGHT, LEFTSHADOW, PRESSED), bi.getSubimage(251, 1004, 251, 251));
		vimgs.put(new VisibleMark(MINEQUEST, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(502, 1004, 251, 251));
		vimgs.put(new VisibleMark(MINENOMARKQUEST, NOBACKLIGHT, NOSHADOW), bi.getSubimage(753, 1004, 251, 251));
		vimgs.put(new VisibleMark(MINENOMARK, NOBACKLIGHT, NOSHADOW), bi.getSubimage(1004, 1004, 251, 251));

		vimgs.put(new VisibleMark(NONE, NOBACKLIGHT, NOSHADOW, PRESSED), vimgs.get(new VisibleMark(ZERO, NOBACKLIGHT, NOSHADOW, PRESSED)));
		styles.put(SEVENSTYLE, vimgs);

		imgs.put(TOPSHADOWIMAGE, bi.getSubimage(1255, 1004, 251, 42));
		imgs.put(LEFTSHADOWIMAGE, bi.getSubimage(1506, 1004, 38, 251));

		imgs.put(CLOCKSIGN, bi.getSubimage(0, 1255, 251, 251));
		imgs.put(MINESIGN, bi.getSubimage(251, 1255, 251, 251));
		imgs.put(PANEL7STYLE, bi.getSubimage(1757, 1004, 251 * 2, 251));

		images.put(SEVENSTYLE, imgs);
	}
	public static void initCustomStyle(InputStream is, int style) throws IOException, InvalidSkinFileException
	{
		if (is == null)
			throw new IOException();

		Map<VisibleMark, Image> vimgs = new HashMap<VisibleMark, Image>();
		Map<Integer, Image> imgs = new HashMap<Integer, Image>();
		BufferedImage bi = null;

		bi = ImageIO.read(is);
		if (bi.getHeight() != SKINHEIGHT || bi.getWidth() != SKINWIDTH)
			throw new InvalidSkinFileException();

		vimgs.put(new VisibleMark(NONE), bi.getSubimage(0, 16, 16, 16));
		vimgs.put(new VisibleMark(ZERO, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(0, 0, 16, 16));
		vimgs.put(new VisibleMark(ONE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(16, 0, 16, 16));
		vimgs.put(new VisibleMark(TWO, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(32, 0, 16, 16));
		vimgs.put(new VisibleMark(THREE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(48, 0, 16, 16));
		vimgs.put(new VisibleMark(FOUR, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(64, 0, 16, 16));
		vimgs.put(new VisibleMark(FIVE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(80, 0, 16, 16));
		vimgs.put(new VisibleMark(SIX, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(96, 0, 16, 16));
		vimgs.put(new VisibleMark(SEVEN, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(112, 0, 16, 16));
		vimgs.put(new VisibleMark(EIGHT, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(128, 0, 16, 16));
		vimgs.put(new VisibleMark(FLAG), bi.getSubimage(48, 16, 16, 16));
		vimgs.put(new VisibleMark(QUESTION), bi.getSubimage(96, 16, 16, 16));
		vimgs.put(new VisibleMark(QUESTION, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(112, 16, 16, 16));
		vimgs.put(new VisibleMark(MINE, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(32, 16, 16, 16));
		vimgs.put(new VisibleMark(MINERED, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(80, 16, 16, 16));
		vimgs.put(new VisibleMark(MINEWRONG, NOBACKLIGHT, NOSHADOW, PRESSED), bi.getSubimage(64, 16, 16, 16));

		vimgs.put(new VisibleMark(NONE, NOBACKLIGHT, NOSHADOW, PRESSED), vimgs.get(new VisibleMark(ZERO, NOBACKLIGHT, NOSHADOW, PRESSED)));
		vimgs.put(new VisibleMark(MINENOMARKQUEST, NOBACKLIGHT, NOSHADOW), vimgs.get(new VisibleMark(FLAG)));
		vimgs.put(new VisibleMark(MINENOMARK, NOBACKLIGHT, NOSHADOW), vimgs.get(new VisibleMark(FLAG)));
		vimgs.put(new VisibleMark(MINEFLAG, NOBACKLIGHT, NOSHADOW, PRESSED), vimgs.get(new VisibleMark(FLAG)));
		vimgs.put(new VisibleMark(MINEQUEST, NOBACKLIGHT, NOSHADOW, PRESSED), vimgs.get(new VisibleMark(MINE, NOBACKLIGHT, NOSHADOW, PRESSED)));

		styles.put(style, vimgs);

		for (int i = 0; i < 5; i++)
			imgs.put(SMILEOK + i, bi.getSubimage(27 * i, 55, 26, 26));

		for (int i = 0; i < 11; i++)
			imgs.put(DIGIT0 + i, bi.getSubimage(12 * i, 33, 11, 21));

		imgs.put(TOPLEFTCORNER, bi.getSubimage(0, 82, 12, 11));
		imgs.put(TOPRIGHTCORNER, bi.getSubimage(15, 82, 12, 11));
		imgs.put(MIDDLELEFTCORNER, bi.getSubimage(0, 96, 12, 11));
		imgs.put(MIDDLERIGHTCORNER, bi.getSubimage(15, 96, 12, 11));
		imgs.put(BOTTOMLEFTCORNER, bi.getSubimage(0, 110, 12, 11));
		imgs.put(BOTTOMRIGHTCORNER, bi.getSubimage(15, 110, 12, 11));

		imgs.put(VERTTOPLEFTFILLER, bi.getSubimage(0, 94, 12, 1));
		imgs.put(VERTTOPRIGHTFILLER, bi.getSubimage(15, 94, 12, 1));
		imgs.put(VERTBOTTOMLEFTFILLER, bi.getSubimage(0, 108, 12, 1));
		imgs.put(VERTBOTTOMRIGHTFILLER, bi.getSubimage(15, 108, 12, 1));

		imgs.put(HORTOPFILLER, bi.getSubimage(13, 82, 1, 11));
		imgs.put(HORMIDDLEFILLER, bi.getSubimage(13, 96, 1, 11));
		imgs.put(HORBOTTOMFILLER, bi.getSubimage(13, 110, 1, 11));

		imgs.put(PANEL, bi.getSubimage(28, 82, 41, 25));

		imgs.put(PIXELFILLER, bi.getSubimage(70, 82, 1, 1));

		images.put(style, imgs);
	}

	public static synchronized void initResources() throws IOException, InvalidSkinFileException
	{
		if (wasInitialized == false)
		{
			wasInitialized = true;
			images = new HashMap<Integer, Map<Integer, Image>>();
			styles = new HashMap<Integer, Map<VisibleMark, Image>>();
			init7Style();
			initXPStyle();

			URL url = ImageResources.class.getResource("icon.png");
			if (url == null)
				throw new IOException();
			icon = ImageIO.read(url);
		}
	}
	public static Image getImage(int style, VisibleMark mark)
	{
		if (styles.containsKey(style))
			if (styles.get(style).containsKey(mark))
				return styles.get(style).get(mark);
		return null;
	}
	public static Image getImage(int style, int id)
	{
		if (images.containsKey(style))
			if (images.get(style).containsKey(id))
				return images.get(style).get(id);
		return null;
	}
	public static Image getIcon()
	{
		return icon;
	}
}
