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

package serum.injectables;

import serum.injectables.components.Injectable;
import serum.injectables.components.Spreadable;
import syringe.callbacks.method.MethodReturnCallback;

import java.lang.annotation.*;

/**
 * This marks a target for return interception.
 */
@Injectable
@Spreadable(from = ElementType.TYPE, to = {ElementType.METHOD, ElementType.CONSTRUCTOR})
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptReturn {

    Class<? extends MethodReturnCallback> callback();
}
