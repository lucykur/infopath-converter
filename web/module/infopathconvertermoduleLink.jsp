<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="infopathconvertermodule.link.name" /></h2>

<br/>
<form action="module/infopathconverter/infopathconvertermoduleLink.form" method="POST">
<table>
 <tr>
 <td>
 </td>
 <td>
 <input type="file"/>
 </td>
 </tr>
 <tr>
  <td></td>
  <td>
    <textarea></textarea>
  </td>
 </tr>
</table>
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>
