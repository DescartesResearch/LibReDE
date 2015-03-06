/**
 * A data source provides functionality to read measurement data from a source
 * of a specific type and format (e.g., file, database, or interface provided
 * by a monitoring tool). A data source is used to continuously load the newly
 * available measurement data points into an instance of a {@link tools.descartes.librede.repository.IMonitoringRepository}.
 * This package provides the interface {@link tools.descartes.librede.datasource.IDataSource}
 * that needs to be implemented by all data sources.
 */
package tools.descartes.librede.datasource;


