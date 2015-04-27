package fr.android.scaron.diaspdroid.model;

/**
 * @author Benjamin Neff
 */
public interface ProgressListener {

	/**
	 * Transferred Callback.
	 * 
	 * @param bytes
	 *            transferred bytes
	 */
	void transferred(long bytes);
}
