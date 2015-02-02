package fonctions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifInjectionXSS {
	
    private static Pattern pattern;
    private static Matcher matcher;
	

   
    
    
    
	public VerifInjectionXSS verif(String var) throws Exception {
		

	
	pattern = Pattern.compile("((<script>)|(<style>)|(<script)|(<style)|(</style>)|(<script))"); // chaine de vérif
	matcher = pattern.matcher(var); // chaine a vérifier
	
	if(matcher.find()) { // si on toruve une occurence
		throw new Exception("Erreur Injection XSS"); // on renvoie une erreur
	}
	return null;

	}
}
