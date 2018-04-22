# MUnit Short Tutorial

## Overview

This simple, short tutorial takes you through the process of creating a series of MUnit tests, which are aimed at validating the behavior of a simple code example.

This tutorial uses only core components of Mule ESB. No Anypoint Connectors are used; however, you can easily apply what you learn here to Anypoint Connectors.

## Sample Production Code

The sample code for this tutorial is fairly simple, but it uses some of Mule's most common message processors. It implements a basic use case:

1. It receives an HTTP request.
1. It extracts data from the request to route a message through the application.
1. It decides how to create a response.

*Production code - doc.xml*

``` xml
<?xml version="1.0" encoding="UTF-8"?>

<mule xmlns:http="http://www.mulesoft.org/schema/mule/http"
	xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core"
	xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
	xmlns:spring="http://www.springframework.org/schema/beans" version="EE-3.6.1"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd
http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd">

    <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="9090" doc:name="HTTP Listener Configuration"/>

    <flow name="exampleFlow">
        <http:listener config-ref="HTTP_Listener_Configuration" path="/" allowedMethods="GET" doc:name="HTTP"/>
        <set-payload value="#[message.inboundProperties['http.query.params']['url_key']]" doc:name="Set Original Payload"/>

        <flow-ref name="exampleFlow2" doc:name="exampleFlow2"/>


        <choice doc:name="Choice">
            <when expression="#[flowVars['my_variable'].equals('var_value_1')]">
                <set-payload value="#['response_payload_1']" doc:name="Set Response Payload"/>
            </when>
            <otherwise>
                <set-payload value="#['response_payload_2']" doc:name="Set Response Payload"/>
            </otherwise>
        </choice>
    </flow>

    <flow name="exampleFlow2">
        <choice doc:name="Choice">
            <when expression="#['payload_1'.equals(payload)]">
                <flow-ref name="exampleSub_Flow1" doc:name="exampleSub_Flow1"/>
            </when>
            <otherwise>
                <flow-ref name="exampleSub_Flow2" doc:name="exampleSub_Flow2"/>
            </otherwise>
        </choice>
	</flow>

    <sub-flow name="exampleSub_Flow1">
        <set-variable variableName="my_variable" value="#['var_value_1']" doc:name="my_variable"/>
    </sub-flow>

    <sub-flow name="exampleSub_Flow2">
        <set-variable variableName="my_variable" value="#['var_value_2']" doc:name="my_variable"/>
    </sub-flow>
</mule>
```

We'll analyze the above code by breaking it up into sections. The first section is the entry point of the application, which contains the HTTP listener in `exampleFlow`.

*exampleFlow*

``` xml
<flow name="exampleFlow">                                                                                                 //1
    <http:listener config-ref="HTTP_Listener_Configuration" path="/" allowedMethods="GET" doc:name="HTTP"/>

    <set-payload value="#[message.inboundProperties['http.query.params']['url_key']]" doc:name="Set Original Payload"/>   //2

    <flow-ref name="exampleFlow2" doc:name="exampleFlow2"/>                                                               //3

    <choice doc:name="Choice">                                                                                            //4
        <when expression="#[flowVars['my_variable'].equals('var_value_1')]">
            <set-payload value="#['response_payload_1']" doc:name="Set Response Payload"/>                                //5
        </when>
        <otherwise>
            <set-payload value="#['response_payload_2']" doc:name="Set Response Payload"/>                                //6
        </otherwise>
    </choice>
</flow>
```

1. Main Flow, `exampleFlow`.
2. Take data from the HTTP request.
3. Make call to `exampleFlow2` for further processing.
4. Make a decision based on the value of the invocation variable `my_variable`, which was set by `exampleFlow2`.
5. Creates `response payload 1`.
6. Creates `response payload 2`.

The second part of the app, `exampleFlow2`, is basically for routing. It does not perform any real action over the message or its payload. Rather, it delegates that to two other subflows, based on the payload received as input.


*exampleFlow2*

