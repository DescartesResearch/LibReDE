package tools.descartes.librede.datasource;

import java.util.LinkedList;
import java.util.List;

import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

public abstract class AbstractDataSource implements IDataSource {
	
	private List<IDataSourceListener> listeners = new LinkedList<IDataSourceListener>();
	
	@Override
	public void addListener(IDataSourceListener listener) {
		listeners.add(listener);		
	}
	
	@Override
	public void removeListener(IDataSourceListener listener) {
		listeners.remove(listener);		
	}

	protected void notifyListeners(TraceEvent e) {
		for (IDataSourceListener listener : listeners) {
			listener.dataAvailable(this, e);
		}
	}
}
