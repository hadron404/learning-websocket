package com.example;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类上加此注解，会被作为WebSocketHandler处理
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component // 使用spring容器进行管理
public @interface ServerEndPoint {
	/**
	 * path of the websocket endpoint
	 */
	String value() default "";
}