``` xml
<flow name="exampleFlow2">
    <choice doc:name="Choice">
        <when expression="#['payload_1'.equals(payload)]">                                          //1
            <flow-ref name="exampleSub_Flow1" doc:name="exampleSub_Flow1"/>                         //2
        </when>
        <otherwise>
            <flow-ref name="exampleSub_Flow2" doc:name="exampleSub_Flow2"/>                         //3
        </otherwise>
    </choice>
</flow>
```

1. Evaluate the payload that enters the flow.
2. Make call to `exampleSub_Flow1`.
3. Make call to `exampleSub_Flow2`.

Finally we have the various subflows called `exampleSub_Flow<number>`, whose only task is to set a value for an _invocation_ variable named `my_variable`.

*exampleSub_Flow(s)*

``` xml
<sub-flow name="exampleSub_Flow1">
    <set-variable variableName="my_variable" value="#['var_value_1']" doc:name="my_variable"/>    //1
</sub-flow>

<sub-flow name="exampleSub_Flow2">
    <set-variable variableName="my_variable" value="#['var_value_2']" doc:name="my_variable"/>    //2
</sub-flow>
```

1. Set `my_variable` to `var_value_1`.
2. Set `my_variable` to `var_value_2`.

## Creating Tests

Below is the MUnit Test Suite file:

*MUnit Test Suite file - doc-test.xml*

``` xml
<?xml version="1.0" encoding="UTF-8"?>

<mule xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:mock="http://www.mulesoft.org/schema/mule/mock"
	xmlns:munit="http://www.mulesoft.org/schema/mule/munit" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
	xmlns:spring="http://www.springframework.org/schema/beans" xmlns:core="http://www.mulesoft.org/schema/mule/core"
	version="EE-3.6.1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.mulesoft.org/schema/mule/mock http://www.mulesoft.org/schema/mule/mock/current/mule-mock.xsd
http://www.mulesoft.org/schema/mule/munit http://www.mulesoft.org/schema/mule/munit/current/mule-munit.xsd
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd">

    <munit:config name="munit" doc:name="Munit configuration"/>

    <spring:beans>
        <spring:import resource="classpath:demo.xml"/>
    </spring:beans>

    <!-- exampleFlow2 Tests -->
    <munit:test name="doc-test-exampleFlow2Test1" description="Validate calls to sub flows are being done properly ">
        <munit:set payload="#['payload_1']" doc:name="Set Message payload == payload_1"/>
        <flow-ref name="exampleFlow2" doc:name="Flow-ref to exampleFlow2"/>
        <mock:verify-call messageProcessor="mule:sub-flow" doc:name="Verify Call" times="1">
            <mock:with-attributes>
                <mock:with-attribute whereValue="#[matchContains('exampleSub_Flow1')]" name="name"/>
            </mock:with-attributes>
        </mock:verify-call>
    </munit:test>

     <munit:test name="doc-test-exampleFlow2Test2" description="Validate calls to sub flows are being done properly ">
        <munit:set payload="#['payload_2']" doc:name="Set Message payload == payload_2"/>
        <flow-ref name="exampleFlow2" doc:name="Flow-ref to exampleFlow2"/>
        <mock:verify-call messageProcessor="mule:sub-flow" doc:name="Verify Call" times="1">
            <mock:with-attributes>
                <mock:with-attribute whereValue="#[matchContains('exampleSub_Flow2')]" name="name"/>
            </mock:with-attributes>
        </mock:verify-call>
    </munit:test>

    <!-- exampleFlow Tests -->
    <munit:test name="doc-test-exampleFlow-unit-Test_1" description="Unit Test case asserting scenario 1">
        <mock:when messageProcessor="mule:set-payload" doc:name="Mock">
            <mock:with-attributes>
                <mock:with-attribute whereValue="#['Set Original Payload']" name="doc:name"/>
            </mock:with-attributes>
            <mock:then-return payload="#[]"/>
        </mock:when>
        <mock:when messageProcessor="mule:flow" doc:name="Mock">
            <mock:with-attributes>
                <mock:with-attribute whereValue="#['exampleFlow2']" name="name"/>
            </mock:with-attributes>
            <mock:then-return payload="#[]">
                <mock:invocation-properties>
                    <mock:invocation-property key="my_variable" value="#['var_value_1']"/>
                </mock:invocation-properties>
            </mock:then-return>
        </mock:when>
        <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
        <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_1']" doc:name="Assert Payload"/>
    </munit:test>

    <munit:test name="doc-test-exampleFlow-unit-Test_2" description="Unit Test case asserting scenario 2">
        <mock:when messageProcessor="mule:set-payload" doc:name="Mock">
            <mock:with-attributes>
                <mock:with-attribute whereValue="#['Set Original Payload']" name="doc:name"/>
            </mock:with-attributes>
            <mock:then-return payload="#[]"/>
        </mock:when>
        <mock:when messageProcessor="mule:flow" doc:name="Mock">
            <mock:with-attributes>
                <mock:with-attribute whereValue="#['exampleFlow2']" name="name"/>
            </mock:with-attributes>
            <mock:then-return payload="#[]">
                <mock:invocation-properties>
                    <mock:invocation-property key="my_variable" value="#['var_value_2']"/>
                </mock:invocation-properties>
            </mock:then-return>
        </mock:when>
        <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
        <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_2']" doc:name="Assert Payload"/>
    </munit:test>

    <!-- exampleFlow Functional Tests -->
    <munit:test name="doc-test-exampleFlow-functionalTest_1" description="Funtional Test case asserting scenario 1">
        <munit:set payload="#['']" doc:name="Set Message url_key:payload_1">
            <munit:inbound-properties>
                <munit:inbound-property key="http.query.params" value="#[['url_key':'payload_1']]"/>
            </munit:inbound-properties>
        </munit:set>
        <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
        <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_1']" doc:name="Assert Payload"/>
    </munit:test>

    <munit:test name="doc-test-exampleFlow-functionalTest_2" description="Funtional Test case asserting scenario 2">
        <munit:set payload="#['']" doc:name="Set Message url_key:payload_1">
            <munit:inbound-properties>
                <munit:inbound-property key="http.query.params" value="#[['url_key':'payload_2']]"/>
            </munit:inbound-properties>
        </munit:set>
        <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
        <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_2']" doc:name="Assert Payload"/>
    </munit:test>

</mule>
```

