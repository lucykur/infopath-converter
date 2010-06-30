<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="infopathconverter.link.name" /></h2>

<br/>
<form action="infopathconvertermoduleLink.form" method="POST" enctype ="multipart/form-data">
   <table>
       <tr>
           <td>
               <input type="file" name="xsn"></input>
           </td>
           <td>
               <input type="submit" name="submit"></input>
           </td>
       </tr>
       <tr>
           <td colspan="2">
               <textarea rows="20" cols="60">${htmlform}</textarea>
           </td>
       </tr>
   </table>
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>
