package ocl;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.ocl.*;
import org.eclipse.ocl.ecore.*;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.ecore.impl.VariableImpl;
import org.eclipse.ocl.ecore.internal.UMLReflectionImpl;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import java.util.List;

/**
 * Based on https://github.com/rte-france/cgmes-ocl-validator
 */
public class OclVerificationTest {
    public static void main(String[] args) throws ParserException {
        // Create package
        EPackage pack = EcoreFactory.eINSTANCE.createEPackage();
        pack.setName("MyGreatPackage");

        // Create classes
        EClass personClass = EcoreFactory.eINSTANCE.createEClass();
        personClass.setName("Person");
        pack.getEClassifiers().add(personClass);

        // Create attributes
        EAttribute ageAttribute = EcoreFactory.eINSTANCE.createEAttribute();
        ageAttribute.setName("age");
        ageAttribute.setEType(EcorePackage.eINSTANCE.getEInt());
        personClass.getEAttributes().add(ageAttribute);

        // Add reference
        EReference parentReference = EcoreFactory.eINSTANCE.createEReference();
        parentReference.setName("parent");
        parentReference.setEType(personClass);
        personClass.getEReferences().add(parentReference);

        // Add the created classes to the UML reflection, so it can be used
        UMLReflectionImpl.INSTANCE.asOCLType(personClass);

        // Setup OCL
        OCLstdlib.install();
        OCL ocl = OCL.newInstance();
        Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> environment = ocl.getEnvironment();
        environment.getContextPackage();
        // hack the variable impl into the environment with one type of the package to implicitly set the contextPackage as required for ocl
        // This is the most hacky shit ever, but it works. Who the fuck hides important setters like setContextPackage which is required for OCL.
        // We will never, ever talk about this bullshit. It took > 2 days to get this run...
        org.eclipse.ocl.expressions.Variable<EClassifier, EParameter> variable = new VariableImpl() {
            @Override
            public EClassifier getType() {
                return personClass;
            }
        };
        environment.setSelfVariable(variable);

        // Create object instance, which should be evaluated
        EFactory factory = EcoreFactory.eINSTANCE.createEFactory();
        factory.setEPackage(pack);
        EObject person = factory.create(personClass);
        person.eSet(ageAttribute, 5);
        EObject person2 = factory.create(personClass);
        person2.eSet(ageAttribute, 35);
        person.eSet(parentReference, person2);


        // Try OCL Constraint
        OCLInput oclInput = new OCLInput("context Person inv: self.parent.age >= 6");
        List<Constraint> parse = ocl.parse(oclInput);
        for(Constraint constraint : parse){
            boolean check = ocl.check(person, constraint);
            System.out.println(check);
        }
    }
}
