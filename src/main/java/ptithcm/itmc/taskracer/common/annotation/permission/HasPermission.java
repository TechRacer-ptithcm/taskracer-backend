package ptithcm.itmc.taskracer.common.annotation.permission;

import java.lang.annotation.*;

//Stage 2
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasPermission {
    String value();
}
