package ocl;

import me.tongfei.progressbar.ProgressBar;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.OCLInput;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.ecore.SendSignalAction;
import org.eclipse.ocl.ecore.impl.VariableImpl;
import org.eclipse.ocl.ecore.internal.UMLReflectionImpl;
import org.eclipse.ocl.pivot.model.OCLstdlib;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Based on https://github.com/rte-france/cgmes-ocl-validator
 */
public class OclVerificationTest {

    private static void setInt(EObject target, EAttribute attribute, String value){
        try {
            target.eSet(attribute, Integer.valueOf(value));
        } catch (Exception e){
            // YOLO
        }
    }


    public static void main(String[] args) throws IOException, ParserException {
        long maxWaitingTimePerThread = 1*60*1000;

        for (long numberOfExpectedRelations : List.of(2_500, 40_000, 160_000,625_000,2_500_000,10_000_000, 41_136_299)) {
            try(BufferedWriter out = new BufferedWriter(new FileWriter(new File(numberOfExpectedRelations + ".csv")))){
                final String actorFile = "/name.basics.tsv";
                final long actorFileLength = 10361483;
                final String movieFile = "/title.basics.tsv";
                final long movieFileLength = 7164546;
                final String actorMovieFile = "/title.principals.tsv";
                final long actorMovieFileLength = 41136299;

                // Create package
                EPackage pack = EcoreFactory.eINSTANCE.createEPackage();
                pack.setName("MyGreatPackage");

                // Create Actor class
                EClass personClass = EcoreFactory.eINSTANCE.createEClass();
                personClass.setName("Person");
                pack.getEClassifiers().add(personClass);

                // Create person attributes
                EAttribute nconst = EcoreFactory.eINSTANCE.createEAttribute();
                nconst.setName("id");
                nconst.setEType(EcorePackage.eINSTANCE.getEString());
                personClass.getEStructuralFeatures().add(nconst);

                EAttribute primaryName = EcoreFactory.eINSTANCE.createEAttribute();
                primaryName.setName("name");
                primaryName.setEType(EcorePackage.eINSTANCE.getEString());
                personClass.getEStructuralFeatures().add(primaryName);

                EAttribute birthYear = EcoreFactory.eINSTANCE.createEAttribute();
                birthYear.setName("birthYear");
                birthYear.setEType(EcorePackage.eINSTANCE.getEInt());
                personClass.getEStructuralFeatures().add(birthYear);

                EAttribute deathYear = EcoreFactory.eINSTANCE.createEAttribute();
                deathYear.setName("deathYear");
                deathYear.setEType(EcorePackage.eINSTANCE.getEInt());
                personClass.getEStructuralFeatures().add(deathYear);

                EAttribute primaryProfession = EcoreFactory.eINSTANCE.createEAttribute();
                primaryProfession.setName("primaryProfession");
                primaryProfession.setEType(EcorePackage.eINSTANCE.getEEList());
                personClass.getEStructuralFeatures().add(primaryProfession);

                EAttribute knownForTitles = EcoreFactory.eINSTANCE.createEAttribute();
                knownForTitles.setName("knownForTitles");
                knownForTitles.setEType(EcorePackage.eINSTANCE.getEEList());
                personClass.getEStructuralFeatures().add(knownForTitles);

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
                job.setEType(EcorePackage.eINSTANCE.getEString());
                actorInMovieClass.getEStructuralFeatures().add(job);

                EAttribute characters = EcoreFactory.eINSTANCE.createEAttribute();
                characters.setName("characters");
                characters.setEType(EcorePackage.eINSTANCE.getEEList());
                actorInMovieClass.getEStructuralFeatures().add(characters);

                // Add references to ActorInMovie
                EReference actorReference = EcoreFactory.eINSTANCE.createEReference();
                actorReference.setName("id");
                actorReference.setEType(personClass);
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
                actors.setEType(personClass);
                actors.setLowerBound(0);
                actors.setUpperBound(-1);
                actors.setName("persons");
                rootClass.getEStructuralFeatures().add(actors);

                EReference movies = EcoreFactory.eINSTANCE.createEReference();
                movies.setEType(personClass);
                movies.setLowerBound(0);
                movies.setUpperBound(-1);
                movies.setName("movies");
                rootClass.getEStructuralFeatures().add(movies);

                EReference actorsInMovies = EcoreFactory.eINSTANCE.createEReference();
                actorsInMovies.setEType(actorInMovieClass);
                actorsInMovies.setLowerBound(0);
                actorsInMovies.setUpperBound(-1);
                actorsInMovies.setName("partOf");
                rootClass.getEStructuralFeatures().add(actorsInMovies);

                // Add the created classes to the UML reflection, so it can be used
                UMLReflectionImpl.INSTANCE.asOCLType(personClass);
                UMLReflectionImpl.INSTANCE.asOCLType(movieClass);
                UMLReflectionImpl.INSTANCE.asOCLType(actorInMovieClass);
                UMLReflectionImpl.INSTANCE.asOCLType(rootClass);

                // Setup OCL
                OCLstdlib.install();
                OCL ocl = OCL.newInstance();
                Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> environment = ocl.getEnvironment();
                environment.getContextPackage();
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
                String thisLine;

                Map<String, EObject> moviesMap = new HashMap<>((int) movieFileLength + 1000, 1f);
                try (ProgressBar pb = new ProgressBar("Movies", movieFileLength - 1)) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(OclVerificationTest.class.getResourceAsStream(movieFile)))) {
                        br.readLine(); // skip first line
                        // id, name, date of birth, date of death, primaryProfession (array), knowForTitles (array)
                        while ((thisLine = br.readLine()) != null) {
                            String[] split = thisLine.split("\t");
                            if (moviesMap.containsKey(split[0])) continue;
                            EObject movie1 = factory.create(movieClass);
                            movie1.eSet(tconst, split[0]);
                            movie1.eSet(titleType, split[1]);
                            movie1.eSet(primaryTitle, split[2]);
                            movie1.eSet(originalTitle, split[3]);
                            movie1.eSet(isAdult, !split[4].equals("0"));
                            setInt(movie1, startYear, split[5]);
                            setInt(movie1, endYear, split[6]);
                            setInt(movie1, runtimeMinutes, split[7]);
                            EList<String> movieGenres = new BasicEList<>();
                            movieGenres.addAll(Arrays.asList(split[5].split(",")));
                            movie1.eSet(genres, movieGenres);
                            moviesMap.put(split[0], movie1);
                            pb.step();
                        }
                    }
                }

