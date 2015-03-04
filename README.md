# GitlabAdapter

GitlabAdapter is a component enabling data exchange with Gitlab version 
control system (https://about.gitlab.com) implementing the uQasarAdapter 
interface and overriding the following methods:

<strong><pre>public  List<Measurement> query(BindedSystem bindedSystem, User user, QueryExpression queryExpression) throws uQasarException;</pre></strong>
<strong><pre>public List<Measurement> query(String bindedSystemURL, String credentials,	String queryExpression) throws uQasarException;</pre></strong>


