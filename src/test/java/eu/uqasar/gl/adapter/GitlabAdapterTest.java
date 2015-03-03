package eu.uqasar.gl.adapter;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.uqasar.adapter.exception.uQasarException;
import eu.uqasar.adapter.model.Measurement;

/**
 * 
 * @author Feetu Nyrhinen <nyrhinen@atb-bremen.de>
 *
 */
public class GitlabAdapterTest {

	GitlabAdapter gitlabAdapter = new GitlabAdapter();
    String newLine = System.getProperty("line.separator");
    String boundSystemURL = "https://gitlab.com";
    String credentials = "user:pass";
    String queryExpression = "";

    
    @Test
    public void testGetCommits() {
    	queryExpression = "GIT_COMMITS";
    	try {
			List<Measurement> measurements = gitlabAdapter.query(boundSystemURL, credentials, queryExpression);
			Assert.assertNotNull(measurements);
			gitlabAdapter.printMeasurements(measurements);
		} catch (uQasarException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testGetProjects() {
    	queryExpression = "GIT_PROJECTS";
    	try {
			List<Measurement> measurements = gitlabAdapter.query(boundSystemURL, credentials, queryExpression);
			Assert.assertNotNull(measurements);
			gitlabAdapter.printMeasurements(measurements);
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    }
    
    
}
