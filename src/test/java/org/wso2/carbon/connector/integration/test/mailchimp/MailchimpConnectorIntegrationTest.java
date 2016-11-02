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
    @Test(groups = {"wso2.esb"}, description = "mailchimp {createList} integration test with mandatory parameters.")
    public void testCreateListWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:createList");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createList_mandatory.json");
        final String listId = esbRestResponse.getBody().getString("id");
        connectorProperties.setProperty("listId", listId);

        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/lists/"
                + connectorProperties.getProperty("listId");
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
                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/lists/"
                + connectorProperties.getProperty("listIdOpt");
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
     * Negative test case for createInvoice method.
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
    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters","testListListAbuseReportsWithOptionalParameters"},
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


        Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
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
    @Test(enabled = true, dependsOnMethods = {"testCreateListWithMandatoryParameters","testGetListActivityWithMandatoryParameters"},
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


        Assert.assertEquals(esbRestResponse.getBody().getString("detail"), "The requested resource could not be found.");
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
                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/lists/"
                + connectorProperties.getProperty("listId");
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
                connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/lists/"
                + connectorProperties.getProperty("listIdOpt");
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
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "subscribeOrUnsubscribeListMembers_mandatory.json");

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
    @Test(groups = { "wso2.esb" }, description = "mailchimp {subscribeOrUnsubscribeListMembers} integration test negative case.")
    public void testSubscribeOrUnsubscribeListMembersWithNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:subscribeOrUnsubscribeListMembers");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "subscribeOrUnsubscribeListMembers_negative.json");

        Assert.assertEquals(esbRestResponse.getBody().getJSONArray("errors").getJSONObject(0).getString("message"),
                            "Required fields were not provided: email_address");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
    }

    /**
     * Positive test case for deleteList method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, priority = 2,
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

}
