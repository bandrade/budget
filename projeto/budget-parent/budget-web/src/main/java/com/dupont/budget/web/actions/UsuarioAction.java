package com.dupont.budget.web.actions;


/**
 * Bean de criação o usuário.
 * 
 * A autenticação do usuário é feita atraves do Login Module do EAP. A configuração segue descrita abaixo: <br/>
 * 
 * <security-domain name="dupont-security-domain">
 *    <authentication>
 *        <login-module code="Database" flag="required">
 *            <module-option name="dsJndiName" value="java:/jboss/datasources/BudgetDS"/>
 *            <module-option name="principalsQuery" value="select password from usuario where login = ?"/>
 *            <module-option name="rolesQuery" value="select perfil, 'Roles' from usuario where login = ?"/>
 *            <module-option name="hashAlgorithm" value="SHA-256"/>
 *            <module-option name="hashEncoding" value="base64"/>
 *        </login-module>
 *	  </authentication>
 * </security-domain>
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public class UsuarioAction {

	// Para criar a senha HASH do password, utilizar o seguinte código.
	
	/* 
	 *   Criar o hash no JAVA:	 
	 * 
	 *   String pass = Util.createPasswordHash("SHA-256", "BASE64", null, null, "123");
     *   
     *   Criar o encoding no MySQL:
     *   
     *   TO_BASE64( sha2('PASSWORD', 256) )
	 */
	
}
