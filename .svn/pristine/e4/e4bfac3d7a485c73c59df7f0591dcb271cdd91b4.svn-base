package com.hangyi.zd.activity.newplay;


public class LoadedList {
	public static LoadedList instance = new LoadedList();
	public static final int preLoadedPlay = 20;
	public static int loadSize = 0;
	public static int needTotalSize = 0;

	private LoadedList() {
	}

	public static LoadedList getInstance() {

		return instance;
	}

	public synchronized int loadedSize() {
		return loadSize;
	}
	
	public synchronized void add(int size){
		loadSize += size;
	}

}
