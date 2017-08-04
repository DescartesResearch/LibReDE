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
