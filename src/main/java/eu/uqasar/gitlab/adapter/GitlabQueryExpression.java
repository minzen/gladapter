package eu.uqasar.gitlab.adapter;

import eu.uqasar.adapter.query.QueryExpression;

public class GitlabQueryExpression extends QueryExpression {

    String query;

    public GitlabQueryExpression(String query) {
        super(query);
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