In the sections below we'll break down and analyze the Test Suite file. When performing unit tests, it's always better to take a ground-up approach, first testing the building blocks of the code.

**TIP**: Always test the building blocks of your code first, then test the more complex code.

You can compare this to setting the pillars and ensuring that they'll hold, before building the rest of the bridge.

We'll start by testing `exampleFlow2`.

Ideally, you should test each and every flow and sub-flow in your application, in order to validate that each one of them behaves as expected. Since we've complicated things a little in order to show you more scenarios, we'll skip testing the sub-flows `exampleSub_Flow1` and `exampleSub_Flow2`). In a real application, we should start by testing those two flows.

**TIP**: Ideally, you should test each and every flow and sub-flow in your application.

### MUnit Test Suit "Musts"

Each MUnit test file _must_ contain the following beans:

* `MUnit config`
* The _import section_

These are shown in the snippet below:

*MUnit Musts*

``` xml
<munit:config name="munit" doc:name="Munit configuration"/>

<spring:beans>
    <spring:import resource="classpath:doc.xml"/>
</spring:beans>
```

In the _import section_, we define the files needed for this test to run. This section usually includes the file containing the flows we want to test, and additional files required for the first file to work.

**WARNING**: MUnit Test Suite files will not run without MUnit config.

### Testing: `exampleFlow2`

We'll start by analyzing the simplest flow in the application, `exampleFlow2`.

This flow contains a `choice` router, which provides two different paths that the code can follow. Here we will test both of them.

**NOTE**: In a real application, always test all possible paths.

*exampleFlow2*

``` xml
<flow name="exampleFlow2">
  <choice doc:name="Choice">
    <when expression="#['payload_1'.equals(payload)]">
      <flow-ref name="exampleSub_Flow1" doc:name="exampleSub_Flow1"/>
    </when>
    <otherwise>
      <flow-ref name="exampleSub_Flow2" doc:name="exampleSub_Flow2"/>
    </otherwise>
  </choice>
</flow>
```

