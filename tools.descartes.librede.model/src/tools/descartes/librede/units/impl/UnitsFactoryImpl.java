/**
 */
package tools.descartes.librede.units.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.units.UnitsPackage;
import tools.descartes.librede.units.UnitsRepository;
import tools.descartes.librede.units.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UnitsFactoryImpl extends EFactoryImpl implements UnitsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static UnitsFactory init() {
		try {
			UnitsFactory theUnitsFactory = (UnitsFactory)EPackage.Registry.INSTANCE.getEFactory(UnitsPackage.eNS_URI);
			if (theUnitsFactory != null) {
				return theUnitsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UnitsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case UnitsPackage.UNIT: return createUnit();
			case UnitsPackage.REQUEST_RATE: return createRequestRate();
			case UnitsPackage.TIME: return createTime();
			case UnitsPackage.REQUEST_COUNT: return createRequestCount();
			case UnitsPackage.RATIO: return createRatio();
			case UnitsPackage.UNITS_REPOSITORY: return createUnitsRepository();
			case UnitsPackage.QUANTITY: return createQuantity();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Unit createUnit() {
		UnitImpl unit = new UnitImpl();
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Unit createUnit(Dimension dimension, String id, String name, String symbol, double baseFactor) {
		UnitImpl unit = new UnitImpl();
		unit.id = id;
		unit.name = name;
		unit.symbol = symbol;
		unit.baseFactor = baseFactor;
		
		dimension.getUnits().add(unit);
		return unit;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Unit createBaseUnit(Dimension dimension, String id, String name, String symbol) {
		Unit unit = createUnit(dimension, id, name, symbol, 1.0);
		dimension.setBaseUnit(unit);
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequestRate createRequestRate() {
		RequestRateImpl requestRate = new RequestRateImpl();
		return requestRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Time createTime() {
		TimeImpl time = new TimeImpl();
		return time;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequestCount createRequestCount() {
		RequestCountImpl requestCount = new RequestCountImpl();
		return requestCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ratio createRatio() {
		RatioImpl ratio = new RatioImpl();
		return ratio;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitsRepository createUnitsRepository() {
		UnitsRepositoryImpl unitsRepository = new UnitsRepositoryImpl();
		return unitsRepository;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Quantity createQuantity() {
		QuantityImpl quantity = new QuantityImpl();
		return quantity;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Quantity createQuantity(double value, Unit unit) {
		QuantityImpl quantity = new QuantityImpl();
		quantity.unit = unit;
		quantity.value = value;
		return quantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitsPackage getUnitsPackage() {
		return (UnitsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UnitsPackage getPackage() {
		return UnitsPackage.eINSTANCE;
	}

} //UnitsFactoryImpl
