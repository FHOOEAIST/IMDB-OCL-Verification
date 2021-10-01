package ocl;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreEList;
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

        // Create Actor class
        EClass actorClass = EcoreFactory.eINSTANCE.createEClass();
        actorClass.setName("Actor");
        pack.getEClassifiers().add(actorClass);

        // Create person attributes
        EAttribute nconst = EcoreFactory.eINSTANCE.createEAttribute();
        nconst.setName("nconst");
        nconst.setEType(EcorePackage.eINSTANCE.getEString());
        actorClass.getEStructuralFeatures().add(nconst);

        EAttribute primaryName = EcoreFactory.eINSTANCE.createEAttribute();
        primaryName.setName("primaryName");
        primaryName.setEType(EcorePackage.eINSTANCE.getEString());
        actorClass.getEStructuralFeatures().add(primaryName);

        EAttribute birthYear = EcoreFactory.eINSTANCE.createEAttribute();
        birthYear.setName("birthYear");
        birthYear.setEType(EcorePackage.eINSTANCE.getEInt());
        actorClass.getEStructuralFeatures().add(birthYear);

        EAttribute deathYear = EcoreFactory.eINSTANCE.createEAttribute();
        deathYear.setName("deathYear");
        deathYear.setEType(EcorePackage.eINSTANCE.getEInt());
        actorClass.getEStructuralFeatures().add(deathYear);

        EAttribute primaryProfession = EcoreFactory.eINSTANCE.createEAttribute();
        primaryProfession.setName("primaryProfession");
        primaryProfession.setEType(EcorePackage.eINSTANCE.getEEList());
        actorClass.getEStructuralFeatures().add(primaryProfession);

        EAttribute knownForTitles = EcoreFactory.eINSTANCE.createEAttribute();
        knownForTitles.setName("knownForTitles");
        knownForTitles.setEType(EcorePackage.eINSTANCE.getEEList());
        actorClass.getEStructuralFeatures().add(knownForTitles);

        // Create movie class
        EClass movieClass = EcoreFactory.eINSTANCE.createEClass();
        movieClass.setName("Movie");
        pack.getEClassifiers().add(movieClass);

        // Create movie attributes
        EAttribute tconst = EcoreFactory.eINSTANCE.createEAttribute();
        tconst.setName("tconst");
        tconst.setEType(EcorePackage.eINSTANCE.getEString());
        movieClass.getEStructuralFeatures().add(tconst);

        EAttribute titleType = EcoreFactory.eINSTANCE.createEAttribute();
        titleType.setName("titleType");
        titleType.setEType(EcorePackage.eINSTANCE.getEString());
        movieClass.getEStructuralFeatures().add(titleType);

        EAttribute primaryTitle = EcoreFactory.eINSTANCE.createEAttribute();
        primaryTitle.setName("primaryTitle");
        primaryTitle.setEType(EcorePackage.eINSTANCE.getEString());
        movieClass.getEStructuralFeatures().add(primaryTitle);

        EAttribute originalTitle = EcoreFactory.eINSTANCE.createEAttribute();
        originalTitle.setName("originalTitle");
        originalTitle.setEType(EcorePackage.eINSTANCE.getEString());
        movieClass.getEStructuralFeatures().add(originalTitle);

        EAttribute isAdult = EcoreFactory.eINSTANCE.createEAttribute();
        isAdult.setName("isAdult");
        isAdult.setEType(EcorePackage.eINSTANCE.getEBoolean());
        movieClass.getEStructuralFeatures().add(isAdult);

        EAttribute startYear = EcoreFactory.eINSTANCE.createEAttribute();
        startYear.setName("startYear");
        startYear.setEType(EcorePackage.eINSTANCE.getEInt());
        movieClass.getEStructuralFeatures().add(startYear);

        EAttribute endYear = EcoreFactory.eINSTANCE.createEAttribute();
        endYear.setName("endYear");
        endYear.setEType(EcorePackage.eINSTANCE.getEInt());
        movieClass.getEStructuralFeatures().add(endYear);

        EAttribute runtimeMinutes = EcoreFactory.eINSTANCE.createEAttribute();
        runtimeMinutes.setName("runtimeMinutes");
        runtimeMinutes.setEType(EcorePackage.eINSTANCE.getEInt());
        movieClass.getEStructuralFeatures().add(runtimeMinutes);

        EAttribute genres = EcoreFactory.eINSTANCE.createEAttribute();
        genres.setName("genres");
        genres.setEType(EcorePackage.eINSTANCE.getEEList());
        movieClass.getEStructuralFeatures().add(genres);

        // Create ActorInMovieClass class
        EClass actorInMovieClass = EcoreFactory.eINSTANCE.createEClass();
        actorInMovieClass.setName("ActorInMovie");
        pack.getEClassifiers().add(actorInMovieClass);

        // Create ActorInMovie attributes
        EAttribute ordering = EcoreFactory.eINSTANCE.createEAttribute();
        ordering.setName("ordering");
        ordering.setEType(EcorePackage.eINSTANCE.getEInt());
        actorInMovieClass.getEStructuralFeatures().add(ordering);

        EAttribute category = EcoreFactory.eINSTANCE.createEAttribute();
        category.setName("category");
        category.setEType(EcorePackage.eINSTANCE.getEString());
        actorInMovieClass.getEStructuralFeatures().add(category);

        EAttribute job = EcoreFactory.eINSTANCE.createEAttribute();
        job.setName("job");
        job.setEType(EcorePackage.eINSTANCE.getEEList());
        actorInMovieClass.getEStructuralFeatures().add(job);

        EAttribute characters = EcoreFactory.eINSTANCE.createEAttribute();
        characters.setName("category");
        characters.setEType(EcorePackage.eINSTANCE.getEEList());
        actorInMovieClass.getEStructuralFeatures().add(characters);

        // Add references to ActorInMovie
        EReference actorReference = EcoreFactory.eINSTANCE.createEReference();
        actorReference.setName("nconst");
        actorReference.setEType(actorClass);
        actorInMovieClass.getEStructuralFeatures().add(actorReference);

        EReference movieReference = EcoreFactory.eINSTANCE.createEReference();
        movieReference.setName("tconst");
        movieReference.setEType(movieClass);
        actorInMovieClass.getEStructuralFeatures().add(movieReference);

        // Create Root class
        EClass rootClass = EcoreFactory.eINSTANCE.createEClass();
        rootClass.setName("Root");
        pack.getEClassifiers().add(rootClass);

        EReference actors = EcoreFactory.eINSTANCE.createEReference();
        actors.setEType(actorClass);
        actors.setLowerBound(0);
        actors.setUpperBound(-1);
        actors.setName("actors");
        rootClass.getEStructuralFeatures().add(actors);

        EReference movies = EcoreFactory.eINSTANCE.createEReference();
        movies.setEType(actorClass);
        movies.setLowerBound(0);
        movies.setUpperBound(-1);
        movies.setName("movies");
        rootClass.getEStructuralFeatures().add(movies);

        EReference actorsInMovies = EcoreFactory.eINSTANCE.createEReference();
        actorsInMovies.setEType(actorInMovieClass);
        actorsInMovies.setLowerBound(0);
        actorsInMovies.setUpperBound(-1);
        actorsInMovies.setName("actorsInMovies");
        rootClass.getEStructuralFeatures().add(actorsInMovies);



        // Add the created classes to the UML reflection, so it can be used
        UMLReflectionImpl.INSTANCE.asOCLType(actorClass);
        UMLReflectionImpl.INSTANCE.asOCLType(movieClass);
        UMLReflectionImpl.INSTANCE.asOCLType(actorInMovieClass);
        UMLReflectionImpl.INSTANCE.asOCLType(rootClass);

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
                return actorClass;
            }
        };
        environment.setSelfVariable(variable);

        // Create object instance, which should be evaluated
        EFactory factory = EcoreFactory.eINSTANCE.createEFactory();
        factory.setEPackage(pack);
        EObject actor1 = factory.create(actorClass);
        actor1.eSet(nconst, "nm0000001");
        actor1.eSet(primaryName, "Fred Astaire");
        actor1.eSet(birthYear, 1899);
        actor1.eSet(deathYear, 1987);
        EList<String> actorPrimaryProfession = new BasicEList<>();
        actorPrimaryProfession.add("soundtrack");
        actorPrimaryProfession.add("actor");
        actorPrimaryProfession.add("miscellaneous");
        actor1.eSet(primaryProfession, actorPrimaryProfession);
        EList<String> actorKnownForTitles = new BasicEList<>();
        actorKnownForTitles.add("tt0050419");
        actorKnownForTitles.add("tt0031983");
        actorKnownForTitles.add("tt0053137");
        actorKnownForTitles.add("tt0072308");
        actor1.eSet(knownForTitles, actorKnownForTitles);

        EObject movie1 = factory.create(movieClass);
        movie1.eSet(tconst, "tt0000001");
        movie1.eSet(titleType, "short");
        movie1.eSet(primaryTitle, "Carmencita");
        movie1.eSet(originalTitle, "Carmencita");
        movie1.eSet(isAdult, false);
        movie1.eSet(startYear, 1894);
