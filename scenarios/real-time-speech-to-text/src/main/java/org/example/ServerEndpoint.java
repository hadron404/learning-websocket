package org.example;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 类上加此注解，会被作为WebSocketHandler处理
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component // 使用spring容器进行管理
public @interface ServerEndpoint {
	/**
	 * path of the websocket endpoint
	 */
	String value() default "";
}
