package tools.descartes.librede.datasource;

import java.io.IOException;

/**
 * Provides an interface to handle stream reading into an array which can be used as buffer.
 * @author torsten
 *
 */
public interface Stream {
	/**
	 * Open the stream.
	 * @throws IOException
	 */
	public abstract void open() throws IOException;
	/**
	 * Read data to the given array from the stream
	 * @param data - the output array
	 * @param offset - the offset of the actual read 
	 * @param length - the length that should be read
	 * @return - the number of bytes we read or -1 if there is nothing to read.
	 * @throws IOException
	 */
	public abstract int read(byte[] data, int offset, int length) throws IOException;
	/**
	 * Close the stream.
	 * @throws IOException
	 */
	public abstract void close() throws IOException;
}
