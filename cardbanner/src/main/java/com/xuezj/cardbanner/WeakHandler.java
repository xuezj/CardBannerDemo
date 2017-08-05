package com.xuezj.cardbanner;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuezj on 2017/7/29.
 */
public class WeakHandler {
    private final Handler.Callback mCallback; // hard reference to Callback. We need to keep callback in memory
    private final ExecHandler mExec;
    private Lock mLock = new ReentrantLock();
    @SuppressWarnings("ConstantConditions")
    final ChainedRef mRunnables = new ChainedRef(mLock, null);

    public WeakHandler() {
        mCallback = null;
        mExec = new ExecHandler();
    }

  
    public WeakHandler(Handler.Callback callback) {
        mCallback = callback; // Hard referencing body
        mExec = new ExecHandler(new WeakReference<>(callback)); // Weak referencing inside ExecHandler
    }


    public WeakHandler(Looper looper) {
        mCallback = null;
        mExec = new ExecHandler(looper);
    }

  
    public WeakHandler(Looper looper,Handler.Callback callback) {
        mCallback = callback;
        mExec = new ExecHandler(looper, new WeakReference<>(callback));
    }


    public final boolean post(Runnable r) {
        return mExec.post(wrapRunnable(r));
    }

    public final boolean postAtTime( Runnable r, long uptimeMillis) {
        return mExec.postAtTime(wrapRunnable(r), uptimeMillis);
    }


    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return mExec.postAtTime(wrapRunnable(r), token, uptimeMillis);
    }


    public final boolean postDelayed(Runnable r, long delayMillis) {
        return mExec.postDelayed(wrapRunnable(r), delayMillis);
    }


    public final boolean postAtFrontOfQueue(Runnable r) {
        return mExec.postAtFrontOfQueue(wrapRunnable(r));
    }

    public final void removeCallbacks(Runnable r) {
        final WeakRunnable runnable = mRunnables.remove(r);
        if (runnable != null) {
            mExec.removeCallbacks(runnable);
        }
    }

    public final void removeCallbacks(Runnable r, Object token) {
        final WeakRunnable runnable = mRunnables.remove(r);
        if (runnable != null) {
            mExec.removeCallbacks(runnable, token);
        }
    }


    public final boolean sendMessage(Message msg) {
        return mExec.sendMessage(msg);
    }


    public final boolean sendEmptyMessage(int what) {
        return mExec.sendEmptyMessage(what);
    }


    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return mExec.sendEmptyMessageDelayed(what, delayMillis);
    }


    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return mExec.sendEmptyMessageAtTime(what, uptimeMillis);
    }


    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return mExec.sendMessageDelayed(msg, delayMillis);
    }


    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return mExec.sendMessageAtTime(msg, uptimeMillis);
    }


    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return mExec.sendMessageAtFrontOfQueue(msg);
    }


    public final void removeMessages(int what) {
        mExec.removeMessages(what);
    }

    public final void removeMessages(int what, Object object) {
        mExec.removeMessages(what, object);
    }


    public final void removeCallbacksAndMessages(Object token) {
        mExec.removeCallbacksAndMessages(token);
    }


    public final boolean hasMessages(int what) {
        return mExec.hasMessages(what);
    }

    public final boolean hasMessages(int what, Object object) {
        return mExec.hasMessages(what, object);
    }

    public final Looper getLooper() {
        return mExec.getLooper();
    }

    private WeakRunnable wrapRunnable( Runnable r) {
        //noinspection ConstantConditions
        if (r == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        final ChainedRef hardRef = new ChainedRef(mLock, r);
        mRunnables.insertAfter(hardRef);
        return hardRef.wrapper;
    }

    private static class ExecHandler extends Handler {
        private final WeakReference<Callback> mCallback;

        ExecHandler() {
            mCallback = null;
        }

        ExecHandler(WeakReference<Callback> callback) {
            mCallback = callback;
        }

        ExecHandler(Looper looper) {
            super(looper);
            mCallback = null;
        }

        ExecHandler(Looper looper, WeakReference<Callback> callback) {
            super(looper);
            mCallback = callback;
        }

        @Override
        public void handleMessage( Message msg) {
            if (mCallback == null) {
                return;
            }
            final Handler.Callback callback = mCallback.get();
            if (callback == null) { // Already disposed
                return;
            }
            callback.handleMessage(msg);
        }
    }

    static class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate, WeakReference<ChainedRef> reference) {
            mDelegate = delegate;
            mReference = reference;
        }

        @Override
        public void run() {
            final Runnable delegate = mDelegate.get();
            final ChainedRef reference = mReference.get();
            if (reference != null) {
                reference.remove();
            }
            if (delegate != null) {
                delegate.run();
            }
        }
    }

    static class ChainedRef {
        ChainedRef next;
        ChainedRef prev;
        
        final Runnable runnable;
        
        final WeakRunnable wrapper;

        
        Lock lock;

        public ChainedRef( Lock lock,  Runnable r) {
            this.runnable = r;
            this.lock = lock;
            this.wrapper = new WeakRunnable(new WeakReference<>(r), new WeakReference<>(this));
        }

        public WeakRunnable remove() {
            lock.lock();
            try {
                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }
                prev = null;
                next = null;
            } finally {
                lock.unlock();
            }
            return wrapper;
        }

        public void insertAfter( ChainedRef candidate) {
            lock.lock();
            try {
                if (this.next != null) {
                    this.next.prev = candidate;
                }

                candidate.next = this.next;
                this.next = candidate;
                candidate.prev = this;
            } finally {
                lock.unlock();
            }
        }


        public WeakRunnable remove(Runnable obj) {
            lock.lock();
            try {
                ChainedRef curr = this.next; // Skipping head
                while (curr != null) {
                    if (curr.runnable == obj) { // We do comparison exactly how Handler does inside
                        return curr.remove();
                    }
                    curr = curr.next;
                }
            } finally {
                lock.unlock();
            }
            return null;
        }
    }
}