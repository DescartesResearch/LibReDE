/**
 */
package tools.descartes.librede.units;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see tools.descartes.librede.units.UnitsFactory
 * @model kind="package"
 * @generated
 */
public interface UnitsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "units";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.descartes-research.net/librede/units/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "librede-units";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UnitsPackage eINSTANCE = tools.descartes.librede.units.impl.UnitsPackageImpl.init();

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.DimensionImpl <em>Dimension</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.DimensionImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getDimension()
	 * @generated
	 */
	int DIMENSION = 0;

	/**
	 * The feature id for the '<em><b>Base Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION__BASE_UNIT = 0;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION__UNITS = 1;

	/**
	 * The number of structural features of the '<em>Dimension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Dimension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.UnitImpl <em>Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.UnitImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getUnit()
	 * @generated
	 */
	int UNIT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT__SYMBOL = 1;

	/**
	 * The feature id for the '<em><b>Base Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT__BASE_FACTOR = 2;

	/**
	 * The feature id for the '<em><b>Dimension</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT__DIMENSION = 3;

	/**
	 * The number of structural features of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_FEATURE_COUNT = 4;

	/**
	 * The operation id for the '<em>Convert To</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT___CONVERT_TO__DOUBLE_UNIT = 0;

	/**
	 * The operation id for the '<em>Convert From</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT___CONVERT_FROM__DOUBLE_UNIT = 1;

	/**
	 * The number of operations of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_OPERATION_COUNT = 2;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.RequestRateImpl <em>Request Rate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.RequestRateImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getRequestRate()
	 * @generated
	 */
	int REQUEST_RATE = 2;

	/**
	 * The feature id for the '<em><b>Base Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_RATE__BASE_UNIT = DIMENSION__BASE_UNIT;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_RATE__UNITS = DIMENSION__UNITS;

	/**
	 * The number of structural features of the '<em>Request Rate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_RATE_FEATURE_COUNT = DIMENSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Request Rate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_RATE_OPERATION_COUNT = DIMENSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.TimeImpl <em>Time</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.TimeImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getTime()
	 * @generated
	 */
	int TIME = 3;

	/**
	 * The feature id for the '<em><b>Base Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME__BASE_UNIT = DIMENSION__BASE_UNIT;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME__UNITS = DIMENSION__UNITS;

	/**
	 * The number of structural features of the '<em>Time</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_FEATURE_COUNT = DIMENSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Time</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_OPERATION_COUNT = DIMENSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.RequestCountImpl <em>Request Count</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.RequestCountImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getRequestCount()
	 * @generated
	 */
	int REQUEST_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Base Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_COUNT__BASE_UNIT = DIMENSION__BASE_UNIT;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_COUNT__UNITS = DIMENSION__UNITS;

	/**
	 * The number of structural features of the '<em>Request Count</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_COUNT_FEATURE_COUNT = DIMENSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Request Count</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_COUNT_OPERATION_COUNT = DIMENSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.ProportionImpl <em>Proportion</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.ProportionImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getProportion()
	 * @generated
	 */
	int PROPORTION = 5;

	/**
	 * The feature id for the '<em><b>Base Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPORTION__BASE_UNIT = DIMENSION__BASE_UNIT;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPORTION__UNITS = DIMENSION__UNITS;

	/**
	 * The number of structural features of the '<em>Proportion</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPORTION_FEATURE_COUNT = DIMENSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Proportion</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPORTION_OPERATION_COUNT = DIMENSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.units.impl.UnitsRepositoryImpl <em>Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.units.impl.UnitsRepositoryImpl
	 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getUnitsRepository()
	 * @generated
	 */
	int UNITS_REPOSITORY = 6;

	/**
	 * The feature id for the '<em><b>Dimensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNITS_REPOSITORY__DIMENSIONS = 0;

	/**
	 * The number of structural features of the '<em>Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNITS_REPOSITORY_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNITS_REPOSITORY_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.Dimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dimension</em>'.
	 * @see tools.descartes.librede.units.Dimension
	 * @generated
	 */
	EClass getDimension();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.units.Dimension#getBaseUnit <em>Base Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Unit</em>'.
	 * @see tools.descartes.librede.units.Dimension#getBaseUnit()
	 * @see #getDimension()
	 * @generated
	 */
	EReference getDimension_BaseUnit();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.units.Dimension#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Units</em>'.
	 * @see tools.descartes.librede.units.Dimension#getUnits()
	 * @see #getDimension()
	 * @generated
	 */
	EReference getDimension_Units();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.Unit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit</em>'.
	 * @see tools.descartes.librede.units.Unit
	 * @generated
	 */
	EClass getUnit();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.units.Unit#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see tools.descartes.librede.units.Unit#getName()
	 * @see #getUnit()
	 * @generated
	 */
	EAttribute getUnit_Name();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.units.Unit#getSymbol <em>Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Symbol</em>'.
	 * @see tools.descartes.librede.units.Unit#getSymbol()
	 * @see #getUnit()
	 * @generated
	 */
	EAttribute getUnit_Symbol();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.units.Unit#getBaseFactor <em>Base Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Factor</em>'.
	 * @see tools.descartes.librede.units.Unit#getBaseFactor()
	 * @see #getUnit()
	 * @generated
	 */
	EAttribute getUnit_BaseFactor();

	/**
	 * Returns the meta object for the container reference '{@link tools.descartes.librede.units.Unit#getDimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Dimension</em>'.
	 * @see tools.descartes.librede.units.Unit#getDimension()
	 * @see #getUnit()
	 * @generated
	 */
	EReference getUnit_Dimension();

	/**
	 * Returns the meta object for the '{@link tools.descartes.librede.units.Unit#convertTo(double, tools.descartes.librede.units.Unit) <em>Convert To</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Convert To</em>' operation.
	 * @see tools.descartes.librede.units.Unit#convertTo(double, tools.descartes.librede.units.Unit)
	 * @generated
	 */
	EOperation getUnit__ConvertTo__double_Unit();

	/**
	 * Returns the meta object for the '{@link tools.descartes.librede.units.Unit#convertFrom(double, tools.descartes.librede.units.Unit) <em>Convert From</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Convert From</em>' operation.
	 * @see tools.descartes.librede.units.Unit#convertFrom(double, tools.descartes.librede.units.Unit)
	 * @generated
	 */
	EOperation getUnit__ConvertFrom__double_Unit();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.RequestRate <em>Request Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Rate</em>'.
	 * @see tools.descartes.librede.units.RequestRate
	 * @generated
	 */
	EClass getRequestRate();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.Time <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time</em>'.
	 * @see tools.descartes.librede.units.Time
	 * @generated
	 */
	EClass getTime();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.RequestCount <em>Request Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Count</em>'.
	 * @see tools.descartes.librede.units.RequestCount
	 * @generated
	 */
	EClass getRequestCount();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.Proportion <em>Proportion</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Proportion</em>'.
	 * @see tools.descartes.librede.units.Proportion
	 * @generated
	 */
	EClass getProportion();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.units.UnitsRepository <em>Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Repository</em>'.
	 * @see tools.descartes.librede.units.UnitsRepository
	 * @generated
	 */
	EClass getUnitsRepository();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.units.UnitsRepository#getDimensions <em>Dimensions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dimensions</em>'.
	 * @see tools.descartes.librede.units.UnitsRepository#getDimensions()
	 * @see #getUnitsRepository()
	 * @generated
	 */
	EReference getUnitsRepository_Dimensions();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UnitsFactory getUnitsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.DimensionImpl <em>Dimension</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.DimensionImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getDimension()
		 * @generated
		 */
		EClass DIMENSION = eINSTANCE.getDimension();

		/**
		 * The meta object literal for the '<em><b>Base Unit</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIMENSION__BASE_UNIT = eINSTANCE.getDimension_BaseUnit();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIMENSION__UNITS = eINSTANCE.getDimension_Units();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.UnitImpl <em>Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.UnitImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getUnit()
		 * @generated
		 */
		EClass UNIT = eINSTANCE.getUnit();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT__NAME = eINSTANCE.getUnit_Name();

		/**
		 * The meta object literal for the '<em><b>Symbol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT__SYMBOL = eINSTANCE.getUnit_Symbol();

		/**
		 * The meta object literal for the '<em><b>Base Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT__BASE_FACTOR = eINSTANCE.getUnit_BaseFactor();

		/**
		 * The meta object literal for the '<em><b>Dimension</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT__DIMENSION = eINSTANCE.getUnit_Dimension();

		/**
		 * The meta object literal for the '<em><b>Convert To</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation UNIT___CONVERT_TO__DOUBLE_UNIT = eINSTANCE.getUnit__ConvertTo__double_Unit();

		/**
		 * The meta object literal for the '<em><b>Convert From</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation UNIT___CONVERT_FROM__DOUBLE_UNIT = eINSTANCE.getUnit__ConvertFrom__double_Unit();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.RequestRateImpl <em>Request Rate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.RequestRateImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getRequestRate()
		 * @generated
		 */
		EClass REQUEST_RATE = eINSTANCE.getRequestRate();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.TimeImpl <em>Time</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.TimeImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getTime()
		 * @generated
		 */
		EClass TIME = eINSTANCE.getTime();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.RequestCountImpl <em>Request Count</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.RequestCountImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getRequestCount()
		 * @generated
		 */
		EClass REQUEST_COUNT = eINSTANCE.getRequestCount();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.ProportionImpl <em>Proportion</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.ProportionImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getProportion()
		 * @generated
		 */
		EClass PROPORTION = eINSTANCE.getProportion();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.units.impl.UnitsRepositoryImpl <em>Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.units.impl.UnitsRepositoryImpl
		 * @see tools.descartes.librede.units.impl.UnitsPackageImpl#getUnitsRepository()
		 * @generated
		 */
		EClass UNITS_REPOSITORY = eINSTANCE.getUnitsRepository();

		/**
		 * The meta object literal for the '<em><b>Dimensions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNITS_REPOSITORY__DIMENSIONS = eINSTANCE.getUnitsRepository_Dimensions();

	}

} //UnitsPackage