We'll start with the first path.

*exampleFlow2 - First test case*

``` xml
<munit:test name="doc-test-exampleFlow2Test1" description="Validate calls to sub flows are being done properly ">
  <munit:set payload="#['payload_1']" doc:name="Set Message payload == payload_1"/>                         //1

  <flow-ref name="exampleFlow2" doc:name="Flow-ref to exampleFlow2"/>                                           //2

  <mock:verify-call messageProcessor="mule:sub-flow" doc:name="Verify Call" times="1">    //3
    <mock:with-attributes>
      <mock:with-attribute whereValue="#[matchContains('exampleSub_Flow1')]" name="name"/>
    </mock:with-attributes>
  </mock:verify-call>
</munit:test>
```

1. Define input message to be sent to the production flow `exampleFlow2`.
2. Make call to production code.
3. Validate success of the test by using a verification.

This test looks fairly simple, but it has a few points to highlight.

The first thing we do is to create an input message. This is a very common scenario; you will probably have to create input messages for the flows that you'll test. In this example it was only necessary to define a payload, but further down in this tutorial we'll see how to create more complex messages.

For the purposes of this test, we can be confident that the code works properly by simply ensuring that the correct message processor was called. We could also have added an assertion over the variables that were supposed to be set.

[[flow-ref]]
Finally, notice that the message processor to call is a `flow-ref`. In MUnit, you don't mock or verify `flow-ref`, but the flow or sub-flow that would be invoked by `flow-ref`. If you check closely, you'll see that we are not verifying the `flow-ref` message processor, but running a verification over the `mule:sub-flow` message processor.

**WARNING**: In MUnit you don't mock or verify `flow-ref`, you mock or verify the `flow` and `sub-flow`.

**TIP**: Using `flow-ref` is the most common way to trigger your production code. Even if the
flow you're testing is a not a private flow, the usual way to invoke it is by using
`flow-ref`, rather than calling the flow's inbound endpoints such as HTTP, VM, JSM, etc.

Another thing to notice is how we are defining the name of the sub-flow. Instead
of just typing the name of the sub-flow, we are using the MUnit matcher `matchContains`:

``` xml
#[matchContains('exampleSub_Flow1')]
```

This is not needed when verifying or mocking flows, only for sub-flows.

**NOTE**: When mocking or verifying a sub-flow and using the `name` attribute, always use
the MUnit matcher `matchContains`.

So far we have only tested one branch of `exampleFlow2`; we need to test the other one. To do that, we will add another test.

*exampleFlow2 - Second test case*

``` xml
<munit:test name="doc-test-exampleFlow2Test2" description="Validate calls to sub flows are being done properly ">
  <munit:set payload="#['payload_2']" doc:name="Set Message payload == payload_2"/>

  <flow-ref name="exampleFlow2" doc:name="Flow-ref to exampleFlow2"/>

  <mock:verify-call messageProcessor="mule:sub-flow" doc:name="Verify Call" times="1">
    <mock:with-attributes>
      <mock:with-attribute whereValue="#[matchContains('exampleSub_Flow2')]" name="name"/>
    </mock:with-attributes>
  </mock:verify-call>
</munit:test>
```

As you can see, this test is very similar to the first, except for one crucial change:

``` xml
<munit:set payload="#['payload_2']" doc:name="Set Message payload == payload_2"/>
```

When we define the message to send to the production code, we are changing the payload in order to engage the other branch of the code. This may look obvious to experienced developers, but it is a common mistake.

TIP: If your production code takes different actions based on different values of the payload or on the contents of a variable, you should probably design more that one test for that production flow.

### Testing: exampleFlow

The most complex flow in this application is the last flow, `exampleFlow`.

This flow contains a `choice` router, which provides two different paths that the code can follow. As in the previous case, we will test both of them.

*exampleFlow*

