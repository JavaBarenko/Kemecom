package br.rcp.kemecom.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.interceptor.InterceptorBinding;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 Marca todos os metodos que soh poderao ser acessados se o usuario estiver logado
 <p/>
 @author barenko
 */
@InterceptorBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secure {
}
