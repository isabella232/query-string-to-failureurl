<!--
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
 * Copyright ${data.get('yyyy')} ForgeRock AS.
-->
# Copy Referer Query String to FailureURL

**DESCRIPTION**

A utility authentication node for [Forgerock's Identity Platform](https://www.forgerock.com/platform/) 6.0 and above.

+ This node transfers query string parameters from the original referer header to the FailureURL.  It is used to transfer from authentication trees to authentication chains during user authentication. This transfer will be required for a resource-first authentication where the goto and forward parameters must be retained.

+ *Note:  The original service parameter is retained and replaced in the FailureURL.*

**BUILD INSTRUCTIONS**
 
 Navigate to the source directory.  Run Maven to build:
 
     $ mvn clean install
 
 *Note: The code in this repository has binary dependencies that live in the ForgeRock maven repository. Maven can be configured to authenticate to this repository by following the following [ForgeRock Knowledge Base Article](https://backstage.forgerock.com/knowledge/kb/article/a74096897).*
 
 **INSTALLATION**
 
 + Copy the .jar file from the ../target directory into the ../web-container/webapps/openam/WEB-INF/lib directory where AM is deployed.
 
 + Restart the web container to pick up the new node.  The node will then appear in the authentication trees components palette.
 
 
 **USAGE**
 
 + Multiple instaces of the node can be inserted between any nodes in a tree to get the current values of context, header, session, cookies, callbacks, locales, and client IP.
 
 + Configuration - Parameters
     + **referer** - The header variable which contains the source query string
     + **failureURL** - The session variable containing the failureURL   

**SCREENSHOTS**

![ScreenShot](./QueryStringToFailureURL.png)

+ After the Choice Collector, the upper path shows a standard username/passworduse within the tree.

+ After the Choice Collector, the lower path shows a redirection to a chain. 
        
**TERMS OF USE**

*The sample code described herein is provided on an "as is" basis, without warranty of any kind, to the fullest extent permitted by law. ForgeRock does not warrant or guarantee the individual success developers may have in implementing the sample code on their development platforms or in production configurations.*

*ForgeRock does not warrant, guarantee or make any representations regarding the use, results of use, accuracy, timeliness or completeness of any data or information relating to the sample code. ForgeRock disclaims all warranties, expressed or implied, and in particular, disclaims all warranties of merchantability, and warranties related to the code, or any service or software related thereto.*

*ForgeRock shall not be liable for any direct, indirect or consequential damages or costs of any type arising out of any action taken by you or others related to the sample code.*