``` xml
<flow name="exampleFlow">
  <http:listener config-ref="HTTP_Listener_Configuration" path="/" allowedMethods="GET" doc:name="HTTP"/>
  <set-payload value="#[message.inboundProperties['http.query.params']['url_key']]" doc:name="Set Original Payload"/>

  <flow-ref name="exampleFlow2" doc:name="exampleFlow2"/>

  <choice doc:name="Choice">
    <when expression="#[flowVars['my_variable'].equals('var_value_1')]">
      <set-payload value="#['response_payload_1']" doc:name="Set Response Payload"/>
    </when>
    <otherwise>
      <set-payload value="#['response_payload_2']" doc:name="Set Response Payload"/>
    </otherwise>
    </choice>
</flow>
```

This flow contains an `http-listener`, but in order to show you different scenarios we are not going to call it. Since we are not calling the HTTP listener, we need to take a few other actions for this test to work properly.

As with our first flow, here we'll start with the first path contained in the flow.

*exampleFlow - First test case*

``` xml
<munit:test name="doc-test-exampleFlow-unit-Test_1" description="Unit Test case asserting scenario 1">

  <mock:when messageProcessor="mule:set-payload" doc:name="Mock">             //1
    <mock:with-attributes>
      <mock:with-attribute whereValue="#['Set Original Payload']" name="doc:name"/>
    </mock:with-attributes>
    <mock:then-return payload="#[]"/>
  </mock:when>

  <mock:when messageProcessor="mule:flow" doc:name="Mock">                    //2
    <mock:with-attributes>
      <mock:with-attribute whereValue="#['exampleFlow2']" name="name"/>
      </mock:with-attributes>
    <mock:then-return payload="#[]">
      <mock:invocation-properties>
        <mock:invocation-property key="my_variable" value="#['var_value_1']"/>
      </mock:invocation-properties>
    </mock:then-return>
  </mock:when>

  <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>                                //3

  <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_1']" doc:name="Assert Payload"/> //4
</munit:test>
```

1. Define mock for the set-payload message processor in `exampleFlow`.
2. Define mock for the call to `exampleFlow2`.
3. Make call to production code.
4. Validate success of the test by asserting the returned payload.

The first thing to notice in this test is that we are defining _mocks_. Mocks are what allow you to isolate your flow, distinguishing it from third-party systems and any other flows in your application.

The first mock we define is for the `set-payload` message processor. We do this because this message processor expects a certain set of inbound variables, but we won't send them in this test -- hence, for the code to succeed we need to mock the behavior of the `set-payload` message processor.

*Mock set-payload*

``` xml
<mock:when messageProcessor="mule:set-payload" doc:name="Mock">
  <mock:with-attributes>
    <mock:with-attribute whereValue="#['Set Original Payload']" name="doc:name"/>
  </mock:with-attributes>
  <mock:then-return payload="#[]"/>
</mock:when>
```

Notice that we are not actually returning a payload. The payload in the `set-payload` message processor is needed by `exampleFlow2`. In this unit test, we'll trust `exampleFlow2` to work as expected, and will mock it as well.

**TIP**: When doing unit tests, you isolate your flow from third-party systems and other flows and trust they will work as expected. In turn, you must test each third-party system or flow with its own, specific test.


*Mock exampleFlow2*

``` xml
<mock:when messageProcessor="mule:flow" doc:name="Mock">
  <mock:with-attributes>
    <mock:with-attribute whereValue="#['exampleFlow2']" name="name"/>
    </mock:with-attributes>
  <mock:then-return payload="#[]">
    <mock:invocation-properties>
      <mock:invocation-property key="my_variable" value="#['var_value_1']"/>
    </mock:invocation-properties>
  </mock:then-return>
</mock:when>
```

If you've been reading this tutorial from the beginning, you already know that in MUnit you do not mock `flow-ref` message processors, you mock the flows that would be called by them (see <<flow-ref,above>>). That's what we're doing here, mocking `exampleFlow2` which was called from `exampleFlow`.

The purpose of `exampleFlow2` was to set the value of the invocation variable `my_var`. If you look closely at this mock, you'll see that we are telling the mocked flow to return a message that contains an invocation variable named `my_var` with a value of `var_value_1`. This is what should happen in the first test scenario.

Now that our two mocks are in place, we'll run the production code:

``` xml
<flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
```

