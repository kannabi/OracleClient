package ru.nsu.fit.g14205.schukin.Utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kannabi on 25/03/2017.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresEDT {

    /**
     * Execution policy
     *
     * @return execution policy
     */
    RequiresEDTPolicy value() default RequiresEDTPolicy.ASYNC;
}
