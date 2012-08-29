<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="/caintegrator/common/js/dynatree/jquery.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $('#viewStackTrace').click(function() {
            $('#stackTrace').toggle();
        });
        $('#stackTrace').hide();
    });
</script>

<div id="content" class="clearfix">
    <div id="main">
        <h2>An unexpected error has occurred</h2>
        <p>Please report this error to your system administrator or appropriate technical support personnel. Thank you for your cooperation.</p>
        <hr />
        <h3>Error Message</h3>
        <s:actionerror />
        <p>
            <s:property value="%{exception.message}" />
        </p>
        <hr />
        <h3>Technical Details</h3>
        <a id="viewStackTrace" href="#">Click to toggle stack trace.</a>
        <div id="stackTrace">
            <p>
                <s:property value="%{exceptionStack}" />
            </p>
        </div>
    </div>
</div>
