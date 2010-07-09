package org.openmrs.module.infopathconverter;

public class SampleTestElements {


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


    public static final String PATIENT = String.format("%s%s%s", HEADER, "<span class='xdTextBox' xd:binding='patient/patient.given_name' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.given_name'/></span>" +
            "<span class='xdTextBox' xd:binding='patient/patient.family_name' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.family_name'/></span>" +
            "<span class='xdTextBox' xd:binding='patient/patient.medical_record_number' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.medical_record_number'/></span>", FOOTER);


    public static final String OTHER = String.format("%s%s%s", HEADER, "<span class='xdTextBox' xd:binding='obs1/patient.family_name' xd:CtrlId='CTRL5'> <xsl:value-of select='patient/patient.family_name'/></span>", FOOTER);

    static String locationRusumo =
            "<em>" +
                    "<input class='xdBehavior_Boolean' name='{generate-id(encounter/encounter.location_id)}' " +
                    "xd:binding='encounter/encounter.location_id' xd:xctname='OptionButton' xd:onValue='30'>" +
                    "<xsl:attribute name='xd:value'>" +
                    "<xsl:value-of select='encounter/encounter.location_id'/>" +
                    "</xsl:attribute>" +
                    "<xsl:if test='encounter/encounter.location_id=&quot;30&quot;'>" +
                    "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
                    "</xsl:if>" +
                    "</input>" +
                    "</em>";


    static String locationMulundi =
            "<em>" +
                    "<strong>" +
                    "<input class='xdBehavior_Boolean' name='{generate-id(encounter/encounter.location_id)}'  xd:binding='encounter/encounter.location_id' xd:onValue='27'>" +
                    "<xsl:attribute name='xd:value'>" +
                    "<xsl:value-of select='encounter/encounter.location_id'/>" +
                    "</xsl:attribute>" +
                    "<xsl:if test='encounter/encounter.location_id=&quot;27&quot;'>" +
                    "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
                    "</xsl:if>" +
                    "</input>" +
                    "</strong>" +
                    "</em>";

    public static String ENCOUNTER_LOCATION = String.format("%s%s%s", HEADER, "<div>" + locationMulundi + locationRusumo + "</div>", FOOTER);

