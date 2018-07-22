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

package serum.injectables.components;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * This represents an invocation delegate for {@link serum.injectables.components.DynamicProperty}.
 */
@FunctionalInterface
public interface InvocationDelegate {

    /**
     * This is called to get a dynamic annotation property.
     *
     * @param propertyOwner The top-level owner of the property.
     * @param propertyName The name of the property.
     * @param propertyType The expected type for the property value.
     * @param currValue The current value of the property.
     * @return The generated property value.
     */
    @Nullable
    Object invoke(Annotation propertyOwner, String propertyName, Class<?> propertyType, @Nullable Object currValue);
}
