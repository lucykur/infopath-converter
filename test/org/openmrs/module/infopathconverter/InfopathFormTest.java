package org.openmrs.module.infopathconverter;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

public class InfopathFormTest {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<xsl:stylesheet version=\"1.0\" xmlns:xsf2=\"http://schemas.microsoft.com/office/infopath/2006/solutionDefinition/extensions\" xmlns:xdEnvironment=\"http://schemas.microsoft.com/office/infopath/2006/xslt/environment\" xmlns:xdUser=\"http://schemas.microsoft.com/office/infopath/2006/xslt/User\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:openmrs=\"http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-92\" xmlns:my=\"http://schemas.microsoft.com/office/infopath/2003/myXSD/2006-07-25T11:22:21\" xmlns:xd=\"http://schemas.microsoft.com/office/infopath/2003\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:msxsl=\"urn:schemas-microsoft-com:xslt\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns:xdExtension=\"http://schemas.microsoft.com/office/infopath/2003/xslt/extension\" xmlns:xdXDocument=\"http://schemas.microsoft.com/office/infopath/2003/xslt/xDocument\" xmlns:xdSolution=\"http://schemas.microsoft.com/office/infopath/2003/xslt/solution\" xmlns:xdFormatting=\"http://schemas.microsoft.com/office/infopath/2003/xslt/formatting\" xmlns:xdImage=\"http://schemas.microsoft.com/office/infopath/2003/xslt/xImage\" xmlns:xdUtil=\"http://schemas.microsoft.com/office/infopath/2003/xslt/Util\" xmlns:xdMath=\"http://schemas.microsoft.com/office/infopath/2003/xslt/Math\" xmlns:xdDate=\"http://schemas.microsoft.com/office/infopath/2003/xslt/Date\" xmlns:sig=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xdSignatureProperties=\"http://schemas.microsoft.com/office/infopath/2003/SignatureProperties\">" +
            "<xsl:output method=\"html\" indent=\"no\"/>" +
            "<xsl:template match=\"form\">" +
            "<html xmlns:ipApp=\"http://schemas.microsoft.com/office/infopath/2006/XPathExtension/ipApp\" xmlns:ns1=\"http://schema.iukenya.org/2006/AMRS/FormEntry/15\" xmlns:openmrs=\"http://schema.iukenya.org/2006/AMRS/FormEntry/15\" xmlns:my=\"http://schemas.microsoft.com/office/infopath/2003/myXSD/2005-08-07T13:39:21\" xmlns:d=\"http://schemas.microsoft.com/office/infopath/2003/ado/dataFields\" xmlns:dfs=\"http://schemas.microsoft.com/office/infopath/2003/dataFormSolution\">" +
            "<head>" +
            "<meta content=\"text/html\" http-equiv=\"Content-Type\"></meta>" +
            "</head>" +
            "<body>";

    private static final String FOOTER = "</body></html></xsl:template></xsl:stylesheet>";

    @Test
    public void shouldTransformPatient() throws Exception {
        String content = String.format("%s%s%s", HEADER, "<span class='xdTextBox' xd:binding='patient/patient.given_name' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.given_name'/></span>" +
                "<span class='xdTextBox' xd:binding='patient/patient.family_name' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.family_name'/></span>" +
                "<span class='xdTextBox' xd:binding='patient/patient.medical_record_number' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.medical_record_number'/></span>", FOOTER);
        InfopathForm form = new InfopathForm("page1.xsl", content);
        Document transformedXSN = form.toPage();
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", transformedXSN);

    }

    @Test
    public void shouldNotTransformBindingsNotStartingWithPatient() throws Exception {
        String content = String.format("%s%s%s", HEADER, "<span class='xdTextBox' xd:binding='obs1/patient.family_name' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.family_name'/></span>", FOOTER);
        InfopathForm form = new InfopathForm("page1.xsl", content);
        Document transformedXSN = form.toPage();
        assertXpathNotExists("//lookup[@expression='']", transformedXSN);
        assertXpathExists("//span", transformedXSN);
    }

    @Test
    public void shouldTransformEncounter() throws Exception {
        String content = String.format("%s%s%s", HEADER, "<span hideFocus='1' class='xdDTText xdBehavior_GTFormattingNoBUI' contentEditable='true' tabIndex='0' xd:binding='encounter/encounter.encounter_datetime' xd:xctname='DTPicker_DTText' xd:datafmt='&quot;datetime&quot;,&quot;dateFormat:dd MMMM, yyyy;timeFormat:none;&quot;' xd:boundProp='xd:num' xd:innerCtrl='_DTText'>" +
                "                        <xsl:attribute name='xd:num'>" +
                "                            <xsl:value-of select='encounter/encounter.encounter_datetime'/>" +
                "                        </xsl:attribute>" +
                "                        <xsl:choose>" +
                "                            <xsl:when test='not(string(encounter/encounter.encounter_datetime))'>" +
                "                                <xsl:attribute name='xd:ghosted'>true</xsl:attribute>Click -&gt;</xsl:when>" +
                "                            <xsl:when test='function-available(\"xdFormatting:formatString\")'>" +
                "                                <xsl:value-of select='xdFormatting:formatString(encounter/encounter.encounter_datetime,&quot;datetime&quot;,&quot;dateFormat:dd MMMM, yyyy;timeFormat:none;&quot;)'/>" +
                "                            </xsl:when>" +
                "                            <xsl:otherwise>" +
                "                                <xsl:value-of select='encounter/encounter.encounter_datetime'/>" +
                "                            </xsl:otherwise>" +
                "                        </xsl:choose>" +
                "                    </span>", FOOTER);
        InfopathForm form = new InfopathForm("encounter", content);
        Document transformedXSN = form.toPage();
        assertXpathNotExists("//span", transformedXSN);

    }

    @Test
    public void shouldTransformObservations() throws Exception {
        String content = String.format("%s%s%s", HEADER, "<input xd:binding='obs/patient_hospitalized/value'>" +
                "<xsl:attribute name='xd:value'>" +
                "<xsl:value-of select='obs/patient_hospitalized/value'/>" +
                "</xsl:attribute>" +
                "<xsl:if test='obs/patient_hospitalized/value=&quot;1065^YES^99DCT&quot;'>" +
                "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
                "</xsl:if>" +
                "</input>", FOOTER);
        InfopathForm form = new InfopathForm("obs", content);
        Document transformedXSN = form.toPage();
        assertXpathNotExists("//input", transformedXSN);
    }

}
