package tn.esprit.webServices;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tn.esprit.pfe.entities.User;
import tn.esprit.pfe.services.UserService;

@Path("authentication")
@RequestScoped
public class AuthenticationWebService {
	
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	@EJB
	UserService us;

	@Context
	private UriInfo uriInfo;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User u) {
		System.out.println("Authenticating user... "+u.getPlainPassword()+"///"+u.getPassword());
		//Set<Object> response = new HashSet<>();
		Map<String, Object> response = new HashMap<>();
		User user=us.login(u.getEmail(), u.getPassword());
		if (user==null) {
			return Response.status(Status.FORBIDDEN).entity("Erreur de connexion").build();
		}
		else {
			response.put("user",user);
			String token=issueToken(user);
			response.put("token",token);
			NewCookie cookie=new NewCookie("token", AUTHENTICATION_SCHEME+" "+token,"/",uriInfo.getBaseUri().getHost().toString(),"",600,false);
			return Response.status(Status.OK).entity(response).cookie(cookie).build();
		}
	}

	private String issueToken(User user) {
		// Issue a token (can be a random String persisted to a database or a JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		String keyString = "simplekey";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		System.out.println("the key is : " + key.hashCode());
		System.out.println("uriInfo.getAbsolutePath().toString() : " + uriInfo.getAbsolutePath().toString());
		System.out.println("Expiration date: " + toDate(LocalDateTime.now().plusMinutes(15L)));
		System.out.println("role: "+ user.getClass().getSimpleName());
		String jwtToken = Jwts.builder().setSubject(user.getEmail()).claim("role", user.getRole()).claim("id", user.getId()).setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
				.signWith(SignatureAlgorithm.HS512, key).compact();
		System.out.println("the returned token is : " + jwtToken);
		return jwtToken;
	}

	// ======================================
	// = Private methods =
	// ======================================
	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

}
