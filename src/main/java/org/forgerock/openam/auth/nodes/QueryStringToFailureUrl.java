/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2018 ForgeRock AS.
 */


package org.forgerock.openam.auth.nodes;

import com.google.inject.assistedinject.Assisted;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.shared.debug.Debug;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.*;
import org.forgerock.openam.core.CoreWrapper;

import javax.inject.Inject;

import static org.forgerock.openam.auth.node.api.SharedStateConstants.REALM;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;

/**
 *
 * An authentication node which transfers query string parameters from the original referer header to the FailureURL.
 *
 * The original service parameter is retained and replaced in the FailureURL.
 *
 * @author Keith Daly - ForgeRock
 * @version 1.0.0
 *
 */
@Node.Metadata(outcomeProvider  = AbstractDecisionNode.OutcomeProvider.class,
               configClass      = QueryStringToFailureUrl.Config.class)
public class QueryStringToFailureUrl extends AbstractDecisionNode {

    private final Config config;
    private final CoreWrapper coreWrapper;
    private final static String DEBUG_FILE = "QueryStringToFailureUrl";
    protected Debug debug = Debug.getInstance(DEBUG_FILE);

    //-- Operational --
    AMIdentity userIdentity;

    /**
     * Configuration for the node.
     */
    public interface Config {
        @Attribute(order = 100)
        default String headerVar() {
            return "referer";
        }

        @Attribute(order = 200)
        default String sharedStateVar() {
            return "failureUrl";
        }
    }


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public QueryStringToFailureUrl(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
        String referer = context.request.headers.get(config.headerVar()).get(0).toString();
        String failureUrl = context.sharedState.get(config.sharedStateVar()).toString();
        if (failureUrl.substring(0,1).equals("\"")) failureUrl=failureUrl.substring(1,failureUrl.length()-1);

        String qs = referer.split("\\?")[1];
        qs = qs.replaceAll("service=\\w*&", "");
        qs = qs.replaceAll("&service=\\w*$", "");

        failureUrl += "&" + qs;

        try {
            debug.error("[" + DEBUG_FILE + "]: FAILURE URL : " + failureUrl);
            return goTo(true).replaceSharedState(context.sharedState.copy().put("failureUrl", failureUrl)).build();

        } catch (Exception e) {
            debug.error("[" + DEBUG_FILE + "]: " + "Error : ", e);
        }
        return goTo(false).build();

    }

    /**
     * Set the identity object from the username in the config
     *
     * @param context
     */
    private void setIdentity(TreeContext context) {

        if (context.sharedState.get(USERNAME).asString() != null) {
            userIdentity = coreWrapper.getIdentity(context.sharedState.get(USERNAME).asString(), context.sharedState.get(REALM).asString());
        } else {
            //-- User ID <<DEBUG>> --
            userIdentity = coreWrapper.getIdentity("user.0", context.sharedState.get(REALM).asString());
        }
        debug.message("[" + DEBUG_FILE + "]: setIdentity() : " + userIdentity.toString() );
        debug.message("[" + DEBUG_FILE + "]: setIdentity().username : " + userIdentity.getName());

    }
}