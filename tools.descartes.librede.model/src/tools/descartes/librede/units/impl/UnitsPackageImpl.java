/**
 */
package tools.descartes.librede.units.impl;

import java.lang.Comparable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.impl.ConfigurationPackageImpl;
import tools.descartes.librede.metrics.MetricsPackage;
import tools.descartes.librede.metrics.impl.MetricsPackageImpl;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.units.UnitsPackage;
import tools.descartes.librede.units.UnitsRepository;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UnitsPackageImpl extends EPackageImpl implements UnitsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dimensionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass requestRateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass requestCountEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ratioEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitsRepositoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass quantityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass comparableEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see tools.descartes.librede.units.UnitsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UnitsPackageImpl() {
		super(eNS_URI, UnitsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link UnitsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UnitsPackage init() {
		if (isInited) return (UnitsPackage)EPackage.Registry.INSTANCE.getEPackage(UnitsPackage.eNS_URI);

		// Obtain or create and register package
		UnitsPackageImpl theUnitsPackage = (UnitsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof UnitsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new UnitsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ConfigurationPackageImpl theConfigurationPackage = (ConfigurationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) instanceof ConfigurationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) : ConfigurationPackage.eINSTANCE);
		MetricsPackageImpl theMetricsPackage = (MetricsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MetricsPackage.eNS_URI) instanceof MetricsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MetricsPackage.eNS_URI) : MetricsPackage.eINSTANCE);

		// Create package meta-data objects
		theUnitsPackage.createPackageContents();
		theConfigurationPackage.createPackageContents();
		theMetricsPackage.createPackageContents();

		// Initialize created meta-data
		theUnitsPackage.initializePackageContents();
		theConfigurationPackage.initializePackageContents();
		theMetricsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUnitsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(UnitsPackage.eNS_URI, theUnitsPackage);
		return theUnitsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDimension() {
		return dimensionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDimension_BaseUnit() {
		return (EReference)dimensionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDimension_Units() {
		return (EReference)dimensionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnit() {
		return unitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnit_Id() {
		return (EAttribute)unitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnit_Name() {
		return (EAttribute)unitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnit_Symbol() {
		return (EAttribute)unitEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnit_BaseFactor() {
		return (EAttribute)unitEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnit_Dimension() {
		return (EReference)unitEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getUnit__ConvertTo__double_Unit() {
		return unitEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getUnit__ConvertFrom__double_Unit() {
		return unitEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRequestRate() {
		return requestRateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTime() {
		return timeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRequestCount() {
		return requestCountEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRatio() {
		return ratioEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnitsRepository() {
		return unitsRepositoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitsRepository_Dimensions() {
		return (EReference)unitsRepositoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQuantity() {
		return quantityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuantity_Value() {
		return (EAttribute)quantityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQuantity_Unit() {
		return (EReference)quantityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQuantity__ConvertTo__Unit() {
		return quantityEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQuantity__GetValue__Unit() {
		return quantityEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQuantity__Plus__Quantity() {
		return quantityEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQuantity__Minus__Quantity() {
		return quantityEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQuantity__Times__double() {
		return quantityEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComparable() {
		return comparableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitsFactory getUnitsFactory() {
		return (UnitsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		dimensionEClass = createEClass(DIMENSION);
		createEReference(dimensionEClass, DIMENSION__BASE_UNIT);
		createEReference(dimensionEClass, DIMENSION__UNITS);

		unitEClass = createEClass(UNIT);
		createEAttribute(unitEClass, UNIT__ID);
		createEAttribute(unitEClass, UNIT__NAME);
		createEAttribute(unitEClass, UNIT__SYMBOL);
		createEAttribute(unitEClass, UNIT__BASE_FACTOR);
		createEReference(unitEClass, UNIT__DIMENSION);
		createEOperation(unitEClass, UNIT___CONVERT_TO__DOUBLE_UNIT);
		createEOperation(unitEClass, UNIT___CONVERT_FROM__DOUBLE_UNIT);

		requestRateEClass = createEClass(REQUEST_RATE);

		timeEClass = createEClass(TIME);

		requestCountEClass = createEClass(REQUEST_COUNT);

		ratioEClass = createEClass(RATIO);

		unitsRepositoryEClass = createEClass(UNITS_REPOSITORY);
		createEReference(unitsRepositoryEClass, UNITS_REPOSITORY__DIMENSIONS);

		quantityEClass = createEClass(QUANTITY);
		createEAttribute(quantityEClass, QUANTITY__VALUE);
		createEReference(quantityEClass, QUANTITY__UNIT);
		createEOperation(quantityEClass, QUANTITY___CONVERT_TO__UNIT);
		createEOperation(quantityEClass, QUANTITY___GET_VALUE__UNIT);
		createEOperation(quantityEClass, QUANTITY___PLUS__QUANTITY);
		createEOperation(quantityEClass, QUANTITY___MINUS__QUANTITY);
		createEOperation(quantityEClass, QUANTITY___TIMES__DOUBLE);

		comparableEClass = createEClass(COMPARABLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters
		addETypeParameter(comparableEClass, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		requestRateEClass.getESuperTypes().add(this.getDimension());
		timeEClass.getESuperTypes().add(this.getDimension());
		requestCountEClass.getESuperTypes().add(this.getDimension());
		ratioEClass.getESuperTypes().add(this.getDimension());
		EGenericType g1 = createEGenericType(this.getComparable());
		EGenericType g2 = createEGenericType(this.getQuantity());
		g1.getETypeArguments().add(g2);
		quantityEClass.getEGenericSuperTypes().add(g1);

		// Initialize classes, features, and operations; add parameters
		initEClass(dimensionEClass, Dimension.class, "Dimension", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDimension_BaseUnit(), this.getUnit(), null, "baseUnit", null, 1, 1, Dimension.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDimension_Units(), this.getUnit(), this.getUnit_Dimension(), "units", null, 1, -1, Dimension.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitEClass, Unit.class, "Unit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnit_Id(), ecorePackage.getEString(), "id", null, 1, 1, Unit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnit_Name(), ecorePackage.getEString(), "name", null, 1, 1, Unit.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnit_Symbol(), ecorePackage.getEString(), "symbol", null, 1, 1, Unit.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnit_BaseFactor(), ecorePackage.getEDouble(), "baseFactor", null, 1, 1, Unit.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnit_Dimension(), this.getDimension(), this.getDimension_Units(), "dimension", null, 1, 1, Unit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getUnit__ConvertTo__double_Unit(), ecorePackage.getEDouble(), "convertTo", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDouble(), "value", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getUnit(), "targetUnit", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getUnit__ConvertFrom__double_Unit(), ecorePackage.getEDouble(), "convertFrom", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDouble(), "value", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getUnit(), "sourceUnit", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(requestRateEClass, RequestRate.class, "RequestRate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(timeEClass, Time.class, "Time", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(requestCountEClass, RequestCount.class, "RequestCount", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ratioEClass, Ratio.class, "Ratio", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(unitsRepositoryEClass, UnitsRepository.class, "UnitsRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUnitsRepository_Dimensions(), this.getDimension(), null, "dimensions", null, 0, -1, UnitsRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(quantityEClass, Quantity.class, "Quantity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQuantity_Value(), ecorePackage.getEDouble(), "value", null, 1, 1, Quantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getQuantity_Unit(), this.getUnit(), null, "unit", null, 1, 1, Quantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getQuantity__ConvertTo__Unit(), this.getQuantity(), "convertTo", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getUnit(), "targetUnit", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getQuantity__GetValue__Unit(), ecorePackage.getEDouble(), "getValue", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getUnit(), "unit", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getQuantity__Plus__Quantity(), this.getQuantity(), "plus", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getQuantity(), "quantity", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getQuantity__Minus__Quantity(), this.getQuantity(), "minus", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getQuantity(), "quantity", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getQuantity__Times__double(), this.getQuantity(), "times", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDouble(), "factor", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(comparableEClass, Comparable.class, "Comparable", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //UnitsPackageImpl
