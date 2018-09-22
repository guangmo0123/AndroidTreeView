package billy.snxi.mytreeview.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，node的id
 */

@Target(ElementType.FIELD)              //定义该注解的目标对象，此处为类的字段
@Retention(RetentionPolicy.RUNTIME)     //定义该注解的有效范围，此处为运行时
public @interface TreeNodeId {
}