                Map<String, EObject> actorsMap = new HashMap<>((int)actorFileLength + 1000, 1f);
                try (ProgressBar pb = new ProgressBar("Actors", actorFileLength - 1)) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(OclVerificationTest.class.getResourceAsStream(actorFile)))) {
                        br.readLine(); // skip first line
                        // id, name, date of birth, date of death, primaryProfession (array), knowForTitles (array)
                        while ((thisLine = br.readLine()) != null) {
                            String[] split = thisLine.split("\t");
                            if (actorsMap.containsKey(split[0])) continue;
                            EObject actor1 = factory.create(personClass);
                            actor1.eSet(nconst, split[0]);
                            actor1.eSet(primaryName, split[1]);
                            setInt(actor1, birthYear, split[2]);
                            setInt(actor1, deathYear, split[3]);
                            EList<String> actorPrimaryProfession = new BasicEList<>();
                            actorPrimaryProfession.addAll(Arrays.asList(split[4].replace("\"", "").split(",")));
                            actor1.eSet(primaryProfession, actorPrimaryProfession);
                            EList<String> actorKnownForTitles = new BasicEList<>();
                            actorKnownForTitles.addAll(Arrays.asList(split[5].replace("\"", "").split(",")));
                            actor1.eSet(knownForTitles, actorKnownForTitles);
                            actorsMap.put(split[0], actor1);
                            pb.step();
                        }
                    }
                }



                List<EObject> actorsInMoviesList = new ArrayList<>((int)actorMovieFileLength + 1000);
                Map<String, EObject> usedActors = new HashMap<>();
                Map<String, EObject> usedMovies = new HashMap<>();

                long cnt = 0;
                try (ProgressBar pb = new ProgressBar("ActorsInMovies", Math.min(actorMovieFileLength - 1, numberOfExpectedRelations))) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(OclVerificationTest.class.getResourceAsStream(actorMovieFile)))) {
                        br.readLine(); // skip first line
                        // id, name, date of birth, date of death, primaryProfession (array), knowForTitles (array)
                        while ((thisLine = br.readLine()) != null) {
                            if(cnt ++ > numberOfExpectedRelations){
                                break;
                            }
                            pb.step();
                            String[] split = thisLine.split("\t");
                            EObject actorInMovie1 = factory.create(actorInMovieClass);
                            String movieKey = split[0];
                            EObject movie = moviesMap.get(movieKey);
                            actorInMovie1.eSet(movieReference, movie);
                            if(movie != null){
                                usedMovies.put(movieKey, movie);
                            } else {
//                        System.out.println("movie");
                                continue;
                            }

                            actorInMovie1.eSet(ordering, Integer.valueOf(split[1]));
                            String actorKey = split[2];
                            EObject actor = actorsMap.get(actorKey);
                            actorInMovie1.eSet(actorReference, actor);
                            if(actor != null){
                                usedActors.put(actorKey, actor);
                            } else {
//                        System.out.println("actor");
                                continue;
                            }

                            actorInMovie1.eSet(category, split[3]);
                            String jobValue = split[4].replace("\"", "");
                            if (!jobValue.equals("\\N")) {
                                actorInMovie1.eSet(job, jobValue);
                            }
                            EList<String> characterlist = new BasicEList<>();
                            String charactersValue = split[5].replace("\"", "").replace("[", "").replace("]", "").replace(",", ";");
                            if (!charactersValue.equals("\\N")) {
                                characterlist.addAll(Arrays.asList(charactersValue.split(";")));
                            }
                            actorInMovie1.eSet(characters, characterlist);
                            actorsInMoviesList.add(actorInMovie1);
                        }
                    }
                }

                // create Root
                EObject root = factory.create(rootClass);
                EList<Object> actorsInRoot = new BasicEList<>();
                actorsInRoot.addAll(usedActors.values());
                root.eSet(actors, actorsInRoot);
                EList<Object> moviesInRoot = new BasicEList<>();
                moviesInRoot.addAll(usedMovies.values());
                root.eSet(movies, moviesInRoot);
                EList<Object> actorsInMoviesInRoot = new BasicEList<>();
                actorsInMoviesInRoot.addAll(actorsInMoviesList);
                root.eSet(actorsInMovies, actorsInMoviesInRoot);

                List<String> queries = Arrays.asList("context Root inv: self.persons->forAll(a | a.name <> null)",
                        "context Root inv: self.persons->forAll(a | self.partOf->any(aIm | a = aIm.id) <> null)",
                        "context Root inv: self.persons->select(a | a.primaryProfession->includes('actor'))->forAll(a | self.partOf->any(aIm | a = aIm.id and aIm.category = 'actor') <> null)",
                        "context Root inv: self.persons->isUnique(a | a.id)");

                for (int i = 0; i < 100; i++) {
                    // first constraint
                    for(String query : queries){
                        final String innerQuery = query;
                        CompletableFuture<Long> t = CompletableFuture.supplyAsync(() -> {
                            long startTime = System.nanoTime();
                            OCLInput oclInput = new OCLInput(innerQuery);
                            List<Constraint> parse = null;
                            try {
                                parse = ocl.parse(oclInput);
                            } catch (ParserException e) {
                                return -1L;
                            }

                            for(Constraint constraint : parse){
                                boolean check = ocl.check(root, constraint);
                            }
                            long endTime = System.nanoTime();
                            return endTime - startTime;
                        });


                        Long res;
                        try {
                            res = t.get(maxWaitingTimePerThread, TimeUnit.MILLISECONDS);
                            if(res == -1){
                                t.cancel(true);
                            }
                        } catch (ExecutionException | InterruptedException | TimeoutException e) {
                            res = -1L;
                        }

                        out.write(String.valueOf(res));
                        out.write(';');
                    }
                    out.write('\n');
                }
            }
        }
    }

}