    public static final String ENCOUNTER_DATE = String.format("%s%s%s", HEADER, "<span hideFocus='1' class='xdDTText xdBehavior_GTFormattingNoBUI' contentEditable='true' tabIndex='0' xd:binding='encounter/encounter.encounter_datetime' xd:xctname='DTPicker_DTText' xd:datafmt='&quot;datetime&quot;,&quot;dateFormat:dd MMMM, yyyy;timeFormat:none;&quot;' xd:boundProp='xd:num' xd:innerCtrl='_DTText'>" +
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


    public static final String ENCOUNTER_PROVIDER = String.format("%s%s%s", HEADER, "<div><font>Form completed today by: </font>" +
            "<span xd:binding='substring-after(encounter/encounter.provider_id, &quot;^&quot;)'>" +
            "<xsl:value-of select=\"substring-after(encounter/encounter.provider_id, &quot;^&quot;)\"/>" +
            "</span>" +
            "</div>", FOOTER);


    public static final String OBSERVATION_CODED_XSL = String.format("%s%s%s", HEADER, "<div>" +
            "<input xd:binding=\"obs/patient_hospitalized/value\" type='checkbox' xd:onValue=\"1065^YES^99DCT\">" +
            "<xsl:attribute name=\"xd:value\">" +
            "<xsl:value-of select=\"obs/patient_hospitalized/value\"/>" +
            "</xsl:attribute>" +
            "<xsl:if test=\"obs/patient_hospitalized/value=&quot;1065^YES^99DCT&quot;\">" +
            "<xsl:attribute name=\"CHECKED\">CHECKED</xsl:attribute>" +
            "</xsl:if>" +
            "</input>" +
            "</div>", FOOTER);


    public static final String OBSERVATION_CODED_XML = "<form id=\"112\" name=\"Cardiology consultation\" version=\"1.6\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:openmrs=\"http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94\" xmlns:xd=\"http://schemas.microsoft.com/office/infopath/2003\">" +
            "<obs openmrs_concept=\"1238^MEDICAL RECORD OBSERVATIONS^99DCT\" openmrs_datatype=\"ZZ\">\n" +
            "    <patient_hospitalized openmrs_concept=\"3389^PATIENT HOSPITALIZED^99DCT\" openmrs_datatype=\"CWE\" multiple=\"0\">\n" +
            "        <date xsi:nil=\"true\"></date>\n" +
            "        <time xsi:nil=\"true\"></time>\n" +
            "        <value xsi:nil=\"true\"></value>\n" +
            "    </patient_hospitalized></obs>" +
            "</form>";


    public static final String OBSERVATION_CODED_NON_SPECIFIED_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<history_of_present_illness>" +
            "  <previous_echocardiogram_taken openmrs_concept='3301^PREVIOUS ECHOCARDIOGRAM TAKEN^99DCT' openmrs_datatype='CWE' multiple='0'>" +
            "            <date xsi:nil='true'></date>" +
            "            <time xsi:nil='true'></time>" +
            "            <value xsi:nil='true'></value>" +
            "      </previous_echocardiogram_taken>" +
            "</history_of_present_illness>" +
            "   </form>";

    public static final String OBSERVATION_CODED_NON_SPECIFIED_XSL = String.format("%s%s%s", HEADER, "<strong><input class='xdBehavior_Boolean' title='' type='checkbox' name='{generate-id(obs/history_of_present_illness/previous_echocardiogram_taken/value)}' tabIndex='0' xd:binding='obs/history_of_present_illness/previous_echocardiogram_taken/value' xd:xctname='OptionButton' xd:CtrlId='CTRL1314' xd:boundProp='xd:value' style='FONT-SIZE: 7pt; FONT-WEIGHT: normal'>" +
            "<xsl:attribute name='xd:value'>" +
            "<xsl:value-of select='obs/history_of_present_illness/previous_echocardiogram_taken/value'/>" +
            "</xsl:attribute>" +
            "<xsl:if test='obs/history_of_present_illness/previous_echocardiogram_taken/value=&quot;&quot;'>" +
            "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
            "</xsl:if>" +
            "</input>" +
            "</strong>", FOOTER);


    public static final String OBSERVATION_CODED_XSL_TYPE_ZZ = String.format("%s%s%s", HEADER, "<input class='xdBehavior_Boolean' title='' type='checkbox' tabIndex='0' xd:binding='obs/physical_exam/general_exam_construct/general_exam_findings/well_appearing' xd:xctname='CheckBox' xd:CtrlId='CTRL598' xd:boundProp='xd:value' xd:offValue='false' xd:onValue='true' style='FONT-FAMILY: Arial'>" +
            "<xsl:attribute name='xd:value'>" +
            "<xsl:value-of select='obs/physical_exam/general_exam_construct/general_exam_findings/well_appearing'/>" +
            "</xsl:attribute>" +
            "<xsl:if test='obs/physical_exam/general_exam_construct/general_exam_findings/well_appearing=&quot;true&quot;'>" +
            "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
            "</xsl:if>" +
            "</input>", FOOTER);

    public static final String OBSERVATION_CODED_XML_TYPE_ZZ = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<general_exam_construct openmrs_concept='2419^GENERAL EXAM CONSTRUCT^99DCT' openmrs_datatype='ZZ'>" +
            "        <general_exam_findings openmrs_concept='1119^GENERAL EXAM FINDINGS^99DCT' openmrs_datatype='CWE' multiple='0'>" +
            "                <date xsi:nil='true'></date>" +
            "                <time xsi:nil='true'></time>" +
            "        </general_exam_findings>" +
            "      </general_exam_construct>" +
            "</form>";
    public static final String OBSERVATION_CODED_MULTIPLE_XSL = OBSERVATION_CODED_XSL_TYPE_ZZ;

    public static final String OBSERVATION_CODED_MULTIPLE_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<general_exam_construct openmrs_concept='2419^GENERAL EXAM CONSTRUCT^99DCT' openmrs_datatype='ZZ'>" +
            "       <general_exam_findings openmrs_concept='1119^GENERAL EXAM FINDINGS^99DCT' openmrs_datatype='CWE' multiple='1'>" +
            "         <oedema openmrs_concept='460^OEDEMA^99DCT'>false</oedema> " +
            "         <jaundice openmrs_concept='215^JAUNDICE^99DCT'>false</jaundice> " +
            "         <lymphadenopathy openmrs_concept='161^LYMPHADENOPATHY^99DCT'>false</lymphadenopathy>" +
            "        </general_exam_findings>" +
            "      </general_exam_construct>" +
            "</form>";


    public static final String OBSERVATION_CODED_RADIO_XSL = String.format("%s%s%s", HEADER, "<font face='Arial'>" +
            "<div><input class='xdBehavior_Booleanehavior_Boolean' title='' type='radio' name='{generate-id(obs/history_of_present_illness/nyha_class/value)}' tabIndex='0' xd:binding='obs/history_of_present_illness/nyha_class/value' xd:xctname='OptionButton' xd:CtrlId='CTRL1136' xd:boundProp='xd:value' xd:onValue='3135^NYHA CLASS 1^99DCT'>" +
            "<xsl:attribute name='xd:value'>" +
            "<xsl:value-of select='obs/history_of_present_illness/nyha_class/value'/>" +
            "</xsl:attribute>" +
            "<xsl:if test='obs/history_of_present_illness/nyha_class/value=&quot;3135^NYHA CLASS 1^99DCT&quot;'>" +
            "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
            "</xsl:if>" +
            "</input>" +
            "<font face='Arial'>" +
            "<font size='1'>NYHA classe I: (Asymptomatique) Pas de limitations d'activites/ <strong>" +
            "<em>" +
            "<font color='#808080'>(Asymptomatic) No limitations on activity </font>" +
            "</em>" +
            "</strong>" +
            "</font>" +
            "</font>" +
            "</div>" +
            "<div><input class='xdBehavior_Boolean' title='' type='radio' name='{generate-id(obs/history_of_present_illness/nyha_class/value)}' tabIndex='0' xd:binding='obs/history_of_present_illness/nyha_class/value' xd:xctname='OptionButton' xd:CtrlId='CTRL1457' xd:boundProp='xd:value' xd:onValue='3114^NYHA CLASS 1 AND 2^99DCT'>" +
            "<xsl:attribute name='xd:value'>" +
            "<xsl:value-of select='obs/history_of_present_illness/nyha_class/value'/>" +
            "</xsl:attribute>" +
            "<xsl:if test='obs/history_of_present_illness/nyha_class/value=&quot;3114^NYHA CLASS 1 AND 2^99DCT&quot;'>" +
            "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
            "</xsl:if>" +
            "</input>" +
            "<font face='Arial'>" +
            "<font size='1'>NYHA classe I/II</font>" +
            "</font>" +
            "</div></font>", FOOTER);

    public static final String OBSERVATION_CODED_RADIO_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<history_of_present_illness><nyha_class openmrs_concept='3139^NYHA CLASS^99DCT' openmrs_datatype='CWE' multiple='0'>" +
            "            <date xsi:nil='true'></date>" +
            "            <time xsi:nil='true'></time>" +
            "            <value xsi:nil='true'></value>" +
            "      </nyha_class></history_of_present_illness>" +
            "</form>";


    public static final String OBSERVATION_CODED_RADIO_XSD = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "           xmlns:openmrs=\"http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94\"\n" +
            "           elementFormDefault=\"qualified\"\n" +
            "           attributeFormDefault=\"unqualified\">" +
            "<xs:complexType name='nyha_class_type'>" +
            "  <xs:sequence>" +
            "    <xs:element name='date' type='xs:date' nillable='true' minOccurs='0' />" +
            "    <xs:element name='time' type='xs:time' nillable='true' minOccurs='0' />" +
            "    <xs:element name='value' minOccurs='0' maxOccurs='1' nillable='1'>" +
            "      <xs:simpleType>" +
            "        <xs:restriction base='xs:string'>" +
            "          <xs:enumeration value='3138^NYHA CLASS 4^99DCT' /> <!-- NYHA CLASS 4 -->" +
            "          <xs:enumeration value='3135^NYHA CLASS 1^99DCT' /> <!-- NYHA CLASS 1 -->" +
            "          <xs:enumeration value='3136^NYHA CLASS 2^99DCT' /> <!-- NYHA CLASS 2 -->" +
            "          <xs:enumeration value='6246^NYHA CLASS 3 AND 4^99DCT' /> <!-- NYHA CLASS 3 AND 4 -->" +
            "          <xs:enumeration value='3137^NYHA CLASS 3^99DCT' /> <!-- NYHA CLASS 3 -->" +
            "          <xs:enumeration value='3114^NYHA CLASS 1 AND 2^99DCT' /> <!-- NYHA CLASS 1 AND 2 -->" +
            "          <xs:enumeration value='3999^NYHA CLASS 2 AND 3^99DCT' /> <!-- NYHA CLASS 2 AND 3 -->" +
            "        </xs:restriction>" +
            "      </xs:simpleType>" +
            "    </xs:element>" +
            "  </xs:sequence>" +
            "  <xs:attribute name='openmrs_concept' type='xs:string' use='required' fixed='3139^NYHA CLASS^99DCT' />" +
            "  <xs:attribute name='openmrs_datatype' type='xs:string' use='required' fixed='CWE' />" +
            "  <xs:attribute name='multiple' type='xs:integer' use='required' fixed='0' />" +
            "</xs:complexType>" +
            "</xs:schema>";

    public static final String OBSERVATION_CODED_BIT_XSL = String.format("%s%s%s", HEADER, "<input class='xdBehavior_Boolean' title='' type='checkbox' tabIndex='0' xd:binding='obs/history_of_present_illness/history_of_present_illness_duplicated_on_another_form/value' xd:xctname='CheckBox' xd:CtrlId='CTRL1358' xd:boundProp='xd:value' xd:offValue='false' xd:onValue='true'>" +
            "<xsl:attribute name='xd:value'>" +
            "<xsl:value-of select='obs/history_of_present_illness/history_of_present_illness_duplicated_on_another_form/value'/>" +
            "</xsl:attribute>" +
            "<xsl:if test='obs/history_of_present_illness/history_of_present_illness_duplicated_on_another_form/value=&quot;true&quot;'>" +
            "<xsl:attribute name='CHECKED'>CHECKED</xsl:attribute>" +
            "</xsl:if>" +
            "</input>", FOOTER);

    public static final String OBSERVATION_CODED_BIT_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<history_of_present_illness>" +
            "      <history_of_present_illness_duplicated_on_another_form openmrs_concept='6208^HISTORY OF PRESENT ILLNESS DUPLICATED ON ANOTHER FORM^99DCT' openmrs_datatype='BIT'>" +
            "            <date xsi:nil='true'></date>" +
            "            <time xsi:nil='true'></time>" +
            "            <value infopath_boolean_hack='1' xsi:nil='true'></value>" +
            "      </history_of_present_illness_duplicated_on_another_form></history_of_present_illness>" + "</form>";

    public static final String OBSERVATION_CODED_TEXT_XSL = String.format("%s%s%s", HEADER, "" +
            "    <font color='#808080'><span hideFocus='1' class='xdTextBox' title='' tabIndex='0' xd:binding='obs/history_of_present_illness/relevant_interval_history/value' xd:xctname='PlainText' xd:CtrlId='CTRL1359' style='WIDTH: 100%; HEIGHT: 56px'>" +
            "<xsl:value-of select='obs/history_of_present_illness/relevant_interval_history/value'/>" +
            "</span>" +
            "</font>", FOOTER);

    public static final String OBSERVATION_CODED_TEXT_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<history_of_present_illness>" +
            " <relevant_interval_history openmrs_concept=\"3221^RELEVANT INTERVAL HISTORY^99DCT\" openmrs_datatype=\"ST\">\n" +
            "            <date xsi:nil=\"true\"></date>\n" +
            "            <time xsi:nil=\"true\"></time>\n" +
            "            <value xsi:nil=\"true\"></value>\n" +
            "      </relevant_interval_history>" +
            "</history_of_present_illness>" +
            "</form>";
    public static final String OBSERVATION_CODED_NUMERIC_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<history_of_present_illness>" +
            " <relevant_interval_history openmrs_concept=\"3221^RELEVANT INTERVAL HISTORY^99DCT\" openmrs_datatype=\"NM\">\n" +
            "            <date xsi:nil=\"true\"></date>\n" +
            "            <time xsi:nil=\"true\"></time>\n" +
            "            <value xsi:nil=\"true\"></value>\n" +
            "      </relevant_interval_history>" +
            "</history_of_present_illness>" +
            "</form>";

    public static final String OBSERVATION_CODED_DATE_XML = "<form id='112' name='Cardiology consultation' version='1.6' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:openmrs='http://staging.pih-emr.org:8080/openmrs/moduleServlet/formentry/forms/schema/112-94' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003'>" +
            "<history_of_present_illness>" +
            " <relevant_interval_history openmrs_concept=\"3221^RELEVANT INTERVAL HISTORY^99DCT\" openmrs_datatype=\"DT\">\n" +
            "            <date xsi:nil=\"true\"></date>\n" +
            "            <time xsi:nil=\"true\"></time>\n" +
            "            <value xsi:nil=\"true\"></value>\n" +
            "      </relevant_interval_history>" +
            "</history_of_present_illness>" +
            "</form>";
    public static final String SUBMIT_XSL = String.format("%s%s%s", HEADER, "<input style='WIDTH: 129px; FONT-FAMILY: Arial; FONT-SIZE: medium' class='langFont' " +
            "title='' value='Submit' size='1' type='button' xd:xctname='Button' xd:CtrlId='SubmitButton' tabIndex='0'/>", FOOTER);
}



    

