package ru.nsu.ccfit.serdyukov.minesweeper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.CRC32;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.ImageResources;

public class GameSettings
{
	public static final String PATH = "settings.txt";
	public static final String[] difficulties = { "Beginner", "Intermediate", "Advanced" };
	public static final int[] levelWidths = { 9, 16, 30 };
	public static final int[] levelHeights = { 9, 16, 16 };
	public static final int[] levelMines = { 10, 40, 99 };

	public static final int BEGINNER = 0;
	public static final int INTERMEDIATE = 1;
	public static final int ADVANCED = 2;
	public static final int CUSTOM = 3;

	public static final int MAXNICKLENGTH = 12;

	public static final int MINMINES = 10;
	public static final int MAXMINES = 668;
	public static final int MINWIDTH = 9;
	public static final int MAXWIDTH = 30;
	public static final int MINHEIGHT = 9;
	public static final int MAXHEIGHT = 24;

	private int width;
	private int height;
	private int mineCount;
	private boolean useQuestionMark;
	private int visualStyle;

	RecordList[] records;
	private int[] totalGamePlay;
	private int[] totalGameWon;
	private int[] streaks;

	public void writeSettings()
	{
		writeSettings(PATH);
	}
	public void writeSettings(String configFile)
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream os = null;
		FileOutputStream fos = null;
		try
		{
			os = new DataOutputStream(byteStream);
			os.writeInt(width);
			os.writeInt(height);
			os.writeInt(mineCount);
			os.writeBoolean(useQuestionMark);
			os.writeInt(visualStyle);
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < RecordList.getMaxSize(); j++)
				{
					os.writeUTF(records[i].get(j).getNick());
					os.writeDouble(records[i].get(j).getTime());
					os.writeLong(records[i].get(j).getDate().getTime());
				}

