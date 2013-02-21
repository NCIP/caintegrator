<%
session.invalidate();
response.sendRedirect("sessionTimeout.action");
%>