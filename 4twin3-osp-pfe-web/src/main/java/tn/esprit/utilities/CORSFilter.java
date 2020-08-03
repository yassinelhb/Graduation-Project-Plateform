package tn.esprit.utilities;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        String reqHeader = requestContext.getHeaderString("Access-Control-Request-Headers");
        if (reqHeader != null && reqHeader != "") {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", reqHeader);
        }
    }
}
