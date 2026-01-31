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
 *   <li>Getting readable properties by a string key.</li>
 *   <li>Setting writable properties by a string key.</li>
 *   <li>Providing a {@code PropertyDescriptors} inner class.</li>
 *   <li>Distinguishing between known and extra properties.</li>
 * </ul>
 * 
 * <p>Implementations are <b>NOT</b> need to be thread-safe. 
 * Using immutable variants is recommended for thread safety requirements.</p>
 * 
 * <p>See the {@link kaphein.indexable} package documentation for
 * detailed contract explanation and usage examples.</p>
 * 
 * @see kahpein.indexable
 * @see Indexable
 * @see PropertyDescriptor
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexableComplient
{

}
