/*
 * This file is part of Serum.
 *
 * Serum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Serum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Serum.  If not, see <http://www.gnu.org/licenses/>.
 */

package serum;

import com.austinv11.graphs.Graph;
import com.austinv11.graphs.impl.DirectedAcyclicGraph;
import com.austinv11.graphs.impl.SimpleGraph;
import serum.injectables.components.Abstract;
import serum.injectables.components.Extends;
import serum.injectables.components.Injectable;
import serum.injectables.components.Spreadable;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.*;
import java.util.stream.Collectors;

public final class SerumManager {

    private static final DirectedAcyclicGraph<Class<? extends Annotation>, AnnotationVertex, AnnotationEdge>
            graph = new DirectedAcyclicGraph<>(new SimpleGraph<>(true));

    static {
        register(Injectable.class);
    }

    private SerumManager() {}

    private static boolean isJdkAnnotation(Class<? extends Annotation> annotation) {
        return annotation.getPackage().getName().startsWith("java.")
                || annotation.getPackage().getName().startsWith("javax.")
                || annotation.getPackage().getName().startsWith("com.sun.")
                || annotation.getPackage().getName().startsWith("sun.")
                || annotation.getPackage().getName().startsWith("jdk.")
                || annotation.getPackage().getName().startsWith("com.oracle.");
    }
    
    private static Spreadable concatSpreads(Spreadable s1, Spreadable s2) {
        Set<ElementType> from = new HashSet<>();
        Set<ElementType> to = new HashSet<>();
        from.addAll(Arrays.asList(s1.from()));
        from.addAll(Arrays.asList(s2.from()));
        to.addAll(Arrays.asList(s1.to()));
        to.addAll(Arrays.asList(s2.to()));

        return new Spreadable() {
            ElementType[] fromArray = from.toArray(new ElementType[0]);
            ElementType[] toArray = to.toArray(new ElementType[0]);

            @Override
            public Class<? extends Annotation> annotationType() {
                return Spreadable.class;
            }

            @Override
            public ElementType[] from() {
                return fromArray;
            }

            @Override
            public ElementType[] to() {
                return toArray;
            }
        };
    }
    
    @Nullable
    private static Spreadable getSpread(Class<? extends Annotation> a, @Nullable Spreadable currSpread) {
        if (a.equals(Spreadable.class) || a.equals(Injectable.class) || a.equals(Extends.class) || isJdkAnnotation(a))
            return null;

        List<Class<? extends Annotation>> held = Arrays.stream(a.getAnnotations()).map
                (Annotation::annotationType).collect(Collectors.toList());

        // We support both dynamic inheritance via @Extends or static inheritance via meta annotating
        if (a.equals(Extends.class)) {
            Extends[] extensions = a.getDeclaredAnnotationsByType(Extends.class);
            for (Extends e : extensions)
                held.add(e.value());
        }

        for (Class<? extends Annotation> a2 : held) {
            if (!isJdkAnnotation(a2)) {
                if (currSpread == null) {
                    currSpread = getSpread(a2, null);
                } else {
                    Spreadable spread = getSpread(a2, currSpread);
                    if (spread != null)
                        currSpread = concatSpreads(currSpread, spread);
                }
            }
        }

        return currSpread;
    }

    private static Set<Class<? extends Annotation>> getAncestors(Class<? extends Annotation> currAnnotation) {
        Set<Class<? extends Annotation>> directAncestors = new HashSet<>();
        for (Annotation a : currAnnotation.getDeclaredAnnotations()) {
            if (!isJdkAnnotation(a.annotationType())) {
                // We support both dynamic inheritance via @Extends or static inheritance via meta annotating
                if (a.annotationType().equals(Extends.class)) {
                    Extends[] extensions = a.annotationType().getDeclaredAnnotationsByType(Extends.class);
                    for (Extends e : extensions)
                        directAncestors.add(e.value());
                } else {
                    directAncestors.add(a.annotationType());
                }
            }
        }

        //Special case handling: Check if the package contains spreadable annotations to annotation
        // types, if so, traverse them
        for (Annotation a : currAnnotation.getPackage().getDeclaredAnnotations()) {
            if (!isJdkAnnotation(a.annotationType())) {
                Spreadable spread = getSpread(a.annotationType(), null);
                if (spread == null)
                    continue;
                Set<ElementType> from = new HashSet<>(Arrays.asList(spread.from()));
                Set<ElementType> to = new HashSet<>(Arrays.asList(spread.to()));
                if (from.contains(ElementType.PACKAGE) && to.contains(ElementType.ANNOTATION_TYPE))
                    // We support both dynamic inheritance via @Extends or static inheritance via meta annotating
                    if (a.annotationType().equals(Extends.class)) {
                        Extends[] extensions = a.annotationType().getDeclaredAnnotationsByType(Extends.class);
                        for (Extends e : extensions)
                            directAncestors.add(e.value());
                    } else {
                        directAncestors.add(a.annotationType());
                    }
            }
        }

        return directAncestors;
    }

    private static void traverseAnnotation(Class<? extends Annotation> currAnnotation,
                                           Graph<Class<? extends Annotation>, AnnotationVertex, AnnotationEdge> graph) {
        if (isJdkAnnotation(currAnnotation))
            return;

        Set<Class<? extends Annotation>> directAncestors = getAncestors(currAnnotation);

        for (Class<? extends Annotation> ancestor : directAncestors) {
            if (!graph.areConnected(ancestor, currAnnotation)) {
                graph.addEdge(new AnnotationEdge(new AnnotationVertex(ancestor), new AnnotationVertex(currAnnotation)));
                traverseAnnotation(ancestor, graph);
            }
        }
    }

    public static void register(Class<? extends Annotation> injectable) {
        if (injectable.equals(Injectable.class) || isJdkAnnotation(injectable))
            return;

        if (graph.findVertex(injectable) != null)
            return;

        Graph<Class<? extends Annotation>, AnnotationVertex, AnnotationEdge>
                graph = new DirectedAcyclicGraph<>(new SimpleGraph<>(false));
        traverseAnnotation(injectable, graph);

        if (graph.values().contains(Injectable.class)) { //Ensure it's a valid inheritance for this
            for (AnnotationEdge e : graph.edges()) {
                if (!SerumManager.graph.areConnected(e.getFirstVertex(), e.getSecondVertex())) {
                    SerumManager.graph.addEdge(e);
                }
            }
        } else {
            throw new IllegalArgumentException(injectable + " does not descend from @Injectable!");
        }
    }

    public static Collection<Class<? extends Annotation>> registeredInjectables() {
        return graph.values();
    }

    public static <T extends Annotation> boolean isRegistered(Class<T> clazz) {
        return graph.findVertex(clazz) != null;
    }

    public static <T extends Annotation> boolean isAbstract(Class<T> clazz) {
        return graph.areConnected(Abstract.class, clazz);
    }
}
