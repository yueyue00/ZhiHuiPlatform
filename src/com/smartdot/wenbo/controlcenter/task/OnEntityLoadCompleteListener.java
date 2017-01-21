package com.smartdot.wenbo.controlcenter.task;

public interface OnEntityLoadCompleteListener<T> extends OnErrorListener {
	void onEntityLoadComplete(Base base);

	void onError(T entity);
}
