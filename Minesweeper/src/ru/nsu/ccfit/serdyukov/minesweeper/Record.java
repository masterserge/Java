package ru.nsu.ccfit.serdyukov.minesweeper;

import java.util.Date;

public class Record
{
	private double time;
	private String nick;
	private Date date;
	private int place;

	public Record(String nick, double time, Date date, int place, int r)
	{
		this.time = time;
		this.nick = nick;
		this.date = date;
		this.place = place;
	}
	public Record(String nick, double time, Date date)
	{
		this(nick, time, date, 0, 0);
	}
	public double getTime()
	{
		return time;
	}
	public Date getDate()
	{
		return date;
	}
	public String getNick()
	{
		return nick;
	}
	public int getPlace()
	{
		return place;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public void setPlace(int place)
	{
		this.place = place;
	}
	public void setNick(String nick)
	{
		this.nick = nick;
	}
}