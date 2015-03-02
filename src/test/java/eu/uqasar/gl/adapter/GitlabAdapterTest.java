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
    String boundSystemURL = "https://gitlab.com";
    String credentials = "user:pass";
    String queryExpression = "";

    
    @Test
    public void testGetCommits() {
    	queryExpression = "GIT_COMMITS";
    	try {
			gitlabAdapter.query(boundSystemURL, credentials, queryExpression);
		} catch (uQasarException e) {
			e.printStackTrace();
		}
    }
}
