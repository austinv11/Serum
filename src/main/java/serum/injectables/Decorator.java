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

import serum.injectables.components.Extends;
import serum.injectables.components.Injectable;
import serum.injectables.components.PropertyBinding;
import serum.injectables.components.Spreadable;
import syringe.callbacks.method.ExceptionThrownCallback;
import syringe.callbacks.method.MethodInvocationCallback;
import syringe.callbacks.method.MethodReturnCallback;

import java.lang.annotation.*;

@Injectable
@Spreadable(from = ElementType.TYPE, to = {ElementType.METHOD, ElementType.CONSTRUCTOR})
@Extends(InterceptInvocation.class)
@Extends(InterceptReturn.class)
@Extends(InterceptException.class)
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decorator {

    @PropertyBinding(binding = "callback", from = InterceptInvocation.class)
    Class<? extends MethodInvocationCallback> onInvoke();

    @PropertyBinding(binding = "callback", from = InterceptReturn.class)
    Class<? extends MethodReturnCallback> onReturn();

    @PropertyBinding(binding = "callback", from = InterceptException.class)
    Class<? extends ExceptionThrownCallback> onException();
}
