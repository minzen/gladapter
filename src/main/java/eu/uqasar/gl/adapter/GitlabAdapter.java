package eu.uqasar.gl.adapter;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

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

            // Connection to GitLab instance
            uri = new URI(boundSystem.getUri());
						
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

        // TBD

        GitlabQueryExpression gitlabQueryExpression = new GitlabQueryExpression(queryExpression);

        GitlabAdapter gitlabAdapter = new GitlabAdapter();

        measurements = gitlabAdapter.query(boundSystem,user,gitlabQueryExpression);

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
}
