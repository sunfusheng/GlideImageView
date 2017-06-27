package com.xckevin.download;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class DownloadManager {

	private static final String TAG = "DownloadManager";

	//	static {
	//		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	//			DOWNLOAD_DIR = Env.ROOT_DIR + File.separator + "download";
	//		} else {
	//			DOWNLOAD_DIR = Environment.getDataDirectory() + File.separator + "com.huaqian" + File.separator + "databases";
	//		}
	//	}

	private static DownloadManager instance;
	
	private DownloadConfig config;

	private HashMap<DownloadTask, DownloadOperator> taskOperators = new HashMap<DownloadTask, DownloadOperator>();

	private HashMap<DownloadTask, DownloadListener> taskListeners = new HashMap<DownloadTask, DownloadListener>();

	private LinkedList<DownloadObserver> taskObservers = new LinkedList<DownloadObserver>();

	private DownloadProvider provider;

	private static Handler handler = new Handler();

	private ExecutorService pool;

	private DownloadManager() {
		
	}

	public static synchronized DownloadManager getInstance() {
		if(instance == null) {
			instance = new DownloadManager();
		}

		return instance;
	}
	
	public void init() {
		config = DownloadConfig.getDefaultDownloadConfig(this);
		provider = config.getProvider(this);
		pool = Executors.newFixedThreadPool(config.getMaxDownloadThread());
	}
	
	public void init(DownloadConfig config) {
		if(config == null) {
			init();
			return ;
		}
		this.config = config;
		provider = config.getProvider(this);
		pool = Executors.newFixedThreadPool(config.getMaxDownloadThread());
	}

	public DownloadConfig getConfig() {
		return config;
	}

	public void setConfig(DownloadConfig config) {
		this.config = config;
	}

	public void addDownloadTask(DownloadTask task) {
		addDownloadTask(task, null);
	}

	public void addDownloadTask(DownloadTask task, DownloadListener listener) {
		if(TextUtils.isEmpty(task.getUrl())) {
			throw new IllegalArgumentException("task's url cannot be empty");
		}
		if(taskOperators.containsKey(task)) {
			return ;
		}
		DownloadOperator operator = new DownloadOperator(this, task);
		taskOperators.put(task, operator);
		if(listener != null) {
			taskListeners.put(task, listener);
		}

		task.setStatus(DownloadTask.STATUS_PENDDING);
		DownloadTask historyTask = provider.findDownloadTaskById(task.getId());
		if(historyTask == null) {
			task.setId(config.getCreator().createId(task));
			provider.saveDownloadTask(task);
		} else {
			provider.updateDownloadTask(task);
		}

		//		new Thread(operator).start();

		pool.submit(operator);
	}

	public DownloadListener getDownloadListenerForTask(DownloadTask task) {
		if(task == null) {
			return null;
		}

		return taskListeners.get(task);
	}

	public void updateDownloadTaskListener(DownloadTask task, DownloadListener listener) {
		Log.v(TAG, "try to updateDownloadTaskListener");
		if(task == null || !taskOperators.containsKey(task)) {
			return ;
		}

		Log.v(TAG, "updateDownloadTaskListener");
		taskListeners.put(task, listener);
	}

	public void removeDownloadTaskListener(DownloadTask task) {
		Log.v(TAG, "try to removeDownloadTaskListener");
		if(task == null || !taskListeners.containsKey(task)) {
			return ;
		}

		Log.v(TAG, "removeDownloadTaskListener");
		taskListeners.remove(task);
	}

	public void pauseDownload(DownloadTask task) {
		Log.v(TAG, "pauseDownload: " + task.getName());
		DownloadOperator operator = taskOperators.get(task);
		if(operator != null) {
			operator.pauseDownload();
		}
	}

	public void resumeDownload(DownloadTask task) {
		Log.v(TAG, "resumeDownload: "+ task.getName());
		DownloadOperator operator = taskOperators.get(task);
		if(operator != null) {
			operator.resumeDownload();
		}
	}

	public void cancelDownload(DownloadTask task) {
		Log.v(TAG, "cancelDownload: "+ task.getName());
		DownloadOperator operator = taskOperators.get(task);
		if(operator != null) {
			operator.cancelDownload();
		} else {
			task.setStatus(DownloadTask.STATUS_CANCELED);
			provider.updateDownloadTask(task);
		}
	}

	public DownloadTask findDownloadTaskById(String id) {
		Iterator<DownloadTask> iterator = taskOperators.keySet().iterator();
		while(iterator.hasNext()) {
			DownloadTask task = iterator.next();
			if(task.getId().equals(id)) {
				Log.v(TAG, "findDownloadTaskByAdId from map");
				return task;
			}
		}

		Log.v(TAG, "findDownloadTaskByAdId from provider");
		return provider.findDownloadTaskById(id);
	}

	public List<DownloadTask> getAllDownloadTask() {
		return provider.getAllDownloadTask();
	}

	public void registerDownloadObserver(DownloadObserver observer) {
		if(observer == null) {
			return ;
		}
		taskObservers.add(observer);
	}

	public void unregisterDownloadObserver(DownloadObserver observer) {
		if(observer == null) {
			return ;
		}
		taskObservers.remove(observer);
	}
	
	public void close() {
		pool.shutdownNow();
	}

	public void notifyDownloadTaskStatusChanged(final DownloadTask task) {
		handler.post(new Runnable() {

			public void run() {
				for(DownloadObserver observer : taskObservers) {
					observer.onDownloadTaskStatusChanged(task);
				}
			}
		});
	}

	void updateDownloadTask(final DownloadTask task, final long finishedSize, final long trafficSpeed) {
		task.setStatus(DownloadTask.STATUS_RUNNING);
		final DownloadListener listener = taskListeners.get(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadUpdated(task, finishedSize, trafficSpeed);
				}
			}

		});
	}

	void onDownloadStarted(final DownloadTask task) {
		task.setStatus(DownloadTask.STATUS_RUNNING);
		final DownloadListener listener = taskListeners.get(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadStart(task);
				}
			}
		});
	}


	void onDownloadPaused(final DownloadTask task) {
		task.setStatus(DownloadTask.STATUS_PAUSED);
		final DownloadListener listener = taskListeners.get(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadPaused(task);
				}
			}
		});
	}

	void onDownloadResumed(final DownloadTask task) {
		task.setStatus(DownloadTask.STATUS_RUNNING);
		final DownloadListener listener = taskListeners.get(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadResumed(task);
				}
			}
		});
	}

	void onDownloadCanceled(final DownloadTask task) {
		task.setStatus(DownloadTask.STATUS_CANCELED);
		final DownloadListener listener = taskListeners.get(task);
		removeTask(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadCanceled(task);
				}

			}
		});
	}

	void onDownloadSuccessed(final DownloadTask task) {
		task.setStatus(DownloadTask.STATUS_FINISHED);
		final DownloadListener listener = taskListeners.get(task);
		removeTask(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadSuccessed(task);
				}
			}

		});
	}

	void onDownloadFailed(final DownloadTask task) {
		task.setStatus(DownloadTask.STATUS_ERROR);
		final DownloadListener listener = taskListeners.get(task);
		removeTask(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				provider.updateDownloadTask(task);
				if(listener != null) {
					listener.onDownloadFailed(task);
				}
			}
		});
	}

	void onDownloadRetry(final DownloadTask task) {
		final DownloadListener listener = taskListeners.get(task);
		handler.post(new Runnable() {

			@Override
			public void run() {
				if(listener != null) {
					listener.onDownloadRetry(task);
				}
			}
		});
	}

	private void removeTask(DownloadTask task) {
		taskOperators.remove(task);
		taskListeners.remove(task);
	}

}
