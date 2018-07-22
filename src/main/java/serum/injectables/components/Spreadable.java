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
 * This is a meta-annotation which acts similarly to {@link javax.annotation.meta.TypeQualifier}. Essentially, this
 * provides a way to have injectables "spread" to child elements to reduce boilerplate. For example, you can have
 * an @NonNull annotation equivalent spreadable from {@link java.lang.annotation.ElementType#PACKAGE} to
 * {@link java.lang.annotation.ElementType#METHOD}. Now, when you annotation a package with this annotation,
 * the {@link serum.SerumManager} will automatically apply @NonNull to every method within the package ("spreading" it).
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Spreadable {

    /**
     * The top-level targets to consider for spreading.
     *
     * @return The targets where spreading can occur.
     */
    ElementType[] from();

    /**
     * The lower-level child targets for spreading to be done.
     *
     * @return The targets from which top-level annotations are applied to.
     */
    ElementType[] to();
}
