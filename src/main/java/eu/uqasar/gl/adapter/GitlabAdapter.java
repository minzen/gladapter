package eu.uqasar.gl.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;

import eu.uqasar.adapter.SystemAdapter;
import eu.uqasar.adapter.exception.uQasarException;
import eu.uqasar.adapter.model.BindedSystem;
import eu.uqasar.adapter.model.Measurement;
import eu.uqasar.adapter.model.User;
import eu.uqasar.adapter.model.uQasarMetric;
import eu.uqasar.adapter.query.QueryExpression;

public class GitlabAdapter implements SystemAdapter {

	private final static Logger LOGGER = 
			Logger.getLogger(GitlabAdapter.class.getName()); 
	
	// Constructor
    public GitlabAdapter() {
    	LOGGER.setLevel(Level.INFO);
    }

    @Override
    public List<Measurement> query(BindedSystem boundSystem, User user, 
    		QueryExpression queryExpression) throws uQasarException {

    	// For storing the measurements 
    	LinkedList<Measurement> measurements = new LinkedList<Measurement>();

        try {

        	// URL of the Gitlab instance
        	String url = boundSystem.getUri();
        	
        	// Init a Gitlab session
            GitlabSession session = GitlabAPI.connect(url, user.getUsername(), 
            		user.getPassword());
            
            // Obtain a private token from the session by authenticating
            String privateToken = session.getPrivateToken();
            
            LOGGER.info("PrivateToken: " +privateToken);
            
            // Connect to GitlabAPI
            GitlabAPI api = GitlabAPI.connect(url, privateToken);
            
            String query = queryExpression.getQuery();
            JSONArray measurementResultJSONArray = new JSONArray();

            if (query.equalsIgnoreCase(uQasarMetric.GIT_COMMITS.name())){
            	LOGGER.info("COMMITS...");
            	List<GitlabProject> projects = api.getProjects();
            	Integer commitsCount = null;
                for (GitlabProject gitlabProject : projects) {
                	LOGGER.info("Project: " +gitlabProject.getName());
    				List<GitlabCommit> listOfCommits = 
    						api.getAllCommits(gitlabProject.getId());
    				commitsCount = listOfCommits.size();
    	            LOGGER.info("Number of commits: " +commitsCount);
    				
    	            for (GitlabCommit commit : listOfCommits) {

    	            	JSONObject jObj = new JSONObject();
    	            	jObj.put("id", commit.getId());
    	            	jObj.put("shortId", commit.getShortId());
    	            	jObj.put("createdAt", commit.getCreatedAt());
    	            	jObj.put("title", commit.getTitle());
    	            	jObj.put("authorName", commit.getAuthorName());
    	            	jObj.put("authorEmail", commit.getAuthorEmail());
    	            	measurementResultJSONArray.put(jObj);

    					LOGGER.info("[ id: "+commit.getId() 
    							+", shortId: " +commit.getShortId()
    	            			+", createdAt: " +commit.getCreatedAt()
    	            			+", title: " +commit.getTitle()
    	            			+", authorName: " +commit.getAuthorName()
    	            			+", authorEmail: " +commit.getAuthorEmail()
    	            			+" ]");
    				}				
                }
                measurements.add(new Measurement(uQasarMetric.GIT_COMMITS, measurementResultJSONArray.toString()));
            } 
            
            if (query.equalsIgnoreCase(uQasarMetric.GIT_PROJECTS.name())) {
            	LOGGER.info("PROJECTS...");
            	List<GitlabProject> projects = api.getProjects();
            	Integer projectsCount = projects.size();
            	LOGGER.info("Number of projects: " +projectsCount);

            	for (GitlabProject gitlabProject : projects) {
            		
            		JSONObject jObj = new JSONObject();
            		jObj.put("id", gitlabProject.getId());
            		jObj.put("createdAt", gitlabProject.getCreatedAt());
            		jObj.put("defaultBranch", gitlabProject.getDefaultBranch());
            		jObj.put("description", gitlabProject.getDescription());
            		jObj.put("httpUrl", gitlabProject.getHttpUrl());
            		jObj.put("lastActivityAt", gitlabProject.getLastActivityAt());
            		jObj.put("name", gitlabProject.getName());
            		jObj.put("nameSpace", gitlabProject.getNamespace());
            		jObj.put("nameWithNamespace", gitlabProject.getNameWithNamespace());
            		jObj.put("owner", gitlabProject.getOwner());
            		jObj.put("path", gitlabProject.getPath());
            		jObj.put("pathWithNamespace", gitlabProject.getPathWithNamespace());
            		jObj.put("permissions", gitlabProject.getPermissions());
            		jObj.put("sshUrl", gitlabProject.getSshUrl());
            		jObj.put("visibilityLevel", gitlabProject.getVisibilityLevel());
            		jObj.put("webUrl", gitlabProject.getWebUrl());
            		// Add to JSON array
            		measurementResultJSONArray.put(jObj);
            		
					LOGGER.info("[ id: " +gitlabProject.getId() 
							+", createdAt: " +gitlabProject.getCreatedAt()
		            		+", defaultBranch: " +gitlabProject.getDefaultBranch()
		            		+", description: " +gitlabProject.getDescription()
		            		+", httpUrl: " +gitlabProject.getHttpUrl()
		            		+", lastActivityAt: " +gitlabProject.getLastActivityAt()
		            		+",name: " +gitlabProject.getName()
		            		+", nameSpace: " +gitlabProject.getNamespace()
		            		+", nameWithNamespace: " +gitlabProject.getNameWithNamespace()
		            		+", owner: " +gitlabProject.getOwner()
		            		+", path: " +gitlabProject.getPath()
		            		+", pathWithNamespace: " +gitlabProject.getPathWithNamespace()
		            		+", permissions: " +gitlabProject.getPermissions()
		            		+", sshUrl: " +gitlabProject.getSshUrl()
		            		+", visibilityLevel: " +gitlabProject.getVisibilityLevel()
		            		+", webUrl: " +gitlabProject.getWebUrl()
							+" ]");
				}
            	measurements.add(new Measurement(uQasarMetric.GIT_PROJECTS, 
            			measurementResultJSONArray.toString()));
            }
            
            return measurements;
            
        }   catch (Exception e) {
			e.printStackTrace();
			return measurements;
		}
    }

