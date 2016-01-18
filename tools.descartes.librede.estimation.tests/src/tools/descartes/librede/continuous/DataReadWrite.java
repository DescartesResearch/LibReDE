package tools.descartes.librede.continuous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataReadWrite implements Runnable {
	private DataReader dr;
	private DataWriter dw;
	private int linesToCopy = 1;

	public DataReadWrite(File sourceFile, File destinationFile) {
		try {
			this.dr = new DataReader(sourceFile);
			this.dw = new DataWriter(destinationFile);
			dw.emptyFile();
		} catch (IOException e) {
			System.err.println("Error emptying file");
		}
	}
	
	

	public DataReader getDataReader() {
		return dr;
	}



	public void setLinesToCopy(int linesToCopy) {
		this.linesToCopy = linesToCopy;
	}



	public void run() {
		if (!dr.isFileEnd()) {
			try {
				List<String> readLines = dr.readLines(linesToCopy);
				dw.writeLines(readLines);
			} catch (IOException e) {
				System.err.println("IO Error");
			}
		}
	}

}

class DataWriter {

	private File destinationFile;
	private BufferedWriter bw;

	public DataWriter(String outputFile) {
		this.destinationFile = new File(outputFile);
	}

	public DataWriter(File outputFile) {
		this.destinationFile = outputFile;
	}

	private void openDestinationFile(boolean append) throws IOException {
		bw = new BufferedWriter(new FileWriter(destinationFile, append));
	}

	private void closeDestinationFile() throws IOException {
		bw.close();
	}

	synchronized public void writeLines(List<String> lines) throws IOException {
		openDestinationFile(true);
		for (String line : lines) {
			bw.write(line);
			bw.write("\n");
		}
		closeDestinationFile();
	}
	synchronized public void emptyFile() throws IOException {
		openDestinationFile(false);
			bw.write("");
		closeDestinationFile();
	}


}

class DataReader {
	private File sourceFile;
	private BufferedReader br;
	private boolean fileEnd = false;

	public DataReader(String inputFile) {
		this.sourceFile = new File(inputFile);
	}

	public boolean isFileEnd() {
		return fileEnd;
	}

	public DataReader(File inputFile) {
		this.sourceFile = inputFile;
	}

	private void openSourceFile() throws FileNotFoundException {
		br = new BufferedReader(new FileReader(sourceFile));
	}

	private void closeSourceFile() throws IOException {
		br.close();
	}

	synchronized public LinkedList<String> readLines(int linesToCopy) throws IOException {
			if (br == null) {
				openSourceFile();
			}
		LinkedList<String> lines = new LinkedList<String>();
		for (int i = 0; i < linesToCopy; i++) {
			String line;
			line = br.readLine();
			if (line != null) {
				lines.add(line);
			} else {
				fileEnd = true;
			}
		}
		if (fileEnd) {
			System.out.println("Datei erfolgreich eingelesen");
			closeSourceFile();
		}
		return lines;
	}

}
