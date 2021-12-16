<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
        http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Patients List</web-resource-name>
            <url-pattern>/api/patients</url-pattern>
            <http-method>GET</http-method>
        </web-resource-collection>
        <auth-constraint>
            <role-name>Administrator</role-name>
        </auth-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Patients create</web-resource-name>
            <url-pattern>/api/patients</url-pattern>
            <http-method>POST</http-method>
        </web-resource-collection>
        <auth-constraint>
            <role-name>Administrator</role-name>
        </auth-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Patients update and delete</web-resource-name>
            <url-pattern>/api/patients/*</url-pattern>
            <http-method>PUT</http-method>
            <http-method>DELETE</http-method>
        </web-resource-collection>
        <auth-constraint>
            <role-name>Administrator</role-name>
        </auth-constraint>
    </security-constraint>
    <security-role>
        <description>Administrator role</description>
        <role-name>Administrator</role-name>
    </security-role>
    <login-config>
        <auth-method>BEARER_TOKEN</auth-method>
        <realm-name>jwt-realm</realm-name>
    </login-config>
</web-app>