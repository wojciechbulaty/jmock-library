package org.jmock.internal;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.jmock.api.Expectation;
import org.jmock.api.ExpectationError;
import org.jmock.api.Invocation;

public class InvocationDispatcher implements ExpectationCollector, SelfDescribing {
	private List<Expectation> expectations = new ArrayList<Expectation>();
	private List<StateMachine> stateMachines = new ArrayList<StateMachine>();
    
    public StateMachine newStateMachine(String name) {
        StateMachine stateMachine = new StateMachine(name);
        stateMachines.add(stateMachine);
        return stateMachine;
    }
    
	public void add(Expectation expectation) {
		expectations.add(expectation);
	}
	
    public void describeTo(Description description) {
        if (expectations.isEmpty()) {
            description.appendText("no expectations specified: did you...\n"+
                                   " - forget to start an expectation with a cardinality clause?\n" +
                                   " - call a mocked method to specify the parameter of an expectation?");
        }
        else {
            description.appendList("expectations:\n  ", "\n  ", "", expectations);
            if (!stateMachines.isEmpty()) {
                description.appendList("\nstates:\n  ", "\n  ", "", stateMachines);
            }
        }
    }
    
    public boolean isSatisfied() {
		for (Expectation expectation : expectations) {
		    if (! expectation.isSatisfied()) {
                return false;
            }
        }
        return true;
	}
	
	public Object dispatch(Invocation invocation) throws Throwable {
		for (Expectation expectation : expectations) {
		    if (expectation.matches(invocation)) {
		        return expectation.invoke(invocation);
            }
        }
        
        throw new ExpectationError("unexpected invocation", this, invocation);
	}
}