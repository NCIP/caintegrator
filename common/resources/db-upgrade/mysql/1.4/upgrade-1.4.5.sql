# 
# For the CSM UPT 4.2.3 release, modify values required by UPT.
#
update CSM_APPLICATION set DATABASE_URL="jdbc:mysql://localhost:3306/caintegrator2db"
    where APPLICATION_NAME = "caintegrator2";
update CSM_APPLICATION set DATABASE_USER_NAME="cai2"
    where APPLICATION_NAME = "caintegrator2";
update CSM_APPLICATION set DATABASE_PASSWORD="cLSrNlta3CI="
    where APPLICATION_NAME = "caintegrator2";
update CSM_APPLICATION set DATABASE_DIALECT="org.hibernate.dialect.MySQLDialect"
    where APPLICATION_NAME = "caintegrator2";
update CSM_APPLICATION set DATABASE_DRIVER="org.gjt.mm.mysql.Driver"
    where APPLICATION_NAME = "caintegrator2";
update CSM_APPLICATION set CSM_VERSION="4.2.3"
    where APPLICATION_NAME = "caintegrator2";
update CSM_APPLICATION set UPDATE_DATE=sysdate()
    where APPLICATION_NAME = "caintegrator2";