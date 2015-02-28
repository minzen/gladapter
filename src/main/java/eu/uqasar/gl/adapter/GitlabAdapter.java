package eu.uqasar.gl.adapter;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;

import eu.uqasar.adapter.SystemAdapter;
import eu.uqasar.adapter.exception.uQasarException;
import eu.uqasar.adapter.model.BindedSystem;
import eu.uqasar.adapter.model.Measurement;
import eu.uqasar.adapter.model.User;
import eu.uqasar.adapter.query.QueryExpression;

public class GitlabAdapter implements SystemAdapter {


    public GitlabAdapter() {
    }

    @Override
    public List<Measurement> query(BindedSystem boundSystem, User user, QueryExpression queryExpression) throws uQasarException {
        URI uri = null;
        LinkedList<Measurement> measurements = new LinkedList<Measurement>();

        try {

        	String url = boundSystem.getUri();
            // Connection to GitLab instance
            uri = new URI(boundSystem.getUri());
            
            
            GitlabSession session = GitlabAPI.connect(url, user.getUsername(), user.getPassword());
            String privateToken = session.getPrivateToken();
            System.out.println("PrivateToken: " +privateToken);
            GitlabAPI api = GitlabAPI.connect(url, privateToken);
            List<GitlabProject> projects = api.getAllProjects();
            for (GitlabProject gitlabProject : projects) {
				System.out.println(gitlabProject.getName());
			}
        } catch (Exception e) {
			e.printStackTrace();
		}

        return measurements;


    }

    @Override
    public List<Measurement> query(String boundSystemURL, String credentials, String queryExpression) throws uQasarException {
        List<Measurement> measurements = null;

        BindedSystem boundSystem = new BindedSystem();
        boundSystem.setUri(boundSystemURL);
        User user = new User();

        String[] creds = credentials.split(":");

        user.setUsername(creds[0]);
        user.setPassword(creds[1]);

        GitlabQueryExpression gitlabQueryExpression = new GitlabQueryExpression(queryExpression);
        GitlabAdapter gitlabAdapter = new GitlabAdapter();
        // Get the measurements
        measurements = gitlabAdapter.query(boundSystem, user, gitlabQueryExpression);

        return measurements;
    }

    public void printMeasurements(List<Measurement> measurements){
        String newLine = System.getProperty("line.separator");
        for (Measurement measurement : measurements) {
            System.out.println("----------TEST metric: "+measurement.getMetric()+" ----------"+newLine);
            System.out.println(measurement.getMeasurement()+newLine+newLine);
            System.out.println();

        }
    }

	//in order to invoke main from outside jar
	//mvn exec:java -Dexec.mainClass="eu.uqasar.gitlab.adapter.GitlabAdapter" -Dexec.args="https://gitlab.com user:pass"

	
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
	    	GitlabQueryExpression gitlabQueryExpression = new GitlabQueryExpression(args[2]);    	
	        measurements = gitlabAdapter.query(boundSystem, user, gitlabQueryExpression);
	        gitlabAdapter.printMeasurements(measurements);
	        
	    } catch (uQasarException e) {
	        e.printStackTrace();
	    }    
	}
}
