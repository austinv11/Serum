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

import java.lang.annotation.*;

/**
 * This is a helper annotation for defining annotation inheritence. This allows for linking some of the current
 * annotation's properties to a superclass (annotation)'s property.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyBinding {

    /**
     * The property this should be bound to on the superclass.
     *
     * @return The name of the binding.
     */
    String binding();

    /**
     * The superclass that this property is bound to.
     *
     * @return The superclass to bind.
     */
    Class<? extends Annotation> from();
}
