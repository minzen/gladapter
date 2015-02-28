package eu.uqasar.gl.adapter;

import org.junit.Test;

import eu.uqasar.adapter.exception.uQasarException;

/**
 * 
 * @author Feetu Nyrhinen <nyrhinen@atb-bremen.de>
 *
 */
public class GitlabAdapterTest {

	GitlabAdapter gitlabAdapter = new GitlabAdapter();
    String newLine = System.getProperty("line.separator");
    String boundSystemURL = "http://www.gitlab.com/";
    String credentials = "user:passwd";
    String queryExpression = "XXX"; //TODO: Fix this

    
    @Test
    public void testGetProjects() {
    	try {
			gitlabAdapter.query(boundSystemURL, credentials, queryExpression);
		} catch (uQasarException e) {
			e.printStackTrace();
		}
    }
}
