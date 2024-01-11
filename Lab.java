package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;

public class BackDoors extends LessonAdapter{
    private final static String USERNAME = "username";
    private final static String SELECT_ST = "select userid, password, ssn, salary from employee where userid=";
    protected Element concept1(WebSession s) throws Exception{
		ElementContainer ec = new ElementContainer();
		ec.addElement(makeUsername(s));

		try{
			String userInput = s.getParser().getRawParameter(USERNAME, "");
				if (!userInput.equals("")){
					String sql = SELECT_ST + userInput;
					Connection conn = getConnection(s);
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					if (rs.next()){
						Table t = new Table(0).setCellSpacing(0).setCellPadding(0)
							.setBorder(1);
						TR tr = new TR();
						tr.addElement(new TD("User ID"));
						tr.addElement(new TD("Password"));
						tr.addElement(new TD("SSN"));
						tr.addElement(new TD("Salary"));
						t.addElement(tr);
						tr = new TR();
						String ud = rs.getString("userid");
						String pd = rs.getString("password");
						String sn = rs.getString("ssn");
						String sy = rs.getString("salary");
						tr.addElement(new TD(ud));
						tr.addElement(new TD(pd));
						tr.addElement(new TD(sn));
						tr.addElement(new TD(sy));
						t.addElement(tr);
						ec.addElement(t);
					}
				}
		}
		catch (Exception ex){
			
		}
		return ec;
    }
	protected Element makeUsername(WebSession s){
		ElementContainer ec = new ElementContainer();
		StringBuffer script = new StringBuffer();
		script.append("<STYLE TYPE=\"text/css\"> ");
		script.append(".blocklabel { margin-top: 8pt; }");
		script.append(".myClass 	{ color:red;");
		script.append(" font-weight: bold;");
		script.append("padding-left: 1px;");
		script.append("padding-right: 1px;");
		script.append("background: #DDDDDD;");
		script.append("border: thin black solid; }");
		script.append("LI	{ margin-top: 10pt; }");
		script.append("</STYLE>");
		ec.addElement(new StringElement(script.toString()));
		ec.addElement(new StringElement("User ID: "));
		Input username = new Input(Input.TEXT, "username", "");
		ec.addElement(username);
		String userInput = s.getParser().getRawParameter("username", "");
		ec.addElement(new BR());
		ec.addElement(new BR());
		String formattedInput = "<span class='myClass'>" + userInput
			+ "</span>";
		ec.addElement(new Div(SELECT_ST + formattedInput));
		Input b = new Input();
		b.setName("Submit");
		b.setType(Input.SUBMIT);
		b.setValue("Submit");
		ec.addElement(new PRE(b));
		return ec;
    }
}