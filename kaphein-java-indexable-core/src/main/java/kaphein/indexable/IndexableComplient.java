package kaphein.indexable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The marker annotation for classes that comply with the {@code Indexable} contract.</p>
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
