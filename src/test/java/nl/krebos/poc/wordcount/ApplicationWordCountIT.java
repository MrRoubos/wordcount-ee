package nl.krebos.poc.wordcount;

import java.net.URL;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Used resources:
 * https://openliberty.io/guides/arquillian-managed.html#developing-arquillian-tests
 * https://dzone.com/articles/how-to-write-embedded-integration-and-system-tests
 * 
 * @author jan
 *
 */
@RunWith(Arquillian.class)
public class ApplicationWordCountIT {
    private final static String WARNAME = System.getProperty("arquillian.war.name");
    private final String CALCULATE = "calculate/";
    private Client client = ClientBuilder.newClient();


    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                                       .addPackages(true, "nl.krebos.poc.wordcount");
        return archive;
    }
    
    @ArquillianResource
    private URL baseURL;
    
    @Inject
    WordFrequencyAnalyzerImpl invSrv;
    
    @Test
    @RunAsClient
    @InSequence(1)	// not needed, but sequence is possible
    public void checkAlive() {
 	   String localhosturl = baseURL + CALCULATE;
 	   System.out.println("Test checkAlive");
 	   System.out.println("baseUrl: " + baseURL);
 	   System.out.println("localhosturl: " + localhosturl);

       client.register(JsrJsonpProvider.class);
       WebTarget localhosttarget = client.target(localhosturl);
       Response localhostresponse = localhosttarget.request().get();

       Assert.assertEquals("Incorrect response code from " + localhosturl, 200,
                           localhostresponse.getStatus());       
       localhostresponse.close();
    }
    
    @Test
    @RunAsClient
    public void highestFrequencyTest_1() {
	   String localhosturl = baseURL + CALCULATE + "/highestfrequency";

       client.register(JsrJsonpProvider.class);
       WebTarget localhosttarget = client.target(localhosturl);
       
       JsonObject jsonRequest = Json.createObjectBuilder()                
 	          .add("text", "The sun shines over the lake").build();
              
       Response localhostresponse = localhosttarget.request().post(Entity.json(jsonRequest));

       Assert.assertEquals("Incorrect response code from " + localhosturl, 200,
                           localhostresponse.getStatus());

       JsonObject localhostobj = localhostresponse.readEntity(JsonObject.class);
       System.out.println("localhostobj: " + localhostobj);
       Assert.assertEquals("frequency should be 2", localhostobj.getInt("frequency"), 2);
       
       localhostresponse.close();
   }
    
    @Test
    @RunAsClient
    public void frequencyforwordTest_1() {
	   String localhosturl = baseURL + CALCULATE + "/frequencyforword";

       client.register(JsrJsonpProvider.class);
       WebTarget localhosttarget = client.target(localhosturl);
       
       JsonObject jsonRequest = Json.createObjectBuilder()                
 	          .add("text", "The sun shines over the lake")
 	          .add("word", "the")
 	          .build();
              
       Response localhostresponse = localhosttarget.request().post(Entity.json(jsonRequest));

       Assert.assertEquals("Incorrect response code from " + localhosturl, 200,
                           localhostresponse.getStatus());

       JsonObject localhostobj = localhostresponse.readEntity(JsonObject.class);
       System.out.println("localhostobj: " + localhostobj);
       Assert.assertEquals("frequency should be 2", localhostobj.getInt("frequency"), 2);
       
       localhostresponse.close();
   }    

    @Test
    @RunAsClient
    public void mostfrequentnwordsTest_1() {
	   String localhosturl = baseURL + CALCULATE + "/mostfrequentnwords";

       client.register(JsrJsonpProvider.class);
       WebTarget localhosttarget = client.target(localhosturl);
       
       JsonObject jsonRequest = Json.createObjectBuilder()                
 	          .add("text", "The sun shines over the lake")
 	          .add("n", 3)
 	          .build();
              
       Response localhostresponse = localhosttarget.request().post(Entity.json(jsonRequest));

       Assert.assertEquals("Incorrect response code from " + localhosturl, 200,
                           localhostresponse.getStatus());

       // Construct expected Json response
       JsonArrayBuilder outerArray = Json.createArrayBuilder();
       JsonArrayBuilder innerArray = Json.createArrayBuilder();
       innerArray.add("the");			
       innerArray.add(2);
       outerArray.add(innerArray);

       innerArray.add("lake");			
       innerArray.add(1);
       outerArray.add(innerArray);       

       innerArray.add("over");			
       innerArray.add(1);
       outerArray.add(innerArray);       
       
       JsonObject expectedResponse = Json.createObjectBuilder()                
  	          .add("wordlist", outerArray).build();
		
       
       JsonObject localhostobj = localhostresponse.readEntity(JsonObject.class);
       System.out.println("localhostobj: " + localhostobj);
       
       Assert.assertEquals("frequency should be 2", localhostobj, expectedResponse);
       
       localhostresponse.close();
   }    
    
}
