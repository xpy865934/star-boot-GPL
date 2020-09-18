package com.star.starboot.config.flowable;


import com.star.starboot.common.controller.AbstractController;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeployment;
import org.flowable.app.api.repository.AppDeploymentBuilder;
import org.flowable.app.rest.AppRestApiInterceptor;
import org.flowable.app.rest.AppRestResponseFactory;
import org.flowable.app.rest.service.api.repository.AppDeploymentResponse;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 公司信息 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/starflowable")
@CrossOrigin
@Slf4j
public class StarFlowableController extends AbstractController {

    @Autowired(required = false)
    protected AppRestApiInterceptor restApiInterceptor;

    @Autowired(required = false)
    protected AppRestResponseFactory appRestResponseFactory;
    @Autowired
    protected AppRepositoryService appRepositoryService;

    @RequestMapping(value = "/rest/account", method = RequestMethod.GET, produces = "application/json")
    public UserRepresentation getAccount() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("admin");
        userRepresentation.setEmail("admin@flowable.org");
        userRepresentation.setFullName("Test Administrator");
        userRepresentation.setLastName("Administrator");
        userRepresentation.setFirstName("Test");
        List<String> privileges = new ArrayList<>();
        privileges.add(DefaultPrivileges.ACCESS_MODELER);
        privileges.add(DefaultPrivileges.ACCESS_IDM);
        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
        privileges.add(DefaultPrivileges.ACCESS_TASK);
        privileges.add(DefaultPrivileges.ACCESS_REST_API);
        userRepresentation.setPrivileges(privileges);
        return userRepresentation;
    }

    @ApiOperation(
            value = "Create a new app deployment",
            tags = {"App Deployments"},
            consumes = "multipart/form-data",
            produces = "application/json",
            notes = "The request body should contain data of type multipart/form-data. There should be exactly one file in the request, any additional files will be ignored. The deployment name is the name of the file-field passed in. Make sure the file-name ends with .app, .zip or .bar."
    )
    @ApiResponses({@ApiResponse(
            code = 201,
            message = "Indicates the app deployment was created."
    ), @ApiResponse(
            code = 400,
            message = "Indicates there was no content present in the request body or the content mime-type is not supported for app deployment. The status-description contains additional information."
    )})
    @ApiImplicitParams({@ApiImplicitParam(
            name = "file",
            paramType = "form",
            dataType = "java.io.File"
    )})
    @PostMapping(
            value = {"/app-repository/deployments"},
            produces = {"application/json"},
            consumes = {"multipart/form-data"}
    )
    public AppDeploymentResponse uploadDeployment(@ApiParam(name = "tenantId") @RequestParam(value = "tenantId",required = false) String tenantId, HttpServletRequest request, HttpServletResponse response) {
        if (this.restApiInterceptor != null) {
            this.restApiInterceptor.executeNewDeploymentForTenantId(tenantId);
        }

        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new FlowableIllegalArgumentException("Multipart request is required");
        } else {
            String queryString = request.getQueryString();
            Map<String, String> decodedQueryStrings = this.splitQueryString(queryString);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            if (multipartRequest.getFileMap().size() == 0) {
                throw new FlowableIllegalArgumentException("Multipart request with file content is required");
            } else {
                MultipartFile file = (MultipartFile)multipartRequest.getFileMap().values().iterator().next();

                try {
                    AppDeploymentBuilder deploymentBuilder = this.appRepositoryService.createDeployment();
                    String fileName = file.getOriginalFilename();
                    if (StringUtils.isEmpty(fileName) || !fileName.endsWith(".app") && !fileName.toLowerCase().endsWith(".bar") && !fileName.toLowerCase().endsWith(".zip")) {
                        fileName = file.getName();
                    }

                    if (fileName.endsWith(".app")) {
                        deploymentBuilder.addInputStream(fileName, file.getInputStream());
                    } else {
                        if (!fileName.toLowerCase().endsWith(".bar") && !fileName.toLowerCase().endsWith(".zip")) {
                            throw new FlowableIllegalArgumentException("File must be of type .app");
                        }

                        deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
                    }

                    if (decodedQueryStrings.containsKey("deploymentName") && !StringUtils.isEmpty((CharSequence)decodedQueryStrings.get("deploymentName"))) {
                        deploymentBuilder.name((String)decodedQueryStrings.get("deploymentName"));
                    } else {
                        String fileNameWithoutExtension = fileName.split("\\.")[0];
                        if (StringUtils.isNotEmpty(fileNameWithoutExtension)) {
                            fileName = fileNameWithoutExtension;
                        }

                        deploymentBuilder.name(fileName);
                    }

                    if (decodedQueryStrings.containsKey("deploymentKey") && StringUtils.isNotEmpty((CharSequence)decodedQueryStrings.get("deploymentKey"))) {
                        deploymentBuilder.key((String)decodedQueryStrings.get("deploymentKey"));
                    }

                    if (tenantId != null) {
                        deploymentBuilder.tenantId(tenantId);
                    }

                    deploymentBuilder.name(fileName);
                    if (tenantId != null) {
                        deploymentBuilder.tenantId(tenantId);
                    }

                    if (this.restApiInterceptor != null) {
                        this.restApiInterceptor.enhanceDeployment(deploymentBuilder);
                    }

                    AppDeployment deployment = deploymentBuilder.deploy();
                    response.setStatus(HttpStatus.CREATED.value());
                    return this.appRestResponseFactory.createAppDeploymentResponse(deployment);
                } catch (Exception var11) {
                    if (var11 instanceof FlowableException) {
                        throw (FlowableException)var11;
                    } else {
                        throw new FlowableException(var11.getMessage(), var11);
                    }
                }
            }
        }
    }
    public Map<String, String> splitQueryString(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return Collections.emptyMap();
        } else {
            Map<String, String> queryMap = new HashMap();
            String[] var3 = queryString.split("&");
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String param = var3[var5];
                queryMap.put(StringUtils.substringBefore(param, "="), this.decode(StringUtils.substringAfter(param, "=")));
            }

            return queryMap;
        }
    }

    protected String decode(String string) {
        if (string != null) {
            try {
                return URLDecoder.decode(string, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                throw new IllegalStateException("JVM does not support UTF-8 encoding.", var3);
            }
        } else {
            return null;
        }
    }
}

