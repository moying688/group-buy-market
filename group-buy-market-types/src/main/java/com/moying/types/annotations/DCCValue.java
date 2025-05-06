package com.moying.types.annotations;

import java.lang.annotation.*;

/**
 * @Author: moying
 * @CreateTime: 2025-05-06
 * @Description:
 */


@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD})
public @interface DCCValue {

    String value () default "";
}