			for (int i = 0; i < 3; i++)
			{
				os.writeInt(totalGamePlay[i]);
				os.writeInt(totalGameWon[i]);
			}
			CRC32 crc = new CRC32();
			byte[] array = byteStream.toByteArray();
			crc.update(array);
			byteStream.reset();
			os.writeLong(crc.getValue());
			fos = new FileOutputStream(configFile);
			fos.write(byteStream.toByteArray());
			fos.write(array);
		}
		catch (IOException e)
		{
			System.out.println(e.toString());
		}
		finally
		{
			try
			{
				if (fos != null)
					fos.close();
			}
			catch (IOException e)
			{
				System.out.println(e.toString());
			}
		}
	}
	public GameSettings()
	{
		this(PATH);
	}
	public GameSettings(String configFile)
	{
		streaks = new int[3];
		records = new RecordList[3];
		for (int i = 0; i < 3; i++)
			records[i] = new RecordList();
		totalGamePlay = new int[3];
		totalGameWon = new int[3];
		File f = new File(configFile);
		if (f.exists())
			try
			{

				byte[] buffer = new byte[1 << 13];
				int bytesRead = 0;
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				FileInputStream fin = new FileInputStream(f);
				while ((bytesRead = fin.read(buffer)) != -1)
					bao.write(buffer, 0, bytesRead);

				byte[] data = bao.toByteArray();
				ByteArrayInputStream bin = new ByteArrayInputStream(data);
				DataInputStream is = new DataInputStream(bin);

				long crc = is.readLong();
				width = is.readInt();
				height = is.readInt();
				mineCount = is.readInt();
				useQuestionMark = is.readBoolean();
				visualStyle = is.readInt();
				for (int i = 0; i < 3; i++)
					for (int j = 0; j < RecordList.getMaxSize(); j++)
						records[i].add(new Record(is.readUTF(), is.readDouble(), new Date(is.readLong())));

				for (int i = 0; i < 3; i++)
				{
					totalGamePlay[i] = is.readInt();
					totalGameWon[i] = is.readInt();
				}
				CRC32 crc32 = new CRC32();
				crc32.update(bao.toByteArray(), (Long.SIZE + 7) / 8, data.length - (Long.SIZE + 7) / 8);
				if (crc != crc32.getValue())
					resetSettings();
			}
			catch (Exception e)
			{
				resetSettings();
			}
		else
			resetSettings();
		if (visualStyle == ImageResources.CUSTOMSTYLE)
			visualStyle = ImageResources.SEVENSTYLE;
	}
	public int getStreak(int gameType)
	{
		if (gameType == CUSTOM)
			return 0;
		return streaks[gameType];
	}
	public void resetStatistic()
	{
		for (int i = 0; i < 3; i++)
		{
			records[i] = new RecordList();
			totalGamePlay[i] = 0;
			totalGameWon[i] = 0;
			streaks[i] = 0;
		}
	}
	public void resetSettings()
	{
		width = levelWidths[BEGINNER];
		height = levelHeights[BEGINNER];
		mineCount = levelMines[BEGINNER];
		useQuestionMark = true;
		records = new RecordList[3];
		visualStyle = ImageResources.SEVENSTYLE;
		for (int i = 0; i < 3; i++)
		{
			records[i] = new RecordList();
			totalGamePlay[i] = 0;
			totalGameWon[i] = 0;
		}
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public void setMineCount(int mineCount)
	{
		this.mineCount = mineCount;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public int getMineCount()
	{
		return mineCount;
	}
	public boolean getUseQuestionMark()
	{
		return useQuestionMark;
	}
	public static int getGameType(int width, int height, int mineCount)
	{
		for (int i = BEGINNER; i <= ADVANCED; i++)
			if (levelWidths[i] == width && levelHeights[i] == height && levelMines[i] == mineCount)
				return i;

		return CUSTOM;
	}
	public int getGameType()
	{
		return getGameType(width, height, mineCount);
	}
	public int getVisualStyle()
	{
		return visualStyle;
	}
	public double getBestTime(int gameType)
	{
		return records[gameType].get(0).getTime();
	}
	public int getTotalGamePlayed(int gameType)
	{
		return totalGamePlay[gameType];
	}
	public int getGameWon(int gameType)
	{
		return totalGameWon[gameType];
	}
	public RecordList getRecords(int gameType)
	{
		if (gameType == CUSTOM)
			return null;
		return records[gameType];
	}
	public double getWinPercentage(int gameType)
	{
		if (getTotalGamePlayed(gameType) == 0)
			return 0;
		return (getGameWon(gameType) * 10000 / getTotalGamePlayed(gameType)) / 100.0;
	}
	public boolean isSuitableModel(GameModel model)
	{
		if (model == null)
			return false;
		GameField field = model.getField();
		return (field.getWidth() == width && field.getHeight() == height && field.getMineCount() == mineCount);
	}
	public Record registerGame(GameModel model)
	{
		if (model.getGameType() != CUSTOM)
		{
			totalGamePlay[model.getGameType()]++;
			if (model.isWin() == true)
			{
				streaks[model.getGameType()]++;
				totalGameWon[model.getGameType()]++;
				if (records[model.getGameType()].get(RecordList.getMaxSize() - 1).getTime() > model.getElapsedTime())
					return records[model.getGameType()].add(new Record("", model.getElapsedTime(), Calendar.getInstance().getTime()));
			}
			else if (model.isLoss() == true || model.isGameWasStarted() == true)
				streaks[model.getGameType()]--;
			else
				totalGamePlay[model.getGameType()]--;
		}
		return null;
	}
	public void setVisualStyle(int visualStyle)
	{
		this.visualStyle = visualStyle;
	}
	public void setUseQuestionMark(boolean useQuestionMark)
	{
		this.useQuestionMark = useQuestionMark;
	}
	public String getLastNick()
	{
		Record record = new Record("Anonymous", 0, new Date(0));
		for (int i = 0; i < 3; i++)
			for (Record r: records[i])
				if ((r.getDate().after(record.getDate()) || r.getDate().equals(record.getDate())) && r.getNick().equals("") == false)
					record = r;
		return record.getNick();
	}
	public static boolean isValidParameters(int width, int height, int mines)
	{
		if (mines > MAXMINES || mines < MINMINES || width < MINWIDTH || width > MAXWIDTH || height > MAXHEIGHT || height < MINHEIGHT || height * width <= mines)
			return false;
		return true;
	}
}
