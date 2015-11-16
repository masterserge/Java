package ru.nsu.ccfit.serdyukov.minesweeper;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class RecordList implements Iterable<Record>
{
	private static final int maxSize = 5;
	private Vector<Record> records;

	public RecordList()
	{
		records = new Vector<Record>();
		for (int i = 0; i < maxSize; i++)
			records.add(new Record("Anonymous", 9999, new Date(0)));
	}
	public Record add(Record record)
	{
		int index = -1;
		int size = records.size();
		for (int i = 0; i < size; i++)
			if (record.getTime() < records.get(i).getTime())
			{
				records.add(index = i, record);
				break;
			}
		records.setSize(maxSize);
		for (int i = 0; i < records.size(); i++)
			records.get(i).setPlace(i);
		if (index == -1)
			return null;
		return records.get(index);
	}
	public Record get(int index)
	{
		return records.get(index);
	}
	public static int getMaxSize()
	{
		return maxSize;
	}
	@Override
	public Iterator<Record> iterator()
	{
		return records.iterator();
	}

}