The only thing that remains to be done for this test is to define its success criteria. For the purposes of this example, we'll determine whether it was successful based on the payload returned by the flow.

``` xml
<munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_1']" doc:name="Assert Payload"/> //4
```

As you can see, we are validating that the payload returned is equal to that set by the first branch of the choice in the production code, i.e. `response_payload_1`.

Now we'll test the other branch.

*exampleFlow - Second test case*

``` xml
<munit:test name="doc-test-exampleFlow-unit-Test_2" description="Unit Test case asserting scenario 2">
    <mock:when messageProcessor="mule:set-payload" doc:name="Mock">
        <mock:with-attributes>
            <mock:with-attribute whereValue="#['Set Original Payload']" name="doc:name"/>
        </mock:with-attributes>
        <mock:then-return payload="#[]"/>
    </mock:when>

    <mock:when messageProcessor="mule:flow" doc:name="Mock">
        <mock:with-attributes>
            <mock:with-attribute whereValue="#['exampleFlow2']" name="name"/>
        </mock:with-attributes>
        <mock:then-return payload="#[]">
            <mock:invocation-properties>
                <mock:invocation-property key="my_variable" value="#['var_value_2']"/>                                //1
            </mock:invocation-properties>
        </mock:then-return>
    </mock:when>

    <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
    <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_2']" doc:name="Assert Payload"/> //2
</munit:test>
```

1. First difference with first branch.
2. Second difference with first branch.

This test looks very similar, but as you can see there are two crucial differences, explained below.

First difference:

``` xml
<mock:invocation-property key="my_variable" value="#['var_value_2']"/>
```

When mocking `exampleFlow2`, we're telling it to return a variable with a different value: `var_value_2`. This should trigger the second branch of the choice.

Second difference:

``` xml
<munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_2']" doc:name="Assert Payload"/>
```

We are also changing the assertion, because the mock before the returned payload has changed. Hence the need to modify our success criteria.

### Functional Testing

All of the tests explained so far were unit tests, which try to isolate each flow as much as possible from the other flows.

You may also want to do a _functional test_, i.e. an end-to-end test. In our example, this means that we are not going to mock any message processor. To implement a test in this way, we need to correctly define the message that we'll send to the production code.

In previous tests, we mocked the first message processor of `exampleFlow` because it needed the message to contain specific values. Since we are not mocking anything now, we will have to create that message.

*exampleFlow - Functional test*

``` xml
<munit:test name="doc-test-exampleFlow-functionalTest_1" description="Funtional Test case asserting scenario 1">
    <munit:set payload="#['']" doc:name="Set Message url_key:payload_1">
        <munit:inbound-properties>
            <munit:inbound-property key="http.query.params" value="#[['url_key':'payload_1']]"/>
        </munit:inbound-properties>
    </munit:set>
    <flow-ref name="exampleFlow" doc:name="Flow-ref to exampleFlow"/>
    <munit:assert-payload-equals message="oops, wrong payload!" expectedValue="#['response_payload_1']" doc:name="Assert Payload"/>
</munit:test>
```

This test is very similar to the others for `exampleFlow`, without the mocks.

Let's check again the implementation of `exampleFlow`, specifically the `set-payload`:

``` xml
<set-payload value="#[message.inboundProperties['http.query.params']['url_key']]" doc:name="Set Original Payload"/>
```

The `set-payload` message processor is expecting the message to have a inbound property named `http.query.params`, which should be a map. The map should contain the key `url_key`.

The code below shows how to create such a message:

``` xml
<munit:set payload="#['']" doc:name="Set Message url_key:payload_1">
    <munit:inbound-properties>
        <munit:inbound-property key="http.query.params" value="#[['url_key':'payload_1']]"/>
    </munit:inbound-properties>
</munit:set>
```

## Conclusion

In this tutorial, we've seen:

* How to create MUnit tests
* How to create Mule messages
* How to create mocks
* How to run verifications and assertions

In short, we've covered a great deal of the MUnit features.

As you code, your tests may become as large and complex as your production code. The tools provided by MUnit will help you create great tests while maintaining the quality of your code.