//        movie1.eSet(endYear, false);
        movie1.eSet(runtimeMinutes, 1);
        EList<String> movieGenres = new BasicEList<>();
        movieGenres.add("Documentary");
        movieGenres.add("Short");
        movie1.eSet(genres, movieGenres);

        EObject actorInMovie1 = factory.create(actorInMovieClass);
        actorInMovie1.eSet(movieReference, movie1);
        actorInMovie1.eSet(ordering, 1);
        actorInMovie1.eSet(actorReference, actor1);
        actorInMovie1.eSet(category, "self");
        EList<String> jobs = new BasicEList<>();
        jobs.add("Self");
        actorInMovie1.eSet(job, jobs);


        // create Root
        EObject root = factory.create(rootClass);
        EList o = (EList)root.eGet(actors);
        o.add(actor1);
//        EList<Object> actorsInRoot = new BasicEList<>();
//        actorsInRoot.add(actor1);
//        root.eSet(actors, actorsInRoot);
        EList<Object> moviesInRoot = new BasicEList<>();
        moviesInRoot.add(movie1);
        root.eSet(movies, moviesInRoot);
        EList<Object> actorsInMoviesInRoot = new BasicEList<>();
        actorsInMoviesInRoot.add(actorInMovie1);
        root.eSet(actorsInMovies, actorsInMoviesInRoot);


        // Try OCL Constraint
        OCLInput oclInput = new OCLInput("context Root inv: self.actors->forAll(a | self.actorsInMovies->any(aIm | a = aIm.nconst) <> null)");
        List<Constraint> parse = ocl.parse(oclInput);
        for(Constraint constraint : parse){
            boolean check = ocl.check(root, constraint);
            System.out.println(check);
        }
    }
}
