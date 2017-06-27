package com.xckevin.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.text.TextUtils;

import com.xckevin.download.util.FileUtil;

public class DownloadOperator implements Runnable {

	// 100 kb
	private static final long REFRESH_INTEVAL_SIZE = 100 * 1024;

	private DownloadManager manager;

	private DownloadTask task;

	// already try times
	private int tryTimes;

	private volatile boolean pauseFlag;
	private volatile boolean stopFlag;

	private String filePath;

	DownloadOperator(DownloadManager manager, DownloadTask task) {
		this.manager = manager;
		this.task = task;
		this.tryTimes = 0;
	}

	void pauseDownload() {
		if(pauseFlag) {
			return ;
		}
		pauseFlag = true;
	}

	void resumeDownload() {
		if(!pauseFlag) {
			return ;
		}
		pauseFlag = false;
		synchronized (this) {
			notify();
		}
	}

	void cancelDownload() {
		stopFlag = true;
		resumeDownload();
	}

	@Override
	public void run() {
		do {
			RandomAccessFile raf = null;
			HttpURLConnection conn = null;
			InputStream is = null;
			try {
				raf = buildDownloadFile();
				conn = initConnection();

				conn.connect();

				task.setDownloadSavePath(filePath);
				if(task.getDownloadTotalSize() == 0) {
					task.setDownloadTotalSize(conn.getContentLength());
				}
				if(TextUtils.isEmpty(task.getMimeType())) {
					task.setMimeType(conn.getContentType());
				}
				manager.onDownloadStarted(task);


				is = conn.getInputStream();

				byte[] buffer = new byte[8192];
				int count = 0;
				long total = task.getDownloadFinishedSize();
				long prevTime = System.currentTimeMillis();
				long achieveSize = total;
				while(!stopFlag && (count = is.read(buffer)) != -1) {
					while(pauseFlag) {
						manager.onDownloadPaused(task);
						synchronized (this) {
							try {
								wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
								manager.onDownloadResumed(task);
							}
						}
					}

					raf.write(buffer, 0, count);
					total += count;

					long tempSize = total - achieveSize;
					if(tempSize > REFRESH_INTEVAL_SIZE) {
						long tempTime = System.currentTimeMillis() - prevTime;
						long speed = tempSize * 1000 / tempTime;
						achieveSize = total;
						prevTime = System.currentTimeMillis();
						task.setDownloadFinishedSize(total);
						task.setDownloadSpeed(speed);
						manager.updateDownloadTask(task, total, speed);
					}
				}
				task.setDownloadFinishedSize(total);

				if(stopFlag) {
					manager.onDownloadCanceled(task);
				} else {
					manager.onDownloadSuccessed(task);
				}
				break;
			} catch (IOException e) {
				e.printStackTrace();
				if(tryTimes > manager.getConfig().getRetryTime()) {
					manager.onDownloadFailed(task);
					break;
				} else {
					tryTimes ++;
					continue;
				}
			}
		} while(true);
	}

	private RandomAccessFile buildDownloadFile() throws IOException {
		String fileName = FileUtil.getFileNameByUrl(task.getUrl());
		File file = new File(manager.getConfig().getDownloadSavePath(), fileName);
		if(!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
			throw new IOException("cannot create download folder");
		}
		if(file.exists()) {

		}
		filePath = file.getAbsolutePath();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		if(task.getDownloadFinishedSize() != 0) {
			raf.seek(task.getDownloadFinishedSize());
		}

		return raf;
	}

	private HttpURLConnection initConnection() throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(task.getUrl()).openConnection();
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.setUseCaches(true);
		if(task.getDownloadFinishedSize() != 0) {
			conn.setRequestProperty("Range", "bytes=" + task.getDownloadFinishedSize() + "-");
		}

		return conn;
	}

}
