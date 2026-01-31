package kaphein.indexable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>A marker annotation for classes that comply with the {@code Indexable} contract.</p>
 * 
 * <p>Classes annotated with {@code @IndexableComplient} declare their intent
 * to follow the contract's requirements, including:</p>
 * <ul>
 *   <li>Implementing the {@link Indexable} interface</li>
 *   <li>Providing a {@code PropertyDescriptors} inner class</li>
 *   <li>Supporting both known and extra properties</li>
 * </ul>
 * 
 * <p>See the {@linkplain kaphein.indexable package documentation} for
 * detailed contract explanation and usage examples.</p>
 * 
 * @see Indexable
 * @see PropertyDescriptor
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexableComplient
{

}