    @Override
    public List<Measurement> query(String boundSystemURL, String credentials, 
    		String queryExpression) throws uQasarException {
    	
        List<Measurement> measurements = null;

        BindedSystem boundSystem = new BindedSystem();
        boundSystem.setUri(boundSystemURL);
        User user = new User();

        String[] creds = credentials.split(":");

        user.setUsername(creds[0]);
        user.setPassword(creds[1]);

        GitlabQueryExpression gitlabQueryExpression = 
        		new GitlabQueryExpression(queryExpression);
        GitlabAdapter gitlabAdapter = new GitlabAdapter();
        // Get the measurements
        measurements = gitlabAdapter.query(boundSystem, user, 
        		gitlabQueryExpression);

        return measurements;
    }

    public void printMeasurements(List<Measurement> measurements){
        String newLine = System.getProperty("line.separator");
        for (Measurement measurement : measurements) {
            System.out.println("----------TEST metric: "
            		+measurement.getMetric()+" ----------" +newLine);
            System.out.println(measurement.getMeasurement() +newLine +newLine);
            System.out.println();

        }
    }

	//in order to invoke main from outside jar
	//mvn exec:java -Dexec.mainClass="eu.uqasar.gitlab.adapter.GitlabAdapter" 
    //-Dexec.args="https://gitlab.com user:pass"

	
	public static void main(String[] args) {
		
		
	    List<Measurement> measurements;
	    BindedSystem boundSystem = new BindedSystem();
	    boundSystem.setUri(args[0]);
	
	    // User
	    User user = new User();
	    
	    String[] credentials = args[1].split(":");
	    user.setUsername(credentials[0]);
	    user.setPassword(credentials[1]);
	
	    
	    try {
	    	GitlabAdapter gitlabAdapter = new GitlabAdapter();
	    	GitlabQueryExpression gitlabQueryExpression = 
	    			new GitlabQueryExpression(args[2]);    	
	        measurements = gitlabAdapter.query(boundSystem, user, 
	        		gitlabQueryExpression);
	        gitlabAdapter.printMeasurements(measurements);
	        
	    } catch (uQasarException e) {
	        e.printStackTrace();
	    }    
	}
}
