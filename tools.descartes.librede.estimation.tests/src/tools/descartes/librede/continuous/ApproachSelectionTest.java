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
package tools.descartes.librede.continuous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tools.descartes.librede.Librede;
import tools.descartes.librede.LibredeVariables;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.datasource.IDataSource;

public class ApproachSelectionTest {
	private static String basePath = null;
	private static FileSystem fs;

	public static void main(String[] args) {
		fs = FileSystems.getDefault();
		Path path = fs.getPath(basePath);
		PathMatcher csvMatcher = FileSystems.getDefault().getPathMatcher("glob:**.csv");
		PathMatcher configMatcher = FileSystems.getDefault().getPathMatcher("glob:**.librede");
		iterateDirectories(path, csvMatcher, configMatcher);
	}

	private static void iterateDirectories(Path path, PathMatcher csvMatcher, PathMatcher configMatcher) {
		// skip .csv and .librede files
		if (!csvMatcher.matches(path) && !configMatcher.matches(path)) {
			List<Path> sourcesRespTime = new LinkedList<Path>();
			List<Path> sourcesUtil = new LinkedList<Path>();
			DirectoryStream<Path> stream = null;
			try {
				stream = Files.newDirectoryStream(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Iterator<Path> iter = stream.iterator();
			// iterate over all files in this directory
			while (iter.hasNext()) {
				// get child
				Path childPath = iter.next();
				// if file is a .csv
				if (csvMatcher.matches(childPath)) {
					// if file contains the samples for response time add path
					// to sourcesRespTime List
					if (childPath.toString().contains("RESPONSE_TIME") && childPath.toString().contains("WC")) {
						sourcesRespTime.add(childPath);
						// if file contains the samples for utilization add path
						// to sourcesUtil List
					} else if (childPath.toString().contains("UTILIZATION")) {
						sourcesUtil.add(childPath);
					}
					// if child is a directory
				} else {
					// start iteration through directories recursively
					iterateDirectories(childPath, csvMatcher, configMatcher);
				}
			}

			// all child pathes are read now
			// find start- and end-timestamp by checking start- and
			// end-timestamp
			// of every file
			long starttime = Long.MAX_VALUE;
			long endtime = 0;
			for (Path sourceFile : sourcesRespTime) {
				BufferedReader br = null;
				String line = null;
				String lastLine = null;
				try {
					br = new BufferedReader(new FileReader(sourceFile.toString()));
					// read header line into lastline
					lastLine = br.readLine();
					// read first data line into line
					line = br.readLine();
					// read start timestamp out of line
					long startLong = Long.MAX_VALUE;
					if (line != null && !line.isEmpty() && lastLine != null) {
						String start = line.split(";")[0];
						startLong = new BigDecimal(start).longValue();
					}

					if (startLong < starttime) {
						starttime = startLong;
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println(sourceFile.toString());
				}

				// go to last line
				while (line != null) {
					lastLine = line;
					try {
						line = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// read end-timestamp out of lastline
				long endLong = 0;
				// there are files wihtout any sampe data but only with headers
				// to avoid casting of "Timestamp" to a BigDecimal
				if (!lastLine.contains("Timestamp")) {
					String end = lastLine.split(";")[0];
					try {
						endLong = new BigDecimal(end).longValue();
					} catch (NumberFormatException nfe) {
						System.out.println(end);
					}
				}
				if (endLong > endtime) {
					endtime = endLong;
				}
			}

			// only create config and run estimation if there are source
			// .csv-Files in this direcotry
			if (sourcesRespTime.size() > 0 && sourcesUtil.size() > 0) {
				System.out.println(path);
				// System.out.println(sourcesRespTime.size());
				// System.out.println(sourcesUtil.size());
				File confFile = new File(path + "/config.librede");
				if (confFile.exists()) {
					confFile.delete();
				}
				// if (!confFile.exists()) {
				createConfiguration(path, sourcesRespTime, sourcesUtil, starttime, endtime);
				// }
				runEstimation(path);
			}

		}
	}

	private static void createConfiguration(Path path, List<Path> sourcesRespTime, List<Path> sourcesUtil,
			long starttime, long endtime) {

		try {
			Path source = null;
			switch (sourcesRespTime.size()) {
			case 1:
				source = fs.getPath(basePath + "/config1.librede");
				break;
			case 2:
				source = fs.getPath(basePath + "/config2.librede");
				break;
			case 3:
				source = fs.getPath(basePath + "/config3.librede");
				break;
			case 4:
				source = fs.getPath(basePath + "/config4.librede");
				break;
			case 5:
				source = fs.getPath(basePath + "/config5.librede");
				break;
			case 6:
				source = fs.getPath(basePath + "/config6.librede");
				break;
			}
			// create config in target directory if not exist
			Path target = fs.getPath(path + "/config.librede");

			try {
				Files.copy(source, target);
			} catch (FileAlreadyExistsException e) {
				e.printStackTrace();
			}
			// load new config file
			File config = new File(target.toString());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document configDoc = builder.parse(config);

			configDoc.getDocumentElement().normalize();

			// fetch all observation nodes
			NodeList observations = configDoc.getElementsByTagName("observations");

			// iterate all observation nodes except the last one (utilization
			// observation)
			for (int i = 0; i < observations.getLength() - 2; i++) {
				// get attributes of the observation
				NamedNodeMap attrs = observations.item(i).getAttributes();
				// get file attribute
				Node fileAttr = attrs.getNamedItem("file");
				// set file attribute content
				fileAttr.setTextContent(sourcesRespTime.get(i).toString());
			}

			if (sourcesUtil.size() > 0) {
				// fill in file path for last observation (utilization)
				NamedNodeMap attrs = observations.item(observations.getLength() - 1).getAttributes();
				Node fileAttrUtil = attrs.getNamedItem("file");
				fileAttrUtil.setTextContent(sourcesUtil.get(0).toString());
			}

			// set startTimestamp
			Node startTimestamp = configDoc.getElementsByTagName("startTimestamp").item(0);
			NamedNodeMap attrs = startTimestamp.getAttributes();
			Node startTimestampAttr = attrs.getNamedItem("value");
			startTimestampAttr.setTextContent(String.valueOf(starttime));

			// set endTimestamp
			Node endTimestamp = configDoc.getElementsByTagName("endTimestamp").item(0);
			NamedNodeMap attrs2 = endTimestamp.getAttributes();
			Node endTimestampAttr = attrs2.getNamedItem("value");
			endTimestampAttr.setTextContent(String.valueOf(endtime));

			// set outputFileDirectory
			Node outputPara = configDoc.getElementsByTagName("parameters").item(0);
			// TODO the next line is not working!
			NamedNodeMap outputAttrs = outputPara.getAttributes();
			Node outputFileAttr = outputAttrs.getNamedItem("value");
			outputFileAttr.setTextContent(path.toString());

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source1 = new DOMSource(configDoc);
			StreamResult result = new StreamResult(config);
			transformer.transform(source1, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private static void runEstimation(Path path) {
		Librede.init();
		LibredeConfiguration config = Librede.loadConfiguration(path.resolve("config.librede"));

		LibredeVariables var = new LibredeVariables(config);
		Librede.initRepo(var);
		Librede.executeContinuous(var, Collections.<String, IDataSource> emptyMap());
		Set<Class<? extends IEstimationApproach>> approaches = var.getResults().getApproaches();
		for (Class<? extends IEstimationApproach> curApproach : approaches) {
			System.out.println(var.getResults().getApproachResults(curApproach).getApproach().getName() + ", RespError: "
					+ var.getResults().getApproachResults(curApproach).getResponseTimeError() + ", UtilError: "
					+ var.getResults().getApproachResults(curApproach).getUtilizationError());

		}

	}

}
