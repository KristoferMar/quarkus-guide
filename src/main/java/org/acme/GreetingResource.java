package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;

import org.jboss.logging.Logger;

@Path("/hello")
public class GreetingResource {
    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    private static final java.nio.file.Path fileLocation = Paths.get("/mnt/myvolume/hello.txt");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        LOG.infof("Creating file at: %s", fileLocation);

        try {
            Files.createDirectories(fileLocation.getParent());
            if (!Files.exists(fileLocation)) {
                Files.createFile(fileLocation);
            }
            Files.writeString(fileLocation, "Hello from Quarkus REST");

            // Getting the owner of the file
            UserPrincipal owner = Files.getOwner(fileLocation);
            LOG.infof("Original owner: %s", owner.getName());

            // Setting a new owner
            UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
            UserPrincipal newOwner = lookupService.lookupPrincipalByName("test_user");
            Files.setOwner(fileLocation, newOwner);

            // Logging the new owner
            owner = Files.getOwner(fileLocation);
            LOG.infof("New owner: %s", owner.getName());

            return "File created and owner changed successfully";
        } catch (IOException e) {
            LOG.error("Failed to create file or change owner: " + e.getMessage(), e);
            return "Failed to create file or change owner: " + e.getMessage();
        }
    }
}
