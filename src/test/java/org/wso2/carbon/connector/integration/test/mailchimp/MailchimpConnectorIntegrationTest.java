/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* WSO2 Inc. licenses this file to you under the Apache License,
* Version 2.0 (the "License"); you may not use this file except
* in compliance with the License.
* You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.connector.integration.test.mailchimp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

public class MailchimpConnectorIntegrationTest extends ConnectorIntegrationTestBase {

	private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();

	private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

	private String apiBaseUrl;

	/**
	 * Set up the environment.
	 */
	@BeforeClass(alwaysRun = true)
	public void setEnvironment() throws Exception {

		init("mailchimp-connector-2.0.0");
		esbRequestHeadersMap.put("Content-Type", "application/json");
		apiRequestHeadersMap.putAll(esbRequestHeadersMap);
		apiRequestHeadersMap.put("Authorization", "OAuth " + connectorProperties.getProperty("accessToken"));
	}

	/**
	 * Positive test case for createList method with mandatory parameters.
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {createList} integration test with mandatory parameters.")
	public void testCreateListWithMandatoryParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:createList");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createList_mandatory.json");
		final String listId = esbRestResponse.getBody().getString("id");
		connectorProperties.setProperty("listId", listId);

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/lists/" + connectorProperties.getProperty("listId");
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(connectorProperties.getProperty("company"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("company"));
		Assert.assertEquals(connectorProperties.getProperty("address1"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("address1"));
		Assert.assertEquals(connectorProperties.getProperty("city"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("city"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for createList method with optional parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, description = "mailchimp {createList} integration test with optional parameters.")
	public void testCreateListWithOptionalParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:createList");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createList_optional.json");
		final String listIdOpt = esbRestResponse.getBody().getString("id");
		connectorProperties.setProperty("listIdOpt", listIdOpt);

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/lists/" + connectorProperties.getProperty("listIdOpt");
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(connectorProperties.getProperty("company"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("company"));
		Assert.assertEquals(connectorProperties.getProperty("address1"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("address1"));
		Assert.assertEquals(connectorProperties.getProperty("city"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("city"));
		Assert.assertEquals(connectorProperties.getProperty("phone"),
		                    apiRestResponse.getBody().getJSONObject("contact").getString("phone"));
		Assert.assertEquals(connectorProperties.getProperty("notifyOnSubscribe"),
		                    apiRestResponse.getBody().getString("notify_on_subscribe"));
		Assert.assertEquals(connectorProperties.getProperty("visibility"),
		                    apiRestResponse.getBody().getString("visibility"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	    /**
	     * Negative test case for createList method.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(groups = { "wso2.esb" }, description = "mailchimp {createList} integration test negative case.")
	    public void testCreateListWithNegativeCase() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:createList");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createList_negative.json");

	        Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("field"),
	                            "name");
	        Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("message"),
	                            "Please enter a value");
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
	    }

	    /**
	     * Positive test case for listLists method with mandatory parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, description = "mailchimp {listLists} integration test with mandatory parameters.")
	    public void testListListsWithMandatoryParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:listLists");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listLists_mandatory.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for listLists method with optional parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, description = "mailchimp {listLists} integration test with optional parameters.")
	    public void testListListsWithOptionalParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:listLists");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listLists_optional.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists?fields=lists.id";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for listListAbuseReports method with mandatory parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters"},
	            description = "mailchimp {listListAbuseReports} integration test with mandatory parameters.")
	    public void testListListAbuseReportsWithMandatoryParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:listListAbuseReports");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listListAbuseReports_mandatory.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId") + "/abuse-reports";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for listListAbuseReports method with optional parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters",
	                                              "testListListAbuseReportsWithMandatoryParameters"},
	            description = "mailchimp {listListAbuseReports} integration test with optional parameters.")
	    public void testListListAbuseReportsWithOptionalParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:listListAbuseReports");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listListAbuseReports_optional.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId") + "/abuse-reports?fields=id,name";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for getList method with mandatory parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters",
	                                              "testListListAbuseReportsWithOptionalParameters"},
	            description = "mailchimp {getList} integration test with mandatory parameters.")
	    public void testGetListWithMandatoryParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:getList");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getList_mandatory.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId") + "?fields=id,name";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for getList method with optional parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters",
	                                              "testGetListWithMandatoryParameters"},
	            description = "mailchimp {getList} integration test with optional parameters.")
	    public void testGetListWithOptionalParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:getList");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getList_optional.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId") + "?fields=id,name";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Negative test case for getList method.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(groups = { "wso2.esb" }, description = "mailchimp {getList} integration test negative case.")
	    public void testGetListWithNegativeCase() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:getList");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getList_negative.json");


	        Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be" +
	                                                                           " found.");
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	    }

	    /**
	     * Positive test case for getListActivity method with mandatory parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters"},
	            description = "mailchimp {getListActivity} integration test with mandatory parameters.")
	    public void testGetListActivityWithMandatoryParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:getListActivity");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getListActivity_mandatory.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId") + "/activity";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for getListActivity method with optional parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters",
	                                              "testGetListActivityWithMandatoryParameters"},
	            description = "mailchimp {getListActivity} integration test with optional parameters.")
	    public void testGetListActivityWithOptionalParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:getListActivity");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getListActivity_optional.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId") + "/activity?fields=activity.day";
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Negative test case for getListActivity method.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(groups = { "wso2.esb" }, description = "mailchimp {getListActivity} integration test negative case.")
	    public void testGetListActivityWithNegativeCase() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:getListActivity");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getListActivity_negative.json");


	        Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be " +
	                                                                           "found.");
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	    }

	    /**
	     * Positive test case for updateList method with mandatory parameters.
	     */
	    @Test(groups = {"wso2.esb"},priority = 1, dependsOnMethods = {"testCreateListWithMandatoryParameters"},
	            description = "mailchimp {updateList} integration test with mandatory parameters.")
	    public void testUpdateListWithMandatoryParameters() throws IOException, JSONException {

	        esbRequestHeadersMap.put("Action", "urn:updateList");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateList_mandatory.json");
	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listId");
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(connectorProperties.getProperty("newCompany"),
	                            apiRestResponse.getBody().getJSONObject("contact").getString("company"));
	        Assert.assertEquals(connectorProperties.getProperty("newFromName"),
	                            apiRestResponse.getBody().getJSONObject("campaign_defaults").getString("from_name"));
	        Assert.assertEquals(connectorProperties.getProperty("newFromEmail"),
	                            apiRestResponse.getBody().getJSONObject("campaign_defaults").getString("from_email"));
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Positive test case for updateList method with optional parameters.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(enabled = true,dependsOnMethods = {"testCreateListWithOptionalParameters"},
	            description = "mailchimp {updateList} integration test with optional parameters.")
	    public void testUpdateListWithOptionalParameters() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:updateList");

	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateList_optional.json");

	        String apiEndPoint =
	                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
	                + "/lists/" + connectorProperties.getProperty("listIdOpt");
	        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

	        Assert.assertEquals(connectorProperties.getProperty("newNameOpt"),
	                            apiRestResponse.getBody().getString("name"));
	        Assert.assertEquals(connectorProperties.getProperty("company"),
	                            apiRestResponse.getBody().getJSONObject("contact").getString("company"));
	        Assert.assertEquals(connectorProperties.getProperty("address1"),
	                            apiRestResponse.getBody().getJSONObject("contact").getString("address1"));
	        Assert.assertEquals(connectorProperties.getProperty("city"),
	                            apiRestResponse.getBody().getJSONObject("contact").getString("city"));
	        Assert.assertEquals(connectorProperties.getProperty("newPhoneOpt"),
	                            apiRestResponse.getBody().getJSONObject("contact").getString("phone"));
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Negative test case for updateList method.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(groups = { "wso2.esb" }, description = "mailchimp {updateList} integration test negative case.")
	    public void testUpdateListWithNegativeCase() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:updateList");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateList_negative.json");

	        Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("field"),
	                            "name");
	        Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("message"),
	                            "Please enter a value");
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
	    }

	    /**
	     * Positive test case for subscribeOrUnsubscribeListMembers method with mandatory parameters.
	     */
	    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateListWithMandatoryParameters"},
	            description = "mailchimp {subscribeOrUnsubscribeListMembers} integration test with mandatory parameters.")
	    public void testSubscribeOrUnsubscribeListMembersWithMandatoryParameters() throws IOException, JSONException {

	        esbRequestHeadersMap.put("Action", "urn:subscribeOrUnsubscribeListMembers");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
	                                    "subscribeOrUnsubscribeListMembers_mandatory.json");

	        Assert.assertTrue(esbRestResponse.getBody().toString().contains("new_members"));
	        Assert.assertTrue(esbRestResponse.getBody().toString().contains("updated_members"));
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	    }

	    /**
	     * Negative test case for subscribeOrUnsubscribeListMembers method.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(groups = { "wso2.esb" }, description = "mailchimp {subscribeOrUnsubscribeListMembers} integration test " +
	                                                 "negative case.")
	    public void testSubscribeOrUnsubscribeListMembersWithNegativeCase() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:subscribeOrUnsubscribeListMembers");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
	                                    "subscribeOrUnsubscribeListMembers_negative.json");

	        Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("message"),
	                            "Required fields were not provided: email_address");
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
	    }

	    /**
	     * Positive test case for deleteList method with mandatory parameters.
	     */
	    @Test(groups = {"wso2.esb"}, priority = 3,
	            description = "mailchimp {deleteList} integration test with mandatory parameters.")
	    public void testDeleteListWithMandatoryParameters() throws IOException, JSONException {

	        esbRequestHeadersMap.put("Action", "urn:deleteList");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "deleteList_mandatory.json");

	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
	    }

	    /**
	     * Negative test case for deleteList method.
	     *
	     * @throws JSONException
	     * @throws IOException
	     */
	    @Test(groups = { "wso2.esb" }, description = "mailchimp {deleteList} integration test negative case.")
	    public void testDeleteListWithNegativeCase() throws IOException, JSONException {
	        esbRequestHeadersMap.put("Action", "urn:deleteList");
	        RestResponse<JSONObject> esbRestResponse =
	                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "deleteList_negative.json");

	        Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
	        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	    }

	/**
	 * Positive test case for createCampaign method with mandatory parameters.
	 */
	@Test(groups = { "wso2.esb" }, dependsOnMethods = { "testCreateListWithMandatoryParameters" },
			description = "mailchimp {createCampaign} integration test with mandatory parameters.")
	public void testCreateCampaignWithMandatoryParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:createCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createCampaign_mandatory.json");
		final String campaignId = esbRestResponse.getBody().getString("id");
		connectorProperties.setProperty("campaignId", campaignId);

		Assert.assertEquals(connectorProperties.getProperty("type"), esbRestResponse.getBody().getString("type"));
		Assert.assertEquals(connectorProperties.getProperty("subjectLine"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("subject_line"));
		Assert.assertEquals(connectorProperties.getProperty("campaignFromName"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("from_name"));
		Assert.assertEquals(connectorProperties.getProperty("replyTo"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("reply_to"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for createCampaign method with optional parameters.
	 */
	@Test(groups = { "wso2.esb" }, dependsOnMethods = { "testCreateListWithMandatoryParameters",
	                                                    "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {createCampaign} integration test with mandatory parameters.")
	public void testCreateCampaignWithOptionalParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:createCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createCampaign_optional.json");
		final String campaignIdOpt = esbRestResponse.getBody().getString("id");
		connectorProperties.setProperty("campaignIdOpt", campaignIdOpt);

		Assert.assertEquals(connectorProperties.getProperty("type"), esbRestResponse.getBody().getString("type"));
		Assert.assertEquals(connectorProperties.getProperty("subjectLineOpt"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("subject_line"));
		Assert.assertEquals(connectorProperties.getProperty("campaignFromName"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("from_name"));
		Assert.assertEquals(connectorProperties.getProperty("replyTo"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("reply_to"));
		Assert.assertTrue(true, esbRestResponse.getBody().getJSONObject("settings").getString("auto_footer"));
		Assert.assertTrue(true, esbRestResponse.getBody().getJSONObject("tracking").getString("opens"));
		Assert.assertEquals("John", esbRestResponse.getBody().getJSONObject("settings").getString("to_name"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for createCampaign method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {createCampaign} integration test negative case.")
	public void testCreateCampaignWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:createCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createCampaign_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("message"),
		                    "Invalid list id.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
	}

	/**
	 * Positive test case for getCampaign method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {getCampaign} integration test with mandatory parameters.")
	public void testGetCampaignWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaign");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaign_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId");
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for getCampaign method with optional parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters",
	                                           "testGetCampaignWithMandatoryParameters" },
			description = "mailchimp {getCampaign} integration test with optional parameters.")
	public void testGetCampaignWithOptionalParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaign");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaign_optional.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "?fields=id,type";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getCampaign method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {getCampaign} integration test negative case.")
	public void testGetCampaignWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaign_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("title"), "Resource Not Found");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for listCampaigns method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testGetCampaignWithOptionalParameters" },
			description = "mailchimp {listCampaigns} integration test with mandatory parameters.")
	public void testListCampaignsWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaigns");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaigns_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for listCampaigns method with optional parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testGetCampaignWithOptionalParameters" },
			description = "mailchimp {listCampaigns} integration test with optional parameters.")
	public void testListCampaignsWithOptionalParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaigns");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaigns_optional.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns?fields=campaigns.id&status=save";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for addCampaignFeedback method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {addCampaignFeedback} integration test with mandatory parameters.")
	public void testAddCampaignFeedbackWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:addCampaignFeedback");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "addCampaignFeedback_mandatory.json");
		final String feedbackId = esbRestResponse.getBody().getString("feedback_id");
		connectorProperties.setProperty("feedbackId", feedbackId);

		Assert.assertEquals(connectorProperties.getProperty("message"), esbRestResponse.getBody().getString("message"));
		Assert.assertEquals(connectorProperties.getProperty("campaignId"),
		                    esbRestResponse.getBody().getString("campaign_id"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for addCampaignFeedback method with optional parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {addCampaignFeedback} integration test with optional parameters.")
	public void testAddCampaignFeedbackWithOptionalParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:addCampaignFeedback");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "addCampaignFeedback_optional.json");
		final String feedbackIdOpt = esbRestResponse.getBody().getString("feedback_id");
		connectorProperties.setProperty("feedbackIdOpt", feedbackIdOpt);

		Assert.assertEquals(connectorProperties.getProperty("messageOpt"),
		                    esbRestResponse.getBody().getString("message"));
		Assert.assertEquals(connectorProperties.getProperty("campaignId"),
		                    esbRestResponse.getBody().getString("campaign_id"));
		Assert.assertTrue(true, esbRestResponse.getBody().getString("is_complete"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for addCampaignFeedback method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {addCampaignFeedback} integration test negative case.")
	public void testAddCampaignFeedbackWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:addCampaignFeedback");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "addCampaignFeedback_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("field"),
		                    "message");
		Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("message"),
		                    "Please enter a value");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
	}

	/**
	 * Positive test case for getCampaignFeedback method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testAddCampaignFeedbackWithMandatoryParameters" },
			description = "mailchimp {getCampaignFeedback} integration test with mandatory parameters.")
	public void testGetCampaignFeedbackWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaignFeedback");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaignFeedback_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "/feedback/" +
				connectorProperties.getProperty("feedbackId");
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for getCampaignFeedback method with optional parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testAddCampaignFeedbackWithMandatoryParameters" },
			description = "mailchimp {getCampaignFeedback} integration test with optional parameters.")
	public void testGetCampaignFeedbackWithOptionalParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaignFeedback");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaignFeedback_optional.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "/feedback/" +
				connectorProperties.getProperty("feedbackId") + "?fields=message";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getCampaignFeedback method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {addCampaignFeedback} integration test negative case.")
	public void testGetCampaignFeedbackWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaignFeedback");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaignFeedback_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("title"), "Resource Not Found");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for listCampaignFeedback method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testAddCampaignFeedbackWithMandatoryParameters" },
			description = "mailchimp {listCampaignFeedback} integration test with mandatory parameters.")
	public void testListCampaignFeedbackWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaignFeedback");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaignFeedback_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "/feedback";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for listCampaignFeedback method with optional parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testAddCampaignFeedbackWithMandatoryParameters" },
			description = "mailchimp {listCampaignFeedback} integration test with optional parameters.")
	public void testListCampaignFeedbackWithOptionalParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaignFeedback");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaignFeedback_optional.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "/feedback?fields=feedback.feedback_id";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getCampaignFeedback method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {listCampaignFeedback} integration test negative case.")
	public void testListCampaignFeedbackWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaignFeedback");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaignFeedback_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("title"), "Resource Not Found");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for getCampaignContent method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {getCampaignContent} integration test with mandatory parameters.")
	public void testGetCampaignContentWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaignContent");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaignContent_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "/content";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getCampaignContent method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {getCampaignContent} integration test negative case.")
	public void testGetCampaignContentWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getCampaignContent");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getCampaignContent_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("title"), "Resource Not Found");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for getSendCheckList method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {getSendCheckList} integration test with mandatory parameters.")
	public void testGetSendCheckListWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getSendCheckList");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getSendCheckList_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/campaigns/" + connectorProperties.getProperty("campaignId") + "/send-checklist";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getSendCheckList method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {getSendCheckList} integration test negative case.")
	public void testGetSendCheckListWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getSendCheckList");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getSendCheckList_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("title"), "Resource Not Found");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for searchCampaign method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters" },
			description = "mailchimp {searchCampaign} integration test with mandatory parameters.")
	public void testSearchCampaignWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:searchCampaign");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "searchCampaign_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") +
				"/search-campaigns?query=" + connectorProperties.getProperty("subjectLine");
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for searchCampaign method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {searchCampaign} integration test negative case.")
	public void testSearchCampaignWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:searchCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "searchCampaign_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"),
		                    "Please provide 'query' parameter in the query string to search");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
	}

	/**
	 * Positive test case for updateCampaign method with mandatory parameters.
	 */
	@Test(groups = { "wso2.esb" }, priority = 2, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters",
	                                                                  "testCreateListWithMandatoryParameters" },
			description = "mailchimp {updateCampaign} integration test with mandatory parameters.")
	public void testUpdateCampaignWithMandatoryParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:updateCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateCampaign_mandatory.json");

		Assert.assertEquals(connectorProperties.getProperty("campaignId"), esbRestResponse.getBody().getString("id"));
		Assert.assertEquals(connectorProperties.getProperty("newSubjectLine"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("subject_line"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for updateCampaign method with optional parameters.
	 */
	@Test(groups = { "wso2.esb" }, priority = 2, dependsOnMethods = { "testCreateCampaignWithOptionalParameters",
	                                                                  "testCreateListWithOptionalParameters" },
			description = "mailchimp {updateCampaign} integration test with optional parameters.")
	public void testUpdateCampaignWithOptionalParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:updateCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateCampaign_optional.json");

		Assert.assertEquals(connectorProperties.getProperty("campaignIdOpt"), esbRestResponse.getBody().getString("id"));
		Assert.assertEquals(connectorProperties.getProperty("listIdOpt"),
		                    esbRestResponse.getBody().getJSONObject("recipients").getString("list_id"));
		Assert.assertEquals(connectorProperties.getProperty("newSubjectLineOpt"),
		                    esbRestResponse.getBody().getJSONObject("settings").getString("subject_line"));
		Assert.assertEquals("Miller", esbRestResponse.getBody().getJSONObject("settings").getString("to_name"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for updateCampaign method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {updateCampaign} integration test negative case.")
	public void testUpdateCampaignWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:updateCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateCampaign_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"),
		                    "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for updateCampaignFeedback method with mandatory parameters.
	 */
	@Test(groups = { "wso2.esb" }, priority = 2, dependsOnMethods = { "testCreateCampaignWithMandatoryParameters",
	                                                                  "testAddCampaignFeedbackWithMandatoryParameters" },
			description = "mailchimp {updateCampaignFeedback} integration test with mandatory parameters.")
	public void testUpdateCampaignFeedbackWithMandatoryParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:updateCampaignFeedback");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateCampaignFeedback_mandatory.json");

		Assert.assertEquals(connectorProperties.getProperty("newMessage"),
		                    esbRestResponse.getBody().getString("message"));
		Assert.assertEquals(connectorProperties.getProperty("campaignId"),
		                    esbRestResponse.getBody().getString("campaign_id"));
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for updateCampaignFeedback method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {updateCampaignFeedback} integration test negative case.")
	public void testUpdateCampaignFeedbackWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:updateCampaignFeedback");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "updateCampaignFeedback_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"),
		                    "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for deleteCampaignFeedback method with mandatory parameters.
	 */
	@Test(groups = { "wso2.esb" }, priority = 3, dependsOnMethods = { "testCreateCampaignWithOptionalParameters",
	                                                                  "testAddCampaignFeedbackWithOptionalParameters" },
			description = "mailchimp {deleteCampaignFeedback} integration test with mandatory parameters.")
	public void testDeleteCampaignFeedbackWithMandatoryParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:deleteCampaignFeedback");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "deleteCampaignFeedback_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
	}

	/**
	 * Negative test case for deleteCampaignFeedback method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {deleteCampaignFeedback} integration test negative case.")
	public void testDeleteCampaignFeedbackWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "deleteCampaignFeedback_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"),
		                    "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for deleteCampaign method with mandatory parameters.
	 */
	@Test(groups = { "wso2.esb" }, priority = 4, description = "mailchimp {deleteCampaign} integration test with " +
	                                                           "mandatory parameters.")
	public void testDeleteCampaignWithMandatoryParameters() throws IOException, JSONException {

		esbRequestHeadersMap.put("Action", "urn:deleteCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "deleteCampaign_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
	}

	/**
	 * Negative test case for deleteCampaign method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {deleteCampaign} integration test negative case.")
	public void testDeleteCampaignWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteCampaign");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "deleteCampaign_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for listCampaignAbuseReports method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = {"testCreateCampaignWithMandatoryParameters"},
			description = "mailchimp {listCampaignAbuseReports} integration test with mandatory parameters.")
	public void testListCampaignAbuseReportsWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaignAbuseReports");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaignAbuseReports_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports/" + connectorProperties.getProperty("campaignId") + "/abuse-reports";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for listCampaignAbuseReports method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {listCampaignAbuseReports} integration test negative case.")
	public void testListCampaignAbuseReportsWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listCampaignAbuseReports");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listCampaignAbuseReports_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for listClickReports method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = {"testCreateCampaignWithMandatoryParameters"},
			description = "mailchimp {listClickReports} integration test with mandatory parameters.")
	public void testListClickReportsWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listClickReports");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listClickReports_mandatory.json");
		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports/" + connectorProperties.getProperty("campaignId") + "/click-details";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for listClickReports method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {listClickReports} integration test negative case.")
	public void testListClickReportsWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listClickReports");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listClickReports_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for listReports method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, description = "mailchimp {listReports} integration test with mandatory parameters.")
	public void testListReportsWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listReports");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listReports_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Positive test case for listSentTo method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = {"testCreateCampaignWithMandatoryParameters"},
			description = "mailchimp {listSentTo} integration test with mandatory parameters.")
	public void testListSentToWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listSentTo");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listSentTo_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports/"  + connectorProperties.getProperty("campaignId") + "/sent-to";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for listSentTo method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {listSentTo} integration test negative case.")
	public void testListSentToWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listSentTo");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listSentTo_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for listUnsubscribes method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = {"testCreateCampaignWithMandatoryParameters"},
			description = "mailchimp {listUnsubscribes} integration test with mandatory parameters.")
	public void testListUnsubscribesWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listUnsubscribes");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listUnsubscribes_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports/"  + connectorProperties.getProperty("campaignId") + "/unsubscribed";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for listUnsubscribes method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {listUnsubscribes} integration test negative case.")
	public void testListUnsubscribesWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listUnsubscribes");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "listUnsubscribes_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for getLocation method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = {"testCreateCampaignWithMandatoryParameters"},
			description = "mailchimp {getLocation} integration test with mandatory parameters.")
	public void testGetLocationWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getLocation");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getLocation_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports/"  + connectorProperties.getProperty("campaignId") + "/locations";
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getLocation method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {getLocation} integration test negative case.")
	public void testGetLocationWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:listUnsubscribes");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getLocation_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}

	/**
	 * Positive test case for getReport method with mandatory parameters.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(enabled = true, dependsOnMethods = {"testCreateCampaignWithMandatoryParameters"},
			description = "mailchimp {getReport} integration test with mandatory parameters.")
	public void testGetReportWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getReport");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getReport_mandatory.json");

		String apiEndPoint =
				connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion")
				+ "/reports/"  + connectorProperties.getProperty("campaignId");
		RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
	}

	/**
	 * Negative test case for getReport method.
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test(groups = { "wso2.esb" }, description = "mailchimp {getReport} integration test negative case.")
	public void testGetReportWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getReport");
		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getReport_negative.json");

		Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
	}
}
