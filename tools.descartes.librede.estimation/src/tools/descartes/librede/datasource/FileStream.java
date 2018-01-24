/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.datasource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Provides a class, where the Stream is a File.
 * @author torsten
 *
 */
public class FileStream implements Stream {
	
	/**
	 * The file the Stream handles.
	 */
	private final File file;
	/**
	 * The pointer to the actual position in the file.
	 */
	private RandomAccessFile input;
	
	/**
	 * The constructor for the FileStream.
	 * @param path - the path to the file we want to read.
	 * @throws IOException
	 */
	public FileStream(File path) throws IOException {
		this.file = path;
	}
	
	/**
	 * Open the file pointer. Read-only.
	 */
	public void open() throws IOException {
		input = new RandomAccessFile(file, "r");			
	}

	/**
	 * Read from the file, like described in the interface.
	 */
	@Override
	public int read(byte[] data, int offset, int length) throws IOException {
		//get the actual file length
		RandomAccessFile anotherFile = new RandomAccessFile(file, "r");
		long actualsize = anotherFile.length();
		anotherFile.close();
		//update the file stream, if file has changed.
		if(input.length()<actualsize){
			//get the actual position of the pointer in the file
			long pos = input.getFilePointer();
			//close the actual session
			input.close();
			input=null;
			//initiate a new one
			input = new RandomAccessFile(file, "r");
			//return to the previous position
			input.seek(pos);
		}
		//check if we are at the end of the file
		if (input.getFilePointer() >= input.length()) {
			//we are at the end therefore return -1
			return -1;
		}
		//read the data from the file and automatically increase the pointer.
		return input.read(data, offset, length);
	}
	/**
	 * Close the file stream.
	 */
	@Override
	public void close() throws IOException {
		input.close();			
	}
	/**
	 * Give information about the actual file.
	 */
	@Override
	public String toString() {
		return file.toString();
	}
	/**
	 * For comparison.
	 */
	@Override
	public int hashCode() {
		return ((file == null) ? 0 : file.hashCode());
	}
	/**
	 * For comparison.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileStream other = (FileStream) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
	/**
	 * Get the reference to the acutal file.
	 * @return - the file.
	 */
	public File getFile() {
		return file;
	}
}
