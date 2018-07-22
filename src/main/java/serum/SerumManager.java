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

import com.austinv11.graphs.impl.DirectedAcyclicGraph;
import com.austinv11.graphs.impl.SimpleGraph;
import serum.injectables.components.Injectable;

import java.lang.annotation.Annotation;

public final class SerumManager {

    private static final DirectedAcyclicGraph<Class<? extends Annotation>, AnnotationVertex, AnnotationEdge>
            graph = new DirectedAcyclicGraph<>(new SimpleGraph<>(true));

    static {
        graph.addVertex(new AnnotationVertex(Injectable.class));
    }

    private SerumManager() {}
}